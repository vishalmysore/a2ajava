package io.github.vishalmysore.a2a.server;

import com.t4a.detect.ActionCallback;
import io.github.vishalmysore.a2a.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ErrorHandlingTest {

    private DyanamicTaskContoller controller;

    @Mock
    private ActionCallback mockCallback;

    @BeforeEach
    public void setUp() {
        controller = new DyanamicTaskContoller();
    }


    public void testNullParameterHandling() {
        // Test null task parameters
        assertThrows(NullPointerException.class, () -> {
            controller.sendTask(null, mockCallback);
        });

        // Test null message in parameters
        TaskSendParams params = new TaskSendParams();
        params.setId(UUID.randomUUID().toString());
        params.setMessage(null);
        
        SendTaskResponse response = controller.sendTask(params, mockCallback);
        assertEquals(TaskState.FAILED, response.getResult().getStatus().getState());
    }


    public void testInvalidStateTransitions() {
        // Create a task
        TaskSendParams params = createTaskParams("Test invalid transitions");
        SendTaskResponse response = controller.sendTask(params, mockCallback);
        String taskId = response.getId();

        // Try to cancel already completed task
        Task task = controller.getTask(taskId, 0).getBody();
        task.setStatus(new TaskStatus(TaskState.COMPLETED));
        
        String result = controller.cancelTask(taskId);
        assertTrue(result.contains("cancelled"));
        
        // Verify final state
        task = controller.getTask(taskId, 0).getBody();
        assertTrue(task.isCancelled());
    }


    public void testMalformedRequestHandling() {
        // Test empty message parts
        TaskSendParams params = new TaskSendParams();
        params.setId(UUID.randomUUID().toString());
        Message message = new Message();
        message.setParts(new ArrayList<>());
        params.setMessage(message);
        
        SendTaskResponse response = controller.sendTask(params, mockCallback);
        assertNotNull(response);
        assertEquals(TaskState.FAILED, response.getResult().getStatus().getState());

        // Test invalid message part type
        params = createTaskParams("Test invalid part");
        TextPart invalidPart = new TextPart();
        invalidPart.setType("invalid_type");
        params.getMessage().setParts(List.of(invalidPart));
        
        response = controller.sendTask(params, mockCallback);
        assertEquals(TaskState.FAILED, response.getResult().getStatus().getState());
    }


    public void testResourceCleanup() {
        // Create multiple tasks
        List<String> taskIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TaskSendParams params = createTaskParams("Task " + i);
            SendTaskResponse response = controller.sendTask(params, mockCallback);
            taskIds.add(response.getId());
        }

        // Cancel and verify cleanup
        for (String taskId : taskIds) {
            controller.cancelTask(taskId);
            Task task = controller.getTask(taskId, 0).getBody();
            assertTrue(task.isCancelled());
        }
    }


    public void testRecoveryFromErrors() {
        // Create task that will fail
        TaskSendParams params = createTaskParams("This will fail");
        params.getMessage().getParts().get(0).setType("invalid_type");
        SendTaskResponse response = controller.sendTask(params, mockCallback);
        String taskId = response.getId();

        // Verify failed state
        Task failedTask = controller.getTask(taskId, 0).getBody();
        assertEquals(TaskState.FAILED, failedTask.getStatus().getState());

        // Try to recover by sending new valid message
        params = createTaskParams("Recovery message");
        params.setId(taskId);
        response = controller.sendTask(params, mockCallback);

        // Verify recovery
        Task recoveredTask = controller.getTask(taskId, 0).getBody();
        assertNotEquals(TaskState.FAILED, recoveredTask.getStatus().getState());
    }


    public void testConcurrentErrorHandling() throws InterruptedException {
        // Create tasks that will fail concurrently
        int numTasks = 10;
        List<Thread> threads = new ArrayList<>();
        
        for (int i = 0; i < numTasks; i++) {
            Thread t = new Thread(() -> {
                TaskSendParams params = createTaskParams("Concurrent failure test");
                params.getMessage().getParts().get(0).setType("invalid_type");
                controller.sendTask(params, mockCallback);
            });
            threads.add(t);
            t.start();
        }

        // Wait for all threads to complete
        for (Thread t : threads) {
            t.join(TimeUnit.SECONDS.toMillis(5));
        }
    }

    private TaskSendParams createTaskParams(String messageText) {
        TaskSendParams params = new TaskSendParams();
        params.setId(UUID.randomUUID().toString());
        
        Message message = new Message();
        TextPart textPart = new TextPart();
        textPart.setType("text");
        textPart.setText(messageText);
        message.setParts(List.of(textPart));
        params.setMessage(message);
        
        return params;
    }
}
