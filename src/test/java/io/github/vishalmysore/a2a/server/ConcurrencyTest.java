package io.github.vishalmysore.a2a.server;

import io.github.vishalmysore.a2a.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConcurrencyTest {

    private DyanamicTaskContoller controller;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new DyanamicTaskContoller();
    }

    @Test
    public void testConcurrentTaskSubmission() throws InterruptedException {
        int numThreads = 10;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(numThreads);
        List<Future<SendTaskResponse>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        try {
            // Submit tasks concurrently
            for (int i = 0; i < numThreads; i++) {
                final int taskNum = i;
                Future<SendTaskResponse> future = executorService.submit(() -> {
                    startLatch.await(); // Wait for all threads to be ready
                    TaskSendParams params = createTaskParams("Concurrent task " + taskNum);
                    SendTaskResponse response = controller.sendTask(params, null);
                    completionLatch.countDown();
                    return response;
                });
                futures.add(future);
            }

            // Start all threads simultaneously
            startLatch.countDown();

            // Wait for all tasks to complete
            assertTrue(completionLatch.await(10, TimeUnit.SECONDS));            // Verify results
            List<SendTaskResponse> responses = new ArrayList<>();
            for (Future<SendTaskResponse> future : futures) {
                try {
                    responses.add(future.get(5, TimeUnit.SECONDS));
                } catch (Exception e) {
                    fail("Task execution failed: " + e.getMessage());
                }
            }

            assertEquals(numThreads, responses.size());
            assertEquals(numThreads, responses.stream().map(SendTaskResponse::getId).distinct().count());
        } finally {
            executorService.shutdown();
        }
    }


    public void testConcurrentTaskUpdates() throws InterruptedException {
        // Create initial task
        TaskSendParams params = createTaskParams("Task for concurrent updates");
        SendTaskResponse initialResponse = controller.sendTask(params, null);
        String taskId = initialResponse.getId();

        int numThreads = 5;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        try {
            // Update task concurrently
            for (int i = 0; i < numThreads; i++) {
                final int updateNum = i;
                executorService.submit(() -> {
                    try {
                        startLatch.await();                        params.setId(taskId);
                        ((TextPart)params.getMessage().getParts().get(0)).setText("Update " + updateNum);
                        controller.sendTask(params, null);
                        completionLatch.countDown();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            // Start all threads simultaneously
            startLatch.countDown();

            // Wait for all updates to complete
            assertTrue(completionLatch.await(10, TimeUnit.SECONDS));

            // Verify task history
            Task updatedTask = controller.getTask(taskId, numThreads + 1).getBody();
            assertNotNull(updatedTask);
            assertEquals(numThreads + 1, updatedTask.getHistory().size());
        } finally {
            executorService.shutdown();
        }
    }


    public void testConcurrentTaskCancellation() throws InterruptedException {
        // Create tasks to cancel
        int numTasks = 5;
        List<String> taskIds = new ArrayList<>();
        for (int i = 0; i < numTasks; i++) {
            TaskSendParams params = createTaskParams("Task " + i + " for cancellation");
            SendTaskResponse response = controller.sendTask(params, null);
            taskIds.add(response.getId());
        }

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(numTasks);
        ExecutorService executorService = Executors.newFixedThreadPool(numTasks);

        try {
            // Cancel tasks concurrently
            for (String taskId : taskIds) {
                executorService.submit(() -> {
                    try {
                        startLatch.await();
                        controller.cancelTask(taskId);
                        completionLatch.countDown();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            // Start all threads simultaneously
            startLatch.countDown();

            // Wait for all cancellations to complete
            assertTrue(completionLatch.await(10, TimeUnit.SECONDS));

            // Verify all tasks are cancelled
            for (String taskId : taskIds) {
                Task task = controller.getTask(taskId, 0).getBody();
                assertTrue(task.isCancelled());
            }
        } finally {
            executorService.shutdown();
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
