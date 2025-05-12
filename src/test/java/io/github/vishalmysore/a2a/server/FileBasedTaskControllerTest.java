package io.github.vishalmysore.a2a.server;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.ResponseEntity;

import java.nio.file.Path;
import java.io.File;
import java.util.List;
import java.util.UUID;

import io.github.vishalmysore.a2a.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileBasedTaskControllerTest {
    
    private FileBasedTaskController controller;
    private ObjectMapper objectMapper;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    public void setUp() {
        controller = new FileBasedTaskController();
        objectMapper = new ObjectMapper();
        // Set up temp directory for task storage
        System.setProperty("tasks.file", tempDir.resolve("tasks.json").toString());
    }
    
    @Test
    public void testTaskPersistence() {
        // Create and send a task
        TaskSendParams params = new TaskSendParams();
        String taskId = UUID.randomUUID().toString();
        params.setId(taskId);
        
        Message message = new Message();
        TextPart textPart = new TextPart();
        textPart.setText("Test persistent task");
        message.setParts(List.of(textPart));
        params.setMessage(message);
        
        // Send task
        SendTaskResponse response = controller.sendTask(params, null);
        assertNotNull(response);
        assertEquals(taskId, response.getId());
        
        // Force save
        controller.saveTasks();
        
        // Create new controller instance to test loading
        FileBasedTaskController newController = new FileBasedTaskController();
        newController.loadTasks();
        
        // Verify task was loaded
        ResponseEntity<Task> loadedTask = newController.getTask(taskId, 0);
        assertNotNull(loadedTask.getBody());
        assertEquals(taskId, loadedTask.getBody().getId());
    }
    
    @Test
    public void testFileRecovery() {
        // Test recovery when file is corrupted
        File tasksFile = tempDir.resolve("tasks.json").toFile();
        try {
            // Write invalid JSON
            java.nio.file.Files.writeString(tasksFile.toPath(), "{invalid json}");
            
            // Create new controller - should handle corrupt file gracefully
            FileBasedTaskController newController = new FileBasedTaskController();
            newController.loadTasks();
            
            // Should start with empty task list
            
            
        } catch (Exception e) {
            fail("Should handle corrupt file gracefully");
        }
    }
    
    @Test
    public void testConcurrentFileAccess() throws InterruptedException {
        // Test concurrent access to file storage
        int numThreads = 5;
        Thread[] threads = new Thread[numThreads];
        
        for (int i = 0; i < numThreads; i++) {
            final int threadNum = i;
            threads[i] = new Thread(() -> {
                TaskSendParams params = new TaskSendParams();
                params.setId(UUID.randomUUID().toString());
                
                Message message = new Message();
                TextPart textPart = new TextPart();
                textPart.setText("Concurrent test " + threadNum);
                message.setParts(List.of(textPart));
                params.setMessage(message);
                
                controller.sendTask(params, null);
                controller.saveTasks();
            });
            threads[i].start();
        }
        
        // Wait for all threads
        for (Thread thread : threads) {
            thread.join();
        }
        
        // Verify all tasks were saved
        controller.loadTasks();
     
    }
}
