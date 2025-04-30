package io.github.vishalmysore.server;

import com.t4a.processor.GeminiV2ActionProcessor;
import io.github.vishalmysore.domain.*;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This will be used to process dynamic tasks , the tasks are not hard coded but decided by AI
 * based on the input prompt
 */
@Log
@RestController
@RequestMapping("/dynamictasks")
public class DyanamicTaskContoller implements A2ATaskController {
    private final Map<String, Task> tasks = new ConcurrentHashMap<>();
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ExecutorService nonBlockingService = Executors.newCachedThreadPool();

    @Override
    public SendTaskResponse sendTask(TaskSendParams taskSendParams) {
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
            task.setStatus(new TaskStatus(TaskState.SUBMITTED));
            task.setHistory(List.of(taskSendParams.getMessage()));
            SendTaskResponse sendTaskResponse = new SendTaskResponse();
            tasks.put(taskId, task);
        }

        nonBlockingService.execute(() -> {
            try {
                List<Part> parts = taskSendParams.getMessage().getParts();
                if (parts != null && !parts.isEmpty()) {
                    Part part = parts.get(0);
                    if (part instanceof TextPart textPart && "text".equals(textPart.getType())) {
                        String text = textPart.getText();
                        GeminiV2ActionProcessor processor = new GeminiV2ActionProcessor();

                        processor.processSingleAction(text);
                    }
                }
            } catch (Exception e) {
                TaskStatus failedStatus = new TaskStatus(TaskState.FAILED);
                Message errorMessage = new Message();
                errorMessage.setRole("agent");
                TextPart errorPart = new TextPart();
                errorPart.setType("text");
                errorPart.setText("Processing failed: " + e.getMessage());
                errorMessage.setParts(List.of(errorPart));
                failedStatus.setMessage(errorMessage);
                task.setStatus(failedStatus);
                tasks.put(taskId, task);
            }
        });

        SendTaskResponse response = new SendTaskResponse();
        response.setId(taskId);
        response.setResult(task);
        return response;
    }


    @Override
    public SseEmitter sendSubscribeTask(TaskSendSubscribeParams params) {
        String id = params.getId(); // assuming your params object has an id field
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); //timeout
        emitters.put(id, emitter);
        nonBlockingService.execute(() -> {
            try {
                List<Part> parts = params.getMessage().getParts();
                String messageId = (String) params.getMessage().getMetadata().get("message_id");
                if (parts != null && !parts.isEmpty()) {
                    Part part = parts.get(0);
                    if (part instanceof TextPart textPart && "text".equals(textPart.getType())) {
                        // Process text part
                        String text = textPart.getText();
                        // Use text for GeminiV2ActionProcessor
                        GeminiV2ActionProcessor processor = new GeminiV2ActionProcessor();
                        SSEEmitterCallback sseEmitterCallback = new SSEEmitterCallback(id,emitter);
                        processor.processSingleAction(text, sseEmitterCallback);
                    }
                }

            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        //handle client disconnects
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
}
