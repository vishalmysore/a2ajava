package io.github.vishalmysore.a2a.server;

import io.github.vishalmysore.a2a.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DyanamicTaskContollerExtendedTest {

    private DyanamicTaskContoller controller;

    @BeforeEach
    void setUp() {
        controller = new DyanamicTaskContoller();
    }

    @Test
    void testSendTaskWithNewTask() {
        // Create basic task params
        TaskSendParams params = new TaskSendParams();
        String taskId = UUID.randomUUID().toString();
        params.setId(taskId);
        
        // Create a simple message
        Message message = new Message();
        TextPart textPart = new TextPart();
        textPart.setText("Test message");
        message.setParts(Collections.singletonList(textPart));
        params.setMessage(message);
        
        // Create task
        SendTaskResponse response = controller.sendTask(params, null, false);
        
        // Verify task was created correctly
        assertNotNull(response);
        assertEquals(taskId, response.getId());
        assertNotNull(response.getResult());
        assertEquals(taskId, response.getResult().getId());
        assertNotNull(response.getResult().getStatus());
    }
    
    @Test
    void testSendTaskWithExistingTask() {
        // Create basic task params
        TaskSendParams params = new TaskSendParams();
        String taskId = UUID.randomUUID().toString();
        params.setId(taskId);
        
        // Create a simple message
        Message message = new Message();
        TextPart textPart = new TextPart();
        textPart.setText("First message");
        message.setParts(Collections.singletonList(textPart));
        params.setMessage(message);
        
        // Create initial task
        controller.sendTask(params, null, false);
        
        // Create second message
        Message secondMessage = new Message();
        TextPart secondTextPart = new TextPart();
        secondTextPart.setText("Second message");
        secondMessage.setParts(Collections.singletonList(secondTextPart));
        params.setMessage(secondMessage);
        
        // Send second message
        SendTaskResponse response = controller.sendTask(params, null, false);
        
        // Verify task was updated correctly
        assertNotNull(response);
        assertEquals(taskId, response.getId());
        assertNotNull(response.getResult());
        assertEquals(taskId, response.getResult().getId());
        
        // Verify history exists
        List<Message> history = response.getResult().getHistory();
        assertNotNull(history);
        assertEquals(2, history.size());
    }
    
    @Test
    void testGetTask() {
        // Create and add a task
        String taskId = UUID.randomUUID().toString();
        Task task = new Task();
        task.setId(taskId);
        task.setSessionId("test-session");
        task.setStatus(new TaskStatus(TaskState.SUBMITTED));
        
        // Add messages to history
        Message message1 = new Message();
        TextPart textPart1 = new TextPart();
        textPart1.setText("Message 1");
        message1.setParts(Collections.singletonList(textPart1));
        
        Message message2 = new Message();
        TextPart textPart2 = new TextPart();
        textPart2.setText("Message 2");
        message2.setParts(Collections.singletonList(textPart2));
        
        List<Message> history = new ArrayList<>();
        history.add(message1);
        history.add(message2);
        task.setHistory(history);
        
        // Add task to controller using reflection
        try {
            java.lang.reflect.Field tasksField = DyanamicTaskContoller.class.getDeclaredField("tasks");
            tasksField.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.Map<String, Task> tasks = (java.util.Map<String, Task>) tasksField.get(controller);
            tasks.put(taskId, task);
        } catch (Exception e) {
            fail("Failed to set up test: " + e.getMessage());
        }
        
        // Get task without history
        var response = controller.getTask(taskId, 0);
        
        // Verify
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(taskId, response.getBody().getId());
        
        // Get task with limited history
        var responseWithHistory = controller.getTask(taskId, 1);
        
        // Verify history is limited
        assertNotNull(responseWithHistory);
        assertNotNull(responseWithHistory.getBody());
        assertNotNull(responseWithHistory.getBody().getHistory());
        assertEquals(1, responseWithHistory.getBody().getHistory().size());
        
        // Safe access with proper type checking
        List<Part> parts = responseWithHistory.getBody().getHistory().get(0).getParts();
        assertNotNull(parts);
        assertTrue(!parts.isEmpty());
        Part part = parts.get(0);
        assertTrue(part instanceof TextPart, "Expected TextPart");
        assertEquals("Message 2", ((TextPart)part).getText());
    }
}