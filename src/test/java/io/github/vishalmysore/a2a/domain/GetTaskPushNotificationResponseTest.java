package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GetTaskPushNotificationResponseTest {

    @Test
    public void testNoArgsConstructor() {
        GetTaskPushNotificationResponse response = new GetTaskPushNotificationResponse();
        assertNotNull(response);
        assertEquals("2.0", response.getJsonrpc());
        assertNull(response.getId());
        assertNull(response.getResult());
    }

    @Test
    public void testConstructorWithIdAndResult() {
        TaskPushNotificationConfig config = new TaskPushNotificationConfig();
        GetTaskPushNotificationResponse response = new GetTaskPushNotificationResponse("123", config);
        assertEquals("2.0", response.getJsonrpc());
        assertEquals("123", response.getId());
        assertEquals(config, response.getResult());
    }

    @Test
    public void testConstructorWithIdOnly() {
        GetTaskPushNotificationResponse response = new GetTaskPushNotificationResponse("456");
        assertEquals("2.0", response.getJsonrpc());
        assertEquals("456", response.getId());
        assertNull(response.getResult());
    }

    @Test
    public void testSettersAndGetters() {
        GetTaskPushNotificationResponse response = new GetTaskPushNotificationResponse();
        response.setId("789");
        TaskPushNotificationConfig config = new TaskPushNotificationConfig();
        response.setResult(config);

        assertEquals("789", response.getId());
        assertEquals(config, response.getResult());
    }

    @Test
    public void testToString() {
        GetTaskPushNotificationResponse response = new GetTaskPushNotificationResponse("id", null);
        String toString = response.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("id"));
    }
}