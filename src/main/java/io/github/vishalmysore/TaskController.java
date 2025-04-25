package io.github.vishalmysore;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/tasks")
class TaskController {

    private final Map<String, Task> tasks = new ConcurrentHashMap<>();
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ExecutorService nonBlockingService = Executors.newCachedThreadPool();

    // Helper method to send SSE events
    private void sendSseEvent(String taskId, Object event) {
        SseEmitter emitter = emitters.get(taskId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("message").data(event));
            } catch (IOException e) {
                // Handle client disconnection or error
                emitters.remove(taskId);
                emitter.completeWithError(e);
                System.err.println("Error sending SSE event: " + e.getMessage()); // Log
            }        }
    }

    @PostMapping("/send")
    public ResponseEntity<Task> sendTask(@RequestBody TaskSendParams taskSendParams) {
        String taskId = taskSendParams.getId();
        Task task;
        if (tasks.containsKey(taskId)) {
            task = tasks.get(taskId);
            // In a real implementation, you would check the task's status
            // and determine if a new message can be added.  For this example,
            // we'll just add the message.
            List<Message> history = task.getHistory();
            if (history == null) {
                history = new ArrayList<>();
            }
            List<Message> mutableHistory = new ArrayList<>(history);
            mutableHistory.add(taskSendParams.getMessage());
            task.setHistory(mutableHistory);

        } else {
            //creates a new task
            task = new Task();
            task.setId(taskId);
            String sessionId = taskSendParams.getSessionId();
            if (sessionId == null || sessionId.isEmpty()) {
                sessionId = UUID.randomUUID().toString();
            }
            task.setSessionId(sessionId);
            task.setStatus(new TaskStatus("submitted")); // Initial status
            task.setHistory(List.of(taskSendParams.getMessage())); //adds the first message
            tasks.put(taskId, task);
        }

        // Simulate ticket booking (long-running)
        nonBlockingService.execute(() -> {
            try {
                // Simulate processing time
                Thread.sleep(3000);
                TextPart part1 = (TextPart) taskSendParams.getMessage().getParts().get(0);
                //check for input required.
                if (part1.getText().toLowerCase().contains("change")) {
                    TaskStatus inputRequiredStatus = new TaskStatus("input-required");
                    Message agentMessage = new Message();
                    agentMessage.setRole("agent");
                    TextPart textPart = new TextPart();
                    textPart.setType("text");
                    textPart.setText("Please provide the new date for your ticket");
                    agentMessage.setParts(List.of(textPart));
                    inputRequiredStatus.setMessage(agentMessage);
                    task.setStatus(inputRequiredStatus);
                    tasks.put(taskId, task);
                    sendSseEvent(taskId, new TaskStatusUpdateEvent(taskId, inputRequiredStatus, false));
                }
                else {
                    TaskStatus workingStatus = new TaskStatus("working");
                    Message agentWorkingMessage = new Message();
                    agentWorkingMessage.setRole("agent");
                    TextPart workingTextPart = new TextPart();
                    workingTextPart.setType("text");
                    workingTextPart.setText("Booking your ticket...");
                    workingStatus.setMessage(agentWorkingMessage);
                    task.setStatus(workingStatus);
                    tasks.put(taskId, task);
                    sendSseEvent(taskId, new TaskStatusUpdateEvent(taskId, workingStatus, false));
                    Thread.sleep(2000);  // Simulate booking process

                    //create a success status
                    TaskStatus completedStatus = new TaskStatus("completed");
                    Message agentMessage = new Message();
                    agentMessage.setRole("agent");
                    TextPart textPart = new TextPart();
                    textPart.setType("text");
                    textPart.setText("Ticket booked successfully! Confirmation number: " + UUID.randomUUID());
                    agentMessage.setParts(List.of(textPart));
                    completedStatus.setMessage(agentMessage);

                    // Create an artifact.
                    Artifact artifact = new Artifact();
                    artifact.setName("Ticket Confirmation");
                    artifact.setDescription("Your airline ticket confirmation");
                    TextPart artifactTextPart = new TextPart();
                    artifactTextPart.setType("text");
                    artifactTextPart.setText("Your ticket is confirmed.  Details will be sent to your email.");
                    artifact.setParts(List.of(artifactTextPart));
                    artifact.setIndex(0);
                    artifact.setAppend(false);
                    artifact.setLastChunk(true);

                    task.setArtifacts(List.of(artifact));
                    task.setStatus(completedStatus);
                    tasks.put(taskId, task);
                    sendSseEvent(taskId, new TaskStatusUpdateEvent(taskId, completedStatus, true)); //send final
                    sendSseEvent(taskId, new TaskArtifactUpdateEvent(taskId, artifact));

                    // Complete the SSE emitter if it exists
                    SseEmitter emitter = emitters.get(taskId);
                    if (emitter != null) {
                        emitter.complete();
                        emitters.remove(taskId);
                    }
                }


            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
                TaskStatus failedStatus = new TaskStatus("failed");
                Message agentErrorMessage = new Message();
                agentErrorMessage.setRole("agent");
                TextPart errorPart = new TextPart();
                errorPart.setType("text");
                errorPart.setText("Ticket booking failed: " + e.getMessage());
                failedStatus.setMessage(agentErrorMessage);
                task.setStatus(failedStatus);
                tasks.put(taskId, task);
                sendSseEvent(taskId, new TaskStatusUpdateEvent(taskId, failedStatus, true)); //send final
                SseEmitter emitter = emitters.get(taskId);
                if (emitter != null) {
                    emitter.completeWithError(e);
                    emitters.remove(taskId);
                }
            }
        });

        return ResponseEntity.ok(task);
    }

    @GetMapping("/get")
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

    @PostMapping("/cancel")
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

    @PostMapping("/pushNotification/set")
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

    @GetMapping("/pushNotification/get")
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

    @GetMapping(value = "/sendSubscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sendSubscribe(@PathVariable String id) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); //timeout
        emitters.put(id, emitter);

        //handle client disconnects
        emitter.onCompletion(() -> {
            emitters.remove(id);
            System.out.println("Client disconnected for task: " + id);
        });
        emitter.onError((throwable) -> {
            emitters.remove(id);
            System.err.println("Error occurred for task " + id + ": " + throwable.getMessage());
        });
        emitter.onTimeout(() -> {
            emitters.remove(id);
            emitter.complete();
            System.out.println("Timeout occurred for task: " + id);
        });
        return emitter;
    }

    @GetMapping(value = "/resubscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter resubscribe(@PathVariable String id) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(id, emitter);

        Task task = tasks.get(id);
        if (task != null) {
            //send current status
            try {
                emitter.send(SseEmitter.event().name("message").data(new TaskStatusUpdateEvent(id, task.getStatus(), false)));
                //send all artifacts
                if (task.getArtifacts() != null) {
                    for (Artifact artifact : task.getArtifacts()) {
                        emitter.send(SseEmitter.event().name("message").data(new TaskArtifactUpdateEvent(id, artifact)));
                    }
                }

            } catch (IOException e) {
                emitters.remove(id);
                emitter.completeWithError(e);
                System.err.println("Error re-subscribing" + e.getMessage());
            }
        }
        else {
            try {
                emitter.send(SseEmitter.event().name("message").data("Task does not exist"));
                emitter.complete();
                emitters.remove(id);
            } catch (IOException e) {
                System.err.println("Error sending task несуществует message" + e.getMessage());
            }

        }

        emitter.onCompletion(() -> {
            emitters.remove(id);
            System.out.println("Client disconnected on resubscribe: " + id);
        });
        emitter.onError((throwable) -> {
            emitters.remove(id);
            System.err.println("Error on resubscribe for task " + id + ": " + throwable.getMessage());
        });
        emitter.onTimeout(() -> {
            emitters.remove(id);
            emitter.complete();
            System.out.println("Timeout on resubscribe for task: " + id);
        });
        return emitter;
    }
}

