package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SendTaskResponseTest {

    @Test
    public void testDefaultConstructor() {
        SendTaskResponse response = new SendTaskResponse();
        
        assertNull(response.getId());
        assertNull(response.getResult());
        assertNull(response.getError());
        assertEquals("2.0", response.getJsonrpc());
    }
    
    @Test
    public void testSettersAndGetters() {
        SendTaskResponse response = new SendTaskResponse();
        
        String id = "response-123";
        Task task = new Task();
        task.setId("task-456");
        JSONRPCError error = new JSONRPCError();
        error.setCode(100);
        error.setMessage("Test Error");
        
        response.setId(id);
        response.setResult(task);
        response.setError(error);
        
        assertEquals(id, response.getId());
        assertEquals(task, response.getResult());
        assertEquals(error, response.getError());
        assertEquals("2.0", response.getJsonrpc());
    }
    
    @Test
    public void testEqualsAndHashCode() {
        SendTaskResponse response1 = new SendTaskResponse();
        response1.setId("response-123");
        Task task1 = new Task();
        task1.setId("task-456");
        response1.setResult(task1);
        
        SendTaskResponse response2 = new SendTaskResponse();
        response2.setId("response-123");
        Task task2 = new Task();
        task2.setId("task-456");
        response2.setResult(task2);
        
        SendTaskResponse response3 = new SendTaskResponse();
        response3.setId("response-789");
        Task task3 = new Task();
        task3.setId("task-xyz");
        response3.setResult(task3);
        
        // Test equals
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        
        // Test hashCode
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }
    
    @Test
    public void testToString() {
        SendTaskResponse response = new SendTaskResponse();
        response.setId("response-123");
        Task task = new Task();
        task.setId("task-456");
        response.setResult(task);
        
        String toString = response.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("response-123"));
        assertTrue(toString.contains("jsonrpc=2.0"));
    }
}