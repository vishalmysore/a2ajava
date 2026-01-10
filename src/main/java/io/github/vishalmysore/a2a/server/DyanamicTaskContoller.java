package io.github.vishalmysore.a2a.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.t4a.JsonUtils;
import com.t4a.detect.ActionCallback;
import com.t4a.predict.PredictionLoader;
import com.t4a.processor.*;
import com.t4a.processor.scripts.BaseScriptProcessor;
import com.t4a.processor.scripts.ScriptProcessor;
import com.t4a.processor.scripts.ScriptResult;

import com.t4a.transform.GeminiV2PromptTransformer;
import com.t4a.transform.PromptTransformer;
import io.github.vishalmysore.a2a.domain.*;

import io.github.vishalmysore.common.CallBackType;
import lombok.Getter;
import lombok.extern.java.Log;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This will be used to process dynamic tasks , the tasks are not hard coded but decided by AI
 * based on the input prompt
 */
@Log
public class DyanamicTaskContoller implements A2ATaskController {
    public static final String MESSAGE = "message";
    protected final Map<String, Task> tasks = new ConcurrentHashMap<>();
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    @Getter
    private final ExecutorService nonBlockingService = Executors.newCachedThreadPool();
    protected AIProcessor baseProcessor = new GeminiV2ActionProcessor();

    protected PromptTransformer promptTransformer = new GeminiV2PromptTransformer();



    private BaseScriptProcessor scriptProcessor;
    protected PromptTransformer getPromptTransformer() {
        return promptTransformer;
    }

    private JsonUtils utils = new JsonUtils();

    ObjectMapper objectMapper = new ObjectMapper();

    public DyanamicTaskContoller() {
      init();
    }


    @PostConstruct
    public void initOptionalProcessors (){
        scriptProcessor = new ScriptProcessor();
    }

    public BaseScriptProcessor getScriptProcessor() {
        return scriptProcessor;
    }

    public void init() {
       baseProcessor = PredictionLoader.getInstance().createOrGetAIProcessor();
    }
    @Override
    public AIProcessor getBaseProcessor() {
        return baseProcessor;
    }



    public SendTaskResponse sendTask(TaskSendParams taskSendParams, ActionCallback actionCallback,boolean isAsync) {
        String taskId = taskSendParams.getId();
        Task task;

        if (tasks.containsKey(taskId)) {
            task = tasks.get(taskId);
            List<Message> history = task.getHistory();
            if (history == null) {
                history = new ArrayList<>();
            }
            List<Message> mutableHistory = new ArrayList<>(history);
            mutableHistory.add(taskSendParams.getMessage());
            task.setHistory(mutableHistory);
        } else {
            task = new Task();
            task.setId(taskId);
            String sessionId = taskSendParams.getSessionId();
            if (sessionId == null || sessionId.isEmpty()) {
                sessionId = UUID.randomUUID().toString();
            }
            task.setSessionId(sessionId);
            task.setDetailedAndMessage(TaskState.SUBMITTED," Your Task with id " + taskId + " is submitted");
            task.setHistory(new ArrayList<>(List.of(taskSendParams.getMessage())));

            tasks.put(taskId, task);
        }

        processTaskLogicForSyncndAsync(taskSendParams, actionCallback, isAsync, task, taskId);

        SendTaskResponse response = new SendTaskResponse();
        response.setId(taskId);
        response.setResult(task);
        return response;
    }

    protected void processTaskLogicForSyncndAsync(TaskSendParams taskSendParams, ActionCallback actionCallback, boolean isAsync, Task task, String taskId) {
        if (isAsync) {
            nonBlockingService.execute(() -> processTaskLogic(taskSendParams, task, taskId, actionCallback));
        } else {
            processTaskLogic(taskSendParams, task, taskId, actionCallback);
        }
    }


    protected void processTaskLogic(TaskSendParams taskSendParams, Task task, String taskId, ActionCallback actionCallback) {
        try {
            processParts(taskSendParams, task, taskId, actionCallback);
        } catch (Exception e) {
            handleProcessingError(task, taskId, e);
        }
    }

