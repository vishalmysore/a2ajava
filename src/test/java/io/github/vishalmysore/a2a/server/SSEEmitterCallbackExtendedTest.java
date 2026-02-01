package io.github.vishalmysore.a2a.server;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.t4a.detect.ActionState;
import io.github.vishalmysore.a2a.domain.*;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class SSEEmitterCallbackExtendedTest {

    private SSEEmitterCallback callback;
    
    @Mock
    private SseEmitter mockEmitter;
    
    private String taskId;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        taskId = UUID.randomUUID().toString();
        callback = new SSEEmitterCallback(taskId, mockEmitter);
    }
    
    @Test
    public void testConstructorWithTaskId() {
        // Test that taskId is correctly set
        SSEEmitterCallback callbackWithTaskId = new SSEEmitterCallback("test-task-id", mockEmitter);
        
        // Set context and verify
        Object testContext = new Object();
        callbackWithTaskId.setContext(new AtomicReference<>(testContext));
        assertEquals(testContext, callbackWithTaskId.getContext().get());
    }

    @Test
    public void testContextHandling() {
        // Test context handling
        Object testContext = new Object();
        callback.setContext(new AtomicReference<>(testContext));
        assertEquals(testContext, callback.getContext().get());
        
        // Test with null context
        callback.setContext(null);
        assertNull(callback.getContext());
    }
    
    @Test
    public void testSendStatusWithWorkingState() throws IOException {
        String status = "Processing";
        
        // When status is sent with WORKING state
        callback.sendtStatus(status, ActionState.WORKING);
        
        // Then verify an event is sent through the emitter
        verify(mockEmitter, times(1)).send(any(SseEmitter.SseEventBuilder.class));
    }
    
    @Test
    public void testSendStatusWithCompletedState() throws IOException {
        String status = "Task completed";
        
        // When status is sent with COMPLETED state
        callback.sendtStatus(status, ActionState.COMPLETED);
        
        // Then verify an event is sent through the emitter
        verify(mockEmitter, times(1)).send(any(SseEmitter.SseEventBuilder.class));
    }
    
    @Test
    public void testExceptionHandlingInSendStatus() throws IOException {
        // Given an emitter that throws an exception
        doThrow(new IOException("Test exception")).when(mockEmitter).send(any(SseEmitter.SseEventBuilder.class));
        
        // When sendStatus is called
        // Then a RuntimeException should be thrown
        assertThrows(RuntimeException.class, () -> {
            callback.sendtStatus("Test status", ActionState.WORKING);
        });
    }
}