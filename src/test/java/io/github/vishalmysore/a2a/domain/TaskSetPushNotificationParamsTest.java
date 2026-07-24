package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskSetPushNotificationParamsTest {

    @Test
    public void testDefaultConstructor() {
        TaskSetPushNotificationParams params = new TaskSetPushNotificationParams();
        assertNotNull(params);
        assertNull(params.getTaskId());
        assertNull(params.getNotificationUrl());
        assertNull(params.getPushNotificationUrl());
    }

    @Test
    public void testSetAndGetTaskId() {
        TaskSetPushNotificationParams params = new TaskSetPushNotificationParams();
        params.setTaskId("task-789");

        assertEquals("task-789", params.getTaskId());
    }

    @Test
    public void testSetAndGetNotificationUrl() {
        TaskSetPushNotificationParams params = new TaskSetPushNotificationParams();
        params.setNotificationUrl("https://example.com/notify");

        assertEquals("https://example.com/notify", params.getNotificationUrl());
    }

    @Test
    public void testSetAndGetPushNotificationUrl() {
        TaskSetPushNotificationParams params = new TaskSetPushNotificationParams();
        params.setPushNotificationUrl("https://example.com/push");

        assertEquals("https://example.com/push", params.getPushNotificationUrl());
    }

    @Test
    public void testAllFieldsSet() {
        TaskSetPushNotificationParams params = new TaskSetPushNotificationParams();
        params.setTaskId("task-100");
        params.setNotificationUrl("https://example.com/notify");
        params.setPushNotificationUrl("https://example.com/push");

        assertEquals("task-100", params.getTaskId());
        assertEquals("https://example.com/notify", params.getNotificationUrl());
        assertEquals("https://example.com/push", params.getPushNotificationUrl());
    }

    @Test
    public void testEqualsAndHashCode() {
        TaskSetPushNotificationParams params1 = new TaskSetPushNotificationParams();
        params1.setTaskId("task-1");
        params1.setNotificationUrl("https://example.com/a");
        params1.setPushNotificationUrl("https://example.com/b");

        TaskSetPushNotificationParams params2 = new TaskSetPushNotificationParams();
        params2.setTaskId("task-1");
        params2.setNotificationUrl("https://example.com/a");
        params2.setPushNotificationUrl("https://example.com/b");

        assertEquals(params1, params2);
        assertEquals(params1.hashCode(), params2.hashCode());
    }

    @Test
    public void testToString() {
        TaskSetPushNotificationParams params = new TaskSetPushNotificationParams();
        params.setTaskId("task-200");
        params.setNotificationUrl("https://example.com/notify");

        String toString = params.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("task-200"));
        assertTrue(toString.contains("https://example.com/notify"));
    }
}