    private void processParts(TaskSendParams taskSendParams, Task task, String taskId, ActionCallback actionCallback) throws AIProcessingException {
        List<Part> parts = taskSendParams.getMessage().getParts();
        if (parts == null || parts.isEmpty()) {
            return;
        }

        Part part = parts.get(0);
        if (part instanceof TextPart textPart) {
            processTextPart(textPart, task, actionCallback);
        } else if (part instanceof FilePart) {
            processFileTaskLogic(taskSendParams, task, taskId, actionCallback);
        } else if (part instanceof DataPart) {
            // Handle DataPart if needed

            String text = ((DataPart)part).dataToText();
            if (actionCallback != null) {
                processWithCallback(text, task, actionCallback);
            } else {
                processWithoutCallback(text, task);
            }
            log.info("Received DataPart, processing as text.");
        }
    }

    public  DataPart createA2uiDataPart(Map<String, Object> a2uiData) {
        // Create metadata map with the A2UI MIME type

        // Return new DataPart with the data and metadata
        return  DataPart.createA2UIPart(a2uiData);
    }
    private void processTextPart(TextPart textPart, Task task, ActionCallback actionCallback) throws AIProcessingException {
        if (!"text".equals(textPart.getType())) {
            return;
        }

        String text = textPart.getText();
        if (actionCallback != null) {
            processWithCallback(text, task, actionCallback);
        } else {
            processWithoutCallback(text, task);
        }
    }

    private void processWithCallback(String text, Task task, ActionCallback actionCallback) throws AIProcessingException {
        actionCallback.setContext(task);
        if(actionCallback.getType().equals(CallBackType.A2UI.name())) {
            Object obj = getBaseProcessor().processSingleAction(text,null, new LoggingHumanDecision(),new LogginggExplainDecision(),actionCallback);
            TaskStatus status = task.getStatus();
            status.setState(TaskState.COMPLETED);
            status.getMessage().getParts().add(createA2uiDataPart((Map<String, Object>) obj));
        } else {
            getBaseProcessor().processSingleAction(text, actionCallback);
        }
    }

    private void processWithoutCallback(String text, Task task) throws AIProcessingException {
        Object obj = getBaseProcessor().processSingleAction(text);
        updateTaskWithResult(task, obj);
    }

    private void updateTaskWithResult(Task task, Object obj) {
        List<Part> currentParts = task.getStatus().getMessage().getParts();
        List<Part> partsList = new ArrayList<>(currentParts != null ? currentParts : new ArrayList<>());

        if(obj instanceof DataPart) {
            partsList.add((DataPart)obj);
        } else {
            TextPart resultPart = createResultPart(obj);
            partsList.add(resultPart);
        }

        task.getStatus().setState(TaskState.COMPLETED);
        task.getStatus().getMessage().setParts(partsList);
    }

    private TextPart createResultPart(Object obj) {
        TextPart resultPart = new TextPart();
        resultPart.setType("text");
        resultPart.setText(obj != null ? JsonUtils.convertObjectToJson(obj) : "No result");
        return resultPart;
    }

