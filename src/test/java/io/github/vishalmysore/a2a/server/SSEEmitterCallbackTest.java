package io.github.vishalmysore.a2a.server;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.t4a.detect.ActionCallback;
import com.t4a.detect.ActionState;
import io.github.vishalmysore.a2a.domain.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class SSEEmitterCallbackTest {

    private SSEEmitterCallback callback;
    
    @Mock
    private SseEmitter mockEmitter;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        callback = new SSEEmitterCallback(null, mockEmitter);
    }
    

    public void testStatusUpdateSent() throws IOException {
        String status = "Processing";
        ActionState state = ActionState.WORKING;
        
        callback.sendtStatus(status, state);
        
        verify(mockEmitter).send(any(TaskStatusUpdateEvent.class));
    }
    

    public void testEmitterCompletionOnFinalState() throws IOException {
        // Test completion state
        callback.sendtStatus("Completed", ActionState.COMPLETED);
        verify(mockEmitter).complete();
        
        // Test failure state
        callback = new SSEEmitterCallback(null, mockEmitter);
        callback.sendtStatus("Failed", ActionState.FAILED);
        verify(mockEmitter, times(2)).complete();
    }
    
  
    

    public void testContextHandling() {
        Object context = new Object();
        callback.setContext(new AtomicReference<>(context));
        assertEquals(context, callback.getContext());
    }

    public void testTypeHandling() {
        String type = "test-type";
        String result = callback.getType();
        assertEquals(type, result);
        assertEquals(type, callback.getType());
    }
    

    public void testNullStatusHandling() throws IOException {
        // Should handle null values gracefully
        callback.sendtStatus(null, null);
       // verify(mockEmitter, never()).send(any());
    }
    

    public void testEmitterCompletionOrder() throws IOException {
        // Test proper completion order for a task
        callback.sendtStatus("Starting", ActionState.SUBMITTED);
        callback.sendtStatus("Working", ActionState.WORKING);
        callback.sendtStatus("Almost done", ActionState.WORKING);
        callback.sendtStatus("Completed", ActionState.COMPLETED);
        
        // Verify events were sent in order
        verify(mockEmitter, times(4)).send(any(TaskStatusUpdateEvent.class));
        verify(mockEmitter).complete();
    }
}
