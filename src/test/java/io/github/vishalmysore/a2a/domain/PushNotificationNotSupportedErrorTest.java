package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for PushNotificationNotSupportedError domain object
 */
public class PushNotificationNotSupportedErrorTest {

    @Test
    void testDefaultConstructor() {
        PushNotificationNotSupportedError error = new PushNotificationNotSupportedError();
        
        // Check error code is correctly set
        assertEquals(-32003, error.getCode());
        
        // Check error message is correctly set
        assertEquals("Push Notification is not supported", error.getMessage());
        
        // Check data is null
        assertNull(error.getData());
    }

    @Test
    void testInheritance() {
        PushNotificationNotSupportedError error = new PushNotificationNotSupportedError();
        
        assertTrue(error instanceof JSONRPCError, "Should inherit from JSONRPCError");
    }

    @Test
    void testToString() {
        PushNotificationNotSupportedError error = new PushNotificationNotSupportedError();
        String toStringResult = error.toString();
        
        assertTrue(toStringResult.contains("code=-32003"));
        assertTrue(toStringResult.contains("message=Push Notification is not supported"));
    }

    @Test
    void testEquals() {
        PushNotificationNotSupportedError error1 = new PushNotificationNotSupportedError();
        PushNotificationNotSupportedError error2 = new PushNotificationNotSupportedError();
        
        assertEquals(error1, error2, "Two instances should be equal");
    }

    @Test
    void testHashCode() {
        PushNotificationNotSupportedError error1 = new PushNotificationNotSupportedError();
        PushNotificationNotSupportedError error2 = new PushNotificationNotSupportedError();
        
        assertEquals(error1.hashCode(), error2.hashCode(), "Hash codes should match");
    }
}