    private void handleProcessingError(Task task, String taskId, Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));

        TaskStatus failedStatus = createFailedStatus();
        task.setStatus(failedStatus);

        log.severe("Complete stack trace:\n" + sw.toString());
        tasks.put(taskId, task);
    }

    private TaskStatus createFailedStatus() {
        TaskStatus failedStatus = new TaskStatus(TaskState.FAILED);
        Message errorMessage = new Message();
        errorMessage.setRole("agent");

        TextPart errorPart = new TextPart();
        errorPart.setType("text");
        errorPart.setText("Processing failed: Access Denied");

        errorMessage.setParts(List.of(errorPart));
        failedStatus.setMessage(errorMessage);
        return failedStatus;
    }
    protected void processFileTaskLogic(TaskSendParams taskSendParams, Task task, String taskId, ActionCallback actionCallback) {
        try {
            List<Part> parts = taskSendParams.getMessage().getParts();
            FilePart filePart = (FilePart) parts.get(0);
            FileContent fileInfo = filePart.getFile();
            String base64EcbodedString = fileInfo.getBytes();
            // Create an artifact for the file


            String originalString = new String(Base64.getDecoder().decode(base64EcbodedString));
            tasks.put(taskId, task);

                if(actionCallback!= null) {
                    actionCallback.setContext(task);
                }
                FileProcessingInfo info = (FileProcessingInfo) getPromptTransformer().transformIntoPojo(originalString,FileProcessingInfo.class);
                log.info("taskId " + taskId + " file info " + info);
                Path tempFile = Files.createTempFile(task.getId()+System.currentTimeMillis()+"web_steps_", ".txt");


                String fileName = tempFile.getFileName().toString();
                log.info("Created temp file: " + fileName);

                // Write steps to file
                Files.write(tempFile, originalString.getBytes());
                ScriptResult result  = getScriptProcessor().process(tempFile.toAbsolutePath().toString());
                String resultString = objectMapper.writeValueAsString(result);
                log.info(resultString);
                task.setDetailedAndMessage(TaskState.COMPLETED,resultString);
                Path archiveDir = Paths.get("archive");
                Files.createDirectories(archiveDir);
                Files.move(tempFile, archiveDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                log.info("Moved file to archive: " + fileName);


        } catch (AIProcessingException | IOException e) {
            log.warning(e.getMessage());
        }
    }


    @Override
    public ResponseEntity<Task> getTask(@RequestParam String id, @RequestParam(defaultValue = "0") int historyLength) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        //basic get
        if (historyLength == 0) {
            return ResponseEntity.ok(task);
        }
        else {
            //return history
            Task taskWithHistory = new Task();
            taskWithHistory.setId(task.getId());
            taskWithHistory.setSessionId(task.getSessionId());
            taskWithHistory.setStatus(task.getStatus());
            //get artifacts
            taskWithHistory.setArtifacts(task.getArtifacts());
            //get history
            List<Message> history = task.getHistory();
            if (history != null) {
                int start = Math.max(0, history.size() - historyLength);
                taskWithHistory.setHistory(history.subList(start, history.size()));
            }
            return ResponseEntity.ok(taskWithHistory);
        }
    }


    public ResponseEntity<Task> cancelTask(@RequestBody Map<String, String> body) {
        String id = body.get("id");
        Task task = tasks.get(id);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        task.setStatus(new TaskStatus("canceled"));
        tasks.put(id, task); //update
        return ResponseEntity.ok(task);
    }

    public String setTaskPushNotification(TaskSetPushNotificationParams params) {
        // Retrieve the task from the map
        Task task = tasks.get(params.getTaskId());

        if (task == null) {
            throw new IllegalArgumentException("Task not found");
        }

        // Set the push notification URL
        task.setPushNotificationUrl(params.getPushNotificationUrl());

        return "Push notification URL set successfully!";
    }

    public String resubscribeToTask(TaskResubscriptionParams params) {
        // Retrieve the task from the map
        Task task = tasks.get(params.getTaskId());

        if (task == null) {
            throw new IllegalArgumentException("Task not found");
        }

        // Resubscribe logic (e.g., resetting the task's subscription status)
        task.setSubscribed(true);
        task.setSubscriptionDateNow(new Date()); //

        return "Task resubscribed successfully!";
    }

    public String cancelTask(String taskId) {
        // Retrieve the task from the map
        Task task = tasks.get(taskId);

        if (task == null) {
            throw new IllegalArgumentException("Task not found");
        }

        // Mark the task as cancelled
        task.setCancelled(true);

        // Optionally, remove the task from the map if needed
         tasks.remove(taskId);

        return "Task cancelled successfully!";
    }
    public String getTaskPushNotification(TaskGetPushNotificationParams params) {
        // Retrieve the task from the map
        Task task = tasks.get(params.getTaskId());

        if (task == null) {
            throw new IllegalArgumentException("Task not found");
        }

        // Return the push notification URL
        return task.getPushNotificationUrl();
    }

    public ResponseEntity<TaskPushNotificationConfig> setTaskPushNotificationConfig(
            @RequestBody TaskPushNotificationConfigRequest request) {
        String id = request.getId();
        Task task = tasks.get(id);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        // In a real application, you would store this configuration
        // and use it when sending push notifications.  For this
        // example, we just store it in the Task object.
        task.setPushNotificationConfig(request.getPushNotificationConfig());
        tasks.put(id, task);
        return ResponseEntity.ok(request.getPushNotificationConfig());
    }


    public ResponseEntity<TaskPushNotificationConfig> getTaskPushNotificationConfig(@RequestParam String id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        TaskPushNotificationConfig config = task.getPushNotificationConfig();
        if (config == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(config);
    }
    public void sendSseEvent(String taskId, Object event) {
        SseEmitter emitter = emitters.get(taskId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(MESSAGE).data(event));
            } catch (IOException e) {
                // Handle client disconnection or error
                emitters.remove(taskId);
                emitter.completeWithError(e);
                log.severe("Error sending SSE event: " + e.getMessage()); // Log
            }        }
    }



    public SseEmitter sendSubscribeTask(TaskSendSubscribeParams params) {
        String id = params.getId();
        SseEmitter emitter = createEmitter(id);
        Task task = getOrCreateTask(params);

        processTaskAsync(task, emitter, id, params.getMessage());

        return emitter;
    }

    private SseEmitter createEmitter(String id) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(id, emitter);

        emitter.onCompletion(() -> {
            emitters.remove(id);
            log.info("Client disconnected for task: " + id);
        });

        emitter.onError((throwable) -> {
            emitters.remove(id);
            log.info("Error occurred for task " + id + ": " + throwable.getMessage());
        });

        emitter.onTimeout(() -> {
            emitters.remove(id);
            emitter.complete();
            log.info("Timeout occurred for task: " + id);
        });

        return emitter;
    }

    private Task getOrCreateTask(TaskSendSubscribeParams params) {
        String taskId = params.getId();
        Message message = params.getMessage();

        Task task = tasks.get(taskId);
        if (task != null) {
            List<Message> history = task.getHistory() != null ? new ArrayList<>(task.getHistory()) : new ArrayList<>();
            history.add(message);
            task.setHistory(history);
            return task;
        }

        task = new Task();
        task.setId(taskId);
        String sessionId = Optional.ofNullable(params.getSessionId()).filter(s -> !s.isEmpty()).orElse(UUID.randomUUID().toString());
        task.setSessionId(sessionId);
        task.setStatus(new TaskStatus(TaskState.SUBMITTED));
        task.setHistory(List.of(message));

        tasks.put(taskId, task);
        return task;
    }

    private void processTaskAsync(Task task, SseEmitter emitter, String id, Message message) {
        nonBlockingService.execute(() -> {
            try {
                List<Part> parts = message.getParts();
                if (parts != null && !parts.isEmpty()) {
                    Part part = parts.get(0);
                    if (part instanceof TextPart textPart && "text".equals(textPart.getType())) {
                        String text = textPart.getText();
                        SSEEmitterCallback callback = new SSEEmitterCallback(id, emitter);
                        callback.setContext(task);
                        getBaseProcessor().processSingleAction(text, callback);
                    }
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
    }

    public SseEmitter resubscribe(String id) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(id, emitter);

        Task task = tasks.get(id);
        if (task != null) {
            //send current status
            try {
                emitter.send(SseEmitter.event().name(MESSAGE).data(new TaskStatusUpdateEvent(id, task.getStatus(), false)));
                //send all artifacts
                if (task.getArtifacts() != null) {
                    for (Artifact artifact : task.getArtifacts()) {
                        emitter.send(SseEmitter.event().name(MESSAGE).data(new TaskArtifactUpdateEvent(id, artifact)));
                    }
                }

            } catch (IOException e) {
                emitters.remove(id);
                emitter.completeWithError(e);
                log.severe("Error re-subscribing" + e.getMessage());
            }
        }
        else {
            try {
                emitter.send(SseEmitter.event().name(MESSAGE).data("Task does not exist"));
                emitter.complete();
                emitters.remove(id);
            } catch (IOException e) {
                log.severe("Error sending task "+MESSAGE + e.getMessage());
            }

        }

        emitter.onCompletion(() -> {
            emitters.remove(id);
            log.severe("Client disconnected on resubscribe: " + id);
        });
        emitter.onError(throwable -> {
            emitters.remove(id);
            log.severe("Error on resubscribe for task " + id + ": " + throwable.getMessage());
        });
        emitter.onTimeout(() -> {
            emitters.remove(id);
            emitter.complete();
            log.severe("Timeout on resubscribe for task: " + id);
        });
        return emitter;
    }

    public JsonUtils getUtils() {
        return utils;
    }

    public void setUtils(JsonUtils utils) {
        this.utils = utils;
    }
}
