package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class TaskSendSubscribeParamsTest {

    @Test
    public void testTaskSendSubscribeParamsConstructorAndGetters() {
        TaskSendSubscribeParams params = new TaskSendSubscribeParams();
        
        // Test initial state
        assertNull(params.getId());
        assertNull(params.getSessionId());
        assertNull(params.getMessage());
        assertNull(params.getAcceptedOutputModes());
        assertNull(params.getPushNotification());
        assertNull(params.getHistoryLength());
        assertNull(params.getMetadata());
    }

    @Test
    public void testTaskSendSubscribeParamsSetters() {
        TaskSendSubscribeParams params = new TaskSendSubscribeParams();
        
        // Setup test data
        String id = "test-id-123";
        String sessionId = "session-456";
        Message message = new Message();
        message.setRole("user");
        TextPart textPart = new TextPart();
        textPart.setText("Hello, this is a test message");
        message.setParts(java.util.Arrays.asList(textPart));
        
        List<String> acceptedModes = Arrays.asList("text", "json", "html");
        Object pushNotification = new Object();
        Integer historyLength = 5;
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("source", "test");
        metadata.put("priority", "high");
        
        // Set properties
        params.setId(id);
        params.setSessionId(sessionId);
        params.setMessage(message);
        params.setAcceptedOutputModes(acceptedModes);
        params.setPushNotification(pushNotification);
        params.setHistoryLength(historyLength);
        params.setMetadata(metadata);
        
        // Verify properties were set correctly
        assertEquals(id, params.getId());
        assertEquals(sessionId, params.getSessionId());
        assertEquals(message, params.getMessage());
        assertEquals(acceptedModes, params.getAcceptedOutputModes());
        assertEquals(pushNotification, params.getPushNotification());
        assertEquals(historyLength, params.getHistoryLength());
        assertEquals(metadata, params.getMetadata());
    }

    @Test
    public void testEqualsAndHashCode() {
        TaskSendSubscribeParams params1 = new TaskSendSubscribeParams();
        params1.setId("id1");
        params1.setSessionId("session1");
        
        TaskSendSubscribeParams params2 = new TaskSendSubscribeParams();
        params2.setId("id1");
        params2.setSessionId("session1");
        
        TaskSendSubscribeParams params3 = new TaskSendSubscribeParams();
        params3.setId("id2");
        params3.setSessionId("session2");
        
        // Test equals
        assertEquals(params1, params2);
        assertNotEquals(params1, params3);
        
        // Test hashCode
        assertEquals(params1.hashCode(), params2.hashCode());
        assertNotEquals(params1.hashCode(), params3.hashCode());
    }

    @Test
    public void testToString() {
        TaskSendSubscribeParams params = new TaskSendSubscribeParams();
        params.setId("test-id");
        params.setSessionId("test-session");
        
        String toString = params.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("test-id"));
        assertTrue(toString.contains("test-session"));
    }
}