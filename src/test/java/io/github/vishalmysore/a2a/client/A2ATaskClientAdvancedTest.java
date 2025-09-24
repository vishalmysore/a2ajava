package io.github.vishalmysore.a2a.client;

import io.github.vishalmysore.a2a.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class A2ATaskClientAdvancedTest {

    private A2ATaskClient a2aTaskClient;
    private RestTemplate mockRestTemplate;
    
    @BeforeEach
    void setUp() throws Exception {
        // Create an instance with a test URL
        a2aTaskClient = new A2ATaskClient("http://test-url/rpc");
        
        // Use reflection to replace the RestTemplate with a mock
        mockRestTemplate = mock(RestTemplate.class);
        java.lang.reflect.Field field = A2ATaskClient.class.getDeclaredField("restTemplate");
        field.setAccessible(true);
        field.set(a2aTaskClient, mockRestTemplate);
    }
    
    @Test
    void testSendTask() {
        // Prepare mock response
        SendTaskResponse mockResponse = new SendTaskResponse();
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        TaskStatus taskStatus = new TaskStatus(TaskState.SUBMITTED);
        task.setStatus(taskStatus);
        mockResponse.setResult(task);
        
        when(mockRestTemplate.postForEntity(
                eq("http://test-url/rpc"),
                any(),
                eq(SendTaskResponse.class)
        )).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
        
        // Execute the method
        SendTaskResponse response = a2aTaskClient.sendTask("Test prompt");
        
        // Verify
        assertNotNull(response);
        assertEquals(task.getId(), response.getResult().getId());
        assertEquals(1, a2aTaskClient.getPendingTasks().size());
        assertEquals(0, a2aTaskClient.getCompletedTasks().size());
    }
    
    @Test
    void testSendTaskError() {
        // Mock an error
        when(mockRestTemplate.postForEntity(
                eq("http://test-url/rpc"),
                any(),
                eq(SendTaskResponse.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        
        // Execute and verify
        assertThrows(HttpClientErrorException.class, () -> a2aTaskClient.sendTask("Test prompt"));
    }
    
    @Test
    void testGetTask() {
        // Prepare mock response
        String taskId = UUID.randomUUID().toString();
        Task mockTask = new Task();
        mockTask.setId(taskId);
        TaskStatus taskStatus = new TaskStatus(TaskState.COMPLETED);
        mockTask.setStatus(taskStatus);
        
        when(mockRestTemplate.postForEntity(
                eq("http://test-url/rpc"),
                any(),
                eq(Task.class)
        )).thenReturn(new ResponseEntity<>(mockTask, HttpStatus.OK));
        
        // Add to pending tasks (we need to manually add it since we're testing getTask in isolation)
        Task pendingTask = new Task();
        pendingTask.setId(taskId);
        a2aTaskClient.getPendingTasks().add(pendingTask);
        
        // Execute the method
        Task response = a2aTaskClient.getTask(taskId, 0);
        
        // Verify
        assertNotNull(response);
        assertEquals(taskId, response.getId());
        assertEquals(TaskState.COMPLETED, response.getStatus().getState());
    }
    
    @Test
    void testGetTaskError() {
        // Mock an error
        when(mockRestTemplate.postForEntity(
                eq("http://test-url/rpc"),
                any(),
                eq(Task.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        
        // Execute and verify
        assertThrows(HttpClientErrorException.class, () -> a2aTaskClient.getTask("test-id", 0));
    }
    
    @Test
    void testCancelTask() {
        // Prepare mock response
        String taskId = UUID.randomUUID().toString();
        
        when(mockRestTemplate.postForEntity(
                eq("http://test-url/rpc"),
                any(),
                eq(Object.class)
        )).thenReturn(new ResponseEntity<>(new Object(), HttpStatus.OK));
        
        // Add to pending tasks (we need to manually add it since we're testing cancelTask in isolation)
        Task pendingTask = new Task();
        pendingTask.setId(taskId);
        a2aTaskClient.getPendingTasks().add(pendingTask);
        
        // Execute the method
        boolean result = a2aTaskClient.cancelTask(taskId);
        
        // Verify
        assertTrue(result);
        assertEquals(0, a2aTaskClient.getPendingTasks().size());
    }
    
    @Test
    void testCancelTaskError() {
        // Mock an error
        when(mockRestTemplate.postForEntity(
                eq("http://test-url/rpc"),
                any(),
                eq(Object.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        
        // Execute and verify
        assertThrows(HttpClientErrorException.class, () -> a2aTaskClient.cancelTask("test-id"));
    }
    
    @Test
    void testGetPendingAndCompletedTasks() {
        // Add some tasks
        Task pendingTask = new Task();
        pendingTask.setId(UUID.randomUUID().toString());
        a2aTaskClient.getPendingTasks().add(pendingTask);
        
        Task completedTask = new Task();
        completedTask.setId(UUID.randomUUID().toString());
        a2aTaskClient.getCompletedTasks().add(completedTask);
        
        // Execute and verify
        List<Task> pendingTasks = a2aTaskClient.getPendingTasks();
        List<Task> completedTasks = a2aTaskClient.getCompletedTasks();
        
        assertNotNull(pendingTasks);
        assertNotNull(completedTasks);
        
        // Ensure we get a copy of the lists, not the actual internal lists
        pendingTasks.clear();
        completedTasks.clear();
    }
}