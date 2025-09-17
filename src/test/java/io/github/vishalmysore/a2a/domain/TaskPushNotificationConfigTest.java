package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TaskPushNotificationConfig domain object
 */
public class TaskPushNotificationConfigTest {

    @Test
    void testDefaultConstructor() {
        TaskPushNotificationConfig config = new TaskPushNotificationConfig();
        
        assertNull(config.getId());
        assertNull(config.getUrl());
        assertNull(config.getToken());
        assertNull(config.getAuthentication());
    }

    @Test
    void testSettersAndGetters() {
        TaskPushNotificationConfig config = new TaskPushNotificationConfig();
        
        // Test ID
        String id = "config-123";
        config.setId(id);
        assertEquals(id, config.getId());
        
        // Test URL
        String url = "https://example.com/webhook";
        config.setUrl(url);
        assertEquals(url, config.getUrl());
        
        // Test token
        String token = "webhook-token-123";
        config.setToken(token);
        assertEquals(token, config.getToken());
        
        // Test authentication
        Authentication auth = new Authentication();
        auth.setBearerAuth("bearer-token-123");
        config.setAuthentication(auth);
        assertEquals(auth, config.getAuthentication());
    }

    @Test
    void testToString() {
        TaskPushNotificationConfig config = new TaskPushNotificationConfig();
        config.setUrl("https://example.com/webhook");
        config.setToken("webhook-token-123");
        
        String toStringResult = config.toString();
        
        // The ID should not be included in toString() because it's marked with @JsonIgnore
        assertTrue(toStringResult.contains("url=https://example.com/webhook"));
        assertTrue(toStringResult.contains("token=webhook-token-123"));
    }

    @Test
    void testEquals() {
        TaskPushNotificationConfig config1 = new TaskPushNotificationConfig();
        config1.setUrl("https://example.com/webhook");
        config1.setToken("webhook-token-123");
        
        TaskPushNotificationConfig config2 = new TaskPushNotificationConfig();
        config2.setUrl("https://example.com/webhook");
        config2.setToken("webhook-token-123");
        
        TaskPushNotificationConfig config3 = new TaskPushNotificationConfig();
        config3.setUrl("https://other.com/webhook");
        config3.setToken("webhook-token-123");
        
        assertEquals(config1, config2);
        assertNotEquals(config1, config3);
    }

    @Test
    void testHashCode() {
        TaskPushNotificationConfig config1 = new TaskPushNotificationConfig();
        config1.setUrl("https://example.com/webhook");
        config1.setToken("webhook-token-123");
        
        TaskPushNotificationConfig config2 = new TaskPushNotificationConfig();
        config2.setUrl("https://example.com/webhook");
        config2.setToken("webhook-token-123");
        
        assertEquals(config1.hashCode(), config2.hashCode());
    }
    
    @Test
    void testWithAuthentication() {
        TaskPushNotificationConfig config = new TaskPushNotificationConfig();
        config.setUrl("https://example.com/webhook");
        
        // Create and add Basic authentication
        Authentication auth = new Authentication();
        auth.setBasicAuth("username", "password");
        config.setAuthentication(auth);
        
        assertEquals(auth, config.getAuthentication());
        assertTrue(config.getAuthentication().isBasicAuth());
    }
}