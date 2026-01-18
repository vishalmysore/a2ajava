package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SendTaskStreamingResponseTest {

    @Test
    public void testNoArgsConstructor() {
        SendTaskStreamingResponse response = new SendTaskStreamingResponse();
        assertNotNull(response);
        assertEquals("2.0", response.getJsonrpc());
        assertNull(response.getId());
        assertNull(response.getResult());
    }

    @Test
    public void testConstructorWithIdAndResult() {
        TaskStatusUpdateEvent event = new TaskStatusUpdateEvent();
        SendTaskStreamingResponse response = new SendTaskStreamingResponse("123", event);
        assertEquals("2.0", response.getJsonrpc());
        assertEquals("123", response.getId());
        assertEquals(event, response.getResult());
    }

    @Test
    public void testSettersAndGetters() {
        SendTaskStreamingResponse response = new SendTaskStreamingResponse();
        response.setJsonrpc("2.1");
        response.setId(456);
        response.setResult("test");

        assertEquals("2.1", response.getJsonrpc());
        assertEquals(456, response.getId());
        assertEquals("test", response.getResult());
    }

    @Test
    public void testToString() {
        SendTaskStreamingResponse response = new SendTaskStreamingResponse("id", "result");
        String toString = response.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("id"));
        assertTrue(toString.contains("result"));
    }

    @Test
    public void testEqualsAndHashCode() {
        SendTaskStreamingResponse r1 = new SendTaskStreamingResponse("id", "result");
        SendTaskStreamingResponse r2 = new SendTaskStreamingResponse("id", "result");
        SendTaskStreamingResponse r3 = new SendTaskStreamingResponse("different", "result");

        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertNotEquals(r1.hashCode(), r3.hashCode());
    }
}