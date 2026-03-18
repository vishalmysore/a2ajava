package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskCancelParamsTest {

    @Test
    public void testDefaultConstructor() {
        TaskCancelParams params = new TaskCancelParams();
        assertNotNull(params);
        assertNull(params.getId());
    }

    @Test
    public void testSetAndGetId() {
        TaskCancelParams params = new TaskCancelParams();
        params.setId("task-to-cancel-123");

        assertEquals("task-to-cancel-123", params.getId());
    }

    @Test
    public void testToString() {
        TaskCancelParams params = new TaskCancelParams();
        params.setId("cancel-456");

        String toString = params.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("cancel-456"));
    }

    @Test
    public void testEqualsAndHashCode() {
        TaskCancelParams params1 = new TaskCancelParams();
        params1.setId("same-id");

        TaskCancelParams params2 = new TaskCancelParams();
        params2.setId("same-id");

        TaskCancelParams params3 = new TaskCancelParams();
        params3.setId("different-id");

        assertEquals(params1, params2);
        assertEquals(params1.hashCode(), params2.hashCode());
        assertNotEquals(params1, params3);
    }

    @Test
    public void testSetIdToNull() {
        TaskCancelParams params = new TaskCancelParams();
        params.setId("initial-id");
        assertEquals("initial-id", params.getId());

        params.setId(null);
        assertNull(params.getId());
    }
}
