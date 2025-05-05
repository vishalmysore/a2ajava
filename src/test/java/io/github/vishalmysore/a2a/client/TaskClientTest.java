package io.github.vishalmysore.a2a.client;

import io.github.vishalmysore.a2a.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskClientTest {

    private TaskClient client;
    private RestTemplate mockRestTemplate;
    private static final String CUSTOM_BASE_URL = "http://custom.api.com/rpc";

    @BeforeEach
    public void setUp() {
        mockRestTemplate = mock(RestTemplate.class);
        client = new TaskClient(CUSTOM_BASE_URL);
    }

    @Test
    public void testTaskClientInitialization() {
        assertNotNull(client, "TaskClient should be initialized successfully");
    }
/* 
    @Test
    public void testSendTask() {
        // Arrange
        String prompt = "Book a flight from New York to Los Angeles";

        // Act
        Task task = client.sendTask(prompt);

        // Assert
        assertNotNull(task, "Task should not be null");
        assertNotNull(task.getId(), "Task ID should not be null");
        assertEquals(TaskState.SUBMITTED, task.getStatus().getState(), "Task should be in SUBMITTED state");
        assertEquals(1, client.getPendingTasks().size(), "Should have one pending task");
        assertEquals(0, client.getCompletedTasks().size(), "Should have no completed tasks");
    }

    @Test
    public void testTaskCompletionFlow() {
        // Arrange
        String prompt = "Check flight status";
        Task task = client.sendTask(prompt);
        String taskId = task.getId();

        // Act & Assert - Initial state
        assertEquals(1, client.getPendingTasks().size(), "Should have one pending task initially");
        assertEquals(0, client.getCompletedTasks().size(), "Should have no completed tasks initially");

        // Simulate task completion by retrieving a completed task
        Task completedTask = new Task();
        completedTask.setId(taskId);
        TaskStatus completedStatus = new TaskStatus(TaskState.COMPLETED);
        completedTask.setStatus(completedStatus);
        
        // Update internal state by retrieving the "completed" task
        client.getTask(taskId, 0);

        // Assert final state
        assertEquals(0, client.getPendingTasks().size(), "Should have no pending tasks after completion");
        assertEquals(1, client.getCompletedTasks().size(), "Should have one completed task");
        assertTrue(client.getCompletedTasks().stream()
                .anyMatch(t -> t.getId().equals(taskId)), 
                "Completed tasks should contain the task ID");
    }

    @Test
    public void testCancelTask() {
        // Arrange
        String prompt = "Book a hotel room";
        Task task = client.sendTask(prompt);
        String taskId = task.getId();
        assertEquals(1, client.getPendingTasks().size(), "Should have one pending task");

        // Act
        boolean cancelled = client.cancelTask(taskId);

        // Assert
        assertTrue(cancelled, "Task should be successfully cancelled");
        assertEquals(0, client.getPendingTasks().size(), "Should have no pending tasks after cancellation");
        assertTrue(client.getPendingTasks().stream()
                .noneMatch(t -> t.getId().equals(taskId)),
                "Pending tasks should not contain the cancelled task");
    }

    @Test
    public void testCustomBaseUrl() {
        TaskClient customClient = new TaskClient(CUSTOM_BASE_URL);
        assertNotNull(customClient, "Client should be initialized with custom URL");
        
        // Test sending task to custom URL
        String prompt = "Test task";
        Task task = customClient.sendTask(prompt);
        assertNotNull(task, "Should create task with custom URL");
    }

    @Test
    public void testGetTaskWithHistory() {
        // Arrange
        String taskId = "test-task-123";
        Integer historyLength = 5;
        Task task = client.sendTask("Initial task");
        
        // Act
        Task retrievedTask = client.getTask(taskId, historyLength);
        
        // Assert
        assertNotNull(retrievedTask, "Retrieved task should not be null");
        // Verify history length was respected - implementation specific assertions
    }

    @Test
    public void testTaskStateTransitions() {
        // Arrange
        Task task = client.sendTask("Test state transitions");
        String taskId = task.getId();
        
        // Assert initial state
        assertEquals(TaskState.SUBMITTED, task.getStatus().getState());
        
        // Simulate processing state
        task.setStatus(new TaskStatus(TaskState.SUBMITTED));
        assertEquals(TaskState.SUBMITTED, task.getStatus().getState());
        
        // Simulate completion
        task.setStatus(new TaskStatus(TaskState.COMPLETED));
        assertEquals(TaskState.COMPLETED, task.getStatus().getState());
    }

    @Test
    public void testErrorHandling() {
        // Test server error
        when(mockRestTemplate.postForEntity(
            eq(CUSTOM_BASE_URL),
            any(),
            eq(Task.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(HttpClientErrorException.class, () -> {
            client.sendTask("Error test");
        });

        // Test invalid task ID
        assertThrows(HttpClientErrorException.class, () -> {
            client.getTask("invalid-id", 0);
        });
    }

    @Test
    public void testTaskListManagement() {
        // Test empty lists initially
        assertTrue(client.getPendingTasks().isEmpty(), "Pending tasks should be empty initially");
        assertTrue(client.getCompletedTasks().isEmpty(), "Completed tasks should be empty initially");
        
        // Add tasks and verify management
        Task task1 = client.sendTask("Task 1");
        Task task2 = client.sendTask("Task 2");
        
        assertEquals(2, client.getPendingTasks().size(), "Should have two pending tasks");
        
        // Complete one task
        task1.setStatus(new TaskStatus(TaskState.COMPLETED));
        client.getTask(task1.getId(), 0); // This should move task1 to completed
        
        assertEquals(1, client.getPendingTasks().size(), "Should have one pending task");
        assertEquals(1, client.getCompletedTasks().size(), "Should have one completed task");
    }

    @Test
    public void testConcurrentTaskOperations() {
        // Test thread safety of task lists
        Task[] tasks = new Task[5];
        Thread[] threads = new Thread[5];
        
        for (int i = 0; i < 5; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                tasks[index] = client.sendTask("Concurrent task " + index);
            });
            threads[i].start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                fail("Thread interrupted");
            }
        }
        
        assertEquals(5, client.getPendingTasks().size(), "Should have all tasks in pending list");
        
        // Verify no duplicate tasks
        assertEquals(5, client.getPendingTasks().stream().map(Task::getId).distinct().count(),
                "Should have no duplicate tasks");
    }
                */
}