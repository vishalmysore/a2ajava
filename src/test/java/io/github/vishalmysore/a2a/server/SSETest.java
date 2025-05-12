package io.github.vishalmysore.a2a.server;

import io.github.vishalmysore.a2a.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SSETest {

    private DyanamicTaskContoller controller;
    
    @Mock
    private SseEmitter mockEmitter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new DyanamicTaskContoller();
    }


    public void testSSESubscription() throws InterruptedException {
        // Create a task first
        String taskId = UUID.randomUUID().toString();
        TaskSendSubscribeParams params = new TaskSendSubscribeParams();
        params.setId(taskId);
        
        Message message = new Message();
        TextPart textPart = new TextPart();
        textPart.setType("text");
        textPart.setText("Test SSE subscription");
        message.setParts(List.of(textPart));
        params.setMessage(message);

        // Create latch to wait for events
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> receivedEvent = new AtomicReference<>();

        // Get SSE emitter
        SseEmitter emitter = controller.sendSubscribeTask(params);
        assertNotNull(emitter);

        // Wait for completion
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }


    public void testSSEResubscription() {
        // Create initial task
        String taskId = UUID.randomUUID().toString();
        TaskSendParams params = new TaskSendParams();
        params.setId(taskId);
        
        Message message = new Message();
        TextPart textPart = new TextPart();
        textPart.setType("text");
        textPart.setText("Test resubscription");
        message.setParts(List.of(textPart));
        params.setMessage(message);

        controller.sendTask(params, null);

        // Test resubscription
        SseEmitter emitter = controller.resubscribe(taskId);
        assertNotNull(emitter);
    }


    public void testSSEErrorHandling() throws IOException {
        // Test subscription to non-existent task
        String nonExistentId = UUID.randomUUID().toString();
        SseEmitter emitter = controller.resubscribe(nonExistentId);
        
        // Emitter should be created but complete quickly with error
        assertNotNull(emitter);

        // Test timeout handling
        String taskId = UUID.randomUUID().toString();
        TaskSendSubscribeParams params = new TaskSendSubscribeParams();
        params.setId(taskId);
        
        Message message = new Message();
        TextPart textPart = new TextPart();
        textPart.setType("text");
        textPart.setText("Test timeout");
        message.setParts(List.of(textPart));
        params.setMessage(message);

        SseEmitter timeoutEmitter = controller.sendSubscribeTask(params);
        assertNotNull(timeoutEmitter);
    }


    public void testSSEMultipleSubscribers() {
        // Create a task
        String taskId = UUID.randomUUID().toString();
        TaskSendParams params = new TaskSendParams();
        params.setId(taskId);
        
        Message message = new Message();
        TextPart textPart = new TextPart();
        textPart.setType("text");
        textPart.setText("Test multiple subscribers");
        message.setParts(List.of(textPart));
        params.setMessage(message);

        controller.sendTask(params, null);

        // Create multiple subscribers
        SseEmitter emitter1 = controller.resubscribe(taskId);
        SseEmitter emitter2 = controller.resubscribe(taskId);
        SseEmitter emitter3 = controller.resubscribe(taskId);

        assertNotNull(emitter1);
        assertNotNull(emitter2);
        assertNotNull(emitter3);
    }
}
