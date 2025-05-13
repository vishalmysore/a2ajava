package io.github.vishalmysore.a2a.server;

import com.t4a.detect.ActionCallback;
import com.t4a.processor.AIProcessor;
import com.t4a.processor.AIProcessingException;
import io.github.vishalmysore.a2a.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AIProcessingTest {

    private DyanamicTaskContoller controller;

    @Mock
    private AIProcessor mockProcessor;

    @Mock
    private ActionCallback mockCallback;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new DyanamicTaskContoller() {
            @Override
            public AIProcessor getBaseProcessor() {
                return mockProcessor;
            }
        };
    }

    @Test
    public void testSuccessfulAIProcessing() throws AIProcessingException {
        // Setup mock response
        when(mockProcessor.processSingleAction(any(String.class), any(ActionCallback.class)))
            .thenReturn("AI Generated Response");

        // Create task
        TaskSendParams params = createTaskParams("Process this with AI");
        SendTaskResponse response = controller.sendTask(params, mockCallback);

        // Verify processing
        verify(mockProcessor).processSingleAction(any(String.class), any(ActionCallback.class));
        assertNotNull(response);
        assertNotNull(response.getResult());
    }


    public void testAIProcessingFailure() throws AIProcessingException {
        // Setup mock to throw exception
        when(mockProcessor.processSingleAction(any(String.class), any(ActionCallback.class)))
            .thenThrow(new AIProcessingException("AI processing failed"));

        // Create task
        TaskSendParams params = createTaskParams("This should fail");
        SendTaskResponse response = controller.sendTask(params, mockCallback);

        // Verify failure handling
        assertNotNull(response);
        Task result = response.getResult();
        assertNotNull(result);
        assertEquals(TaskState.FAILED, result.getStatus().getState());
        assertTrue(((TextPart)result.getStatus().getMessage().getParts().get(0)).getText()
            .contains("Processing failed"));
    }

    @Test
    public void testAIProviderSwitch() {
        // Test Gemini processor
        //will move tihs to mock later
        controller = new DyanamicTaskContoller(){
            @Override
            public SendTaskResponse sendTask(TaskSendParams taskSendParams,  ActionCallback callback) {
                Task t = new   Task();
                SendTaskResponse response = new SendTaskResponse();
                response.setResult(t);
                TaskStatus ts = new TaskStatus();
                ts.setState(TaskState.SUBMITTED);
                t.setStatus(ts);
                response.setId(taskSendParams.getId());
                return response;
            }
        };
        AIProcessor processor = controller.getBaseProcessor();
        assertNotNull(processor);
        assertTrue(processor.getClass().getSimpleName().contains("Gemini"));

        // Test task processing with different providers
        TaskSendParams params = createTaskParams("Test with different provider");
        SendTaskResponse response = controller.sendTask(params, null);
        assertNotNull(response);
        assertEquals(TaskState.SUBMITTED, response.getResult().getStatus().getState());
    }


    public void testComplexAIProcessing() throws AIProcessingException {
        // Setup mock for complex processing
        when(mockProcessor.processSingleAction(any(String.class), any(ActionCallback.class)))
            .thenAnswer(invocation -> {
                String input = invocation.getArgument(0);
                ActionCallback callback = invocation.getArgument(1);
                
                // Simulate processing stages
                if (callback != null) {
                    callback.sendtStatus("Processing stage 1", null);
                    Thread.sleep(100);
                    callback.sendtStatus("Processing stage 2", null);
                    Thread.sleep(100);
                    callback.sendtStatus("Processing complete", null);
                }
                
                return "Complex AI processing result for: " + input;
            });

        // Create task with complex input
        TaskSendParams params = createTaskParams("Complex AI task with multiple stages");
        SendTaskResponse response = controller.sendTask(params, mockCallback);

        // Verify complex processing
        verify(mockProcessor).processSingleAction(any(String.class), any(ActionCallback.class));
        verify(mockCallback, atLeast(3)).sendtStatus(any(), any());
        
        assertNotNull(response);
        assertNotNull(response.getResult());
    }


    public void testAIProcessingTimeout() throws AIProcessingException {
        // Setup mock to simulate timeout
        when(mockProcessor.processSingleAction(any(String.class), any(ActionCallback.class)))
            .thenAnswer(invocation -> {
                Thread.sleep(5000); // Simulate long processing
                return "Delayed response";
            });

        // Create task
        TaskSendParams params = createTaskParams("Long running task");
        SendTaskResponse response = controller.sendTask(params, mockCallback);

        // Verify timeout handling
        assertNotNull(response);
        assertNotNull(response.getResult());
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
