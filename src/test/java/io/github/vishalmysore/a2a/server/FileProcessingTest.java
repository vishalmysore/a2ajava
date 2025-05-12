package io.github.vishalmysore.a2a.server;

import com.t4a.detect.ActionCallback;
import io.github.vishalmysore.a2a.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class FileProcessingTest {

    private DyanamicTaskContoller controller;
    
    @Mock
    private ActionCallback mockCallback;
    
    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new DyanamicTaskContoller();
    }

    @Test
    public void testFileUploadAndProcessing() throws IOException {
        // Create a test file
        String fileContent = "Test file content\nLine 2\nLine 3";
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, fileContent.getBytes());

        // Create task params with file
        TaskSendParams params = new TaskSendParams();
        params.setId(UUID.randomUUID().toString());

        Message message = new Message();
        FilePart filePart = new FilePart();
        filePart.setType("file");
        
        FileContent fileInfo = new FileContent();
        fileInfo.setBytes(Base64.getEncoder().encodeToString(fileContent.getBytes()));
        filePart.setFile(fileInfo);
        
        message.setParts(List.of(filePart));
        params.setMessage(message);

        // Send task
        SendTaskResponse response = controller.sendTask(params, mockCallback);

        // Verify response
        assertNotNull(response);
        assertEquals(params.getId(), response.getId());
        assertNotNull(response.getResult());
    }

    @Test
    public void testLargeFileProcessing() throws IOException {
        // Create a large test file (1MB)
        StringBuilder largeContent = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            largeContent.append("Line ").append(i).append(" of test content\n");
        }

        Path largeFile = tempDir.resolve("large.txt");
        Files.write(largeFile, largeContent.toString().getBytes());

        // Create task params with large file
        TaskSendParams params = new TaskSendParams();
        params.setId(UUID.randomUUID().toString());

        Message message = new Message();
        FilePart filePart = new FilePart();
        filePart.setType("file");
        
        FileContent fileInfo = new FileContent();
        fileInfo.setBytes(Base64.getEncoder().encodeToString(Files.readAllBytes(largeFile)));
        filePart.setFile(fileInfo);
        
        message.setParts(List.of(filePart));
        params.setMessage(message);

        // Send task
        SendTaskResponse response = controller.sendTask(params, mockCallback);

        // Verify response
        assertNotNull(response);
        assertEquals(params.getId(), response.getId());
    }


    public void testInvalidFileContent() {
        TaskSendParams params = new TaskSendParams();
        params.setId(UUID.randomUUID().toString());

        Message message = new Message();
        FilePart filePart = new FilePart();
        filePart.setType("file");
        
        FileContent fileInfo = new FileContent();
        fileInfo.setBytes("Invalid Base64 Content!");
        filePart.setFile(fileInfo);
        
        message.setParts(List.of(filePart));
        params.setMessage(message);

        SendTaskResponse response = controller.sendTask(params, mockCallback);
        
        // The task should be created but marked as failed
        Task result = response.getResult();
        assertNotNull(result);
        assertEquals(TaskState.FAILED, result.getStatus().getState());
    }
}
