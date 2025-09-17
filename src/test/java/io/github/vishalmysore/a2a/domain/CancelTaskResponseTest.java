package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for CancelTaskResponse domain object
 */
public class CancelTaskResponseTest {

    @Test
    void testDefaultConstructor() {
        CancelTaskResponse response = new CancelTaskResponse();
        
        assertEquals("2.0", response.getJsonrpc());
        assertNull(response.getId());
        assertNull(response.getResult());
        assertNull(response.getError());
    }

    @Test
    void testConstructorWithResult() {
        String id = "request-123";
        Task task = new Task();
        task.setId("task-123");
        
        CancelTaskResponse response = new CancelTaskResponse(id, task);
        
        assertEquals("2.0", response.getJsonrpc());
        assertEquals(id, response.getId());
        assertEquals(task, response.getResult());
        assertNull(response.getError());
    }

    @Test
    void testConstructorWithError() {
        String id = "request-123";
        JSONRPCError error = new JSONRPCError(500, "Internal Server Error");
        
        CancelTaskResponse response = new CancelTaskResponse(id, error);
        
        assertEquals("2.0", response.getJsonrpc());
        assertEquals(id, response.getId());
        assertNull(response.getResult());
        assertEquals(error, response.getError());
    }

    @Test
    void testSettersAndGetters() {
        CancelTaskResponse response = new CancelTaskResponse();
        
        // Test ID
        String id = "request-123";
        response.setId(id);
        assertEquals(id, response.getId());
        
        // Test Result
        Task task = new Task();
        task.setId("task-123");
        response.setResult(task);
        assertEquals(task, response.getResult());
        
        // Test Error
        JSONRPCError error = new JSONRPCError(400, "Bad Request");
        response.setError(error);
        assertEquals(error, response.getError());
    }

    @Test
    void testJsonRpcVersion() {
        CancelTaskResponse response = new CancelTaskResponse();
        assertEquals("2.0", response.getJsonrpc());
        
        // Make sure jsonrpc is always 2.0 and can't be changed (final field)
        // This is more of a compilation check, but the assertion confirms the value
        assertEquals("2.0", response.getJsonrpc());
    }

    @Test
    void testToString() {
        String id = "request-123";
        Task task = new Task();
        task.setId("task-123");
        
        CancelTaskResponse response = new CancelTaskResponse(id, task);
        String toStringResult = response.toString();
        
        assertTrue(toStringResult.contains("jsonrpc=2.0"));
        assertTrue(toStringResult.contains("id=request-123"));
        assertTrue(toStringResult.contains("result="));
        assertTrue(toStringResult.contains("task-123"));
    }

    @Test
    void testEquals() {
        String id = "request-123";
        Task task = new Task();
        task.setId("task-123");
        
        CancelTaskResponse response1 = new CancelTaskResponse(id, task);
        CancelTaskResponse response2 = new CancelTaskResponse(id, task);
        CancelTaskResponse response3 = new CancelTaskResponse("different-id", task);
        
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
    }

    @Test
    void testHashCode() {
        String id = "request-123";
        Task task = new Task();
        task.setId("task-123");
        
        CancelTaskResponse response1 = new CancelTaskResponse(id, task);
        CancelTaskResponse response2 = new CancelTaskResponse(id, task);
        
        assertEquals(response1.hashCode(), response2.hashCode());
    }
}