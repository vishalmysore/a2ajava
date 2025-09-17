package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for JSONRPCError domain object
 */
public class JSONRPCErrorTest {

    @Test
    void testDefaultConstructor() {
        JSONRPCError error = new JSONRPCError();
        
        assertEquals(0, error.getCode());
        assertNull(error.getMessage());
        assertNull(error.getData());
    }

    @Test
    void testConstructorWithCodeAndMessage() {
        int code = 404;
        String message = "Not Found";
        
        JSONRPCError error = new JSONRPCError(code, message);
        
        assertEquals(code, error.getCode());
        assertEquals(message, error.getMessage());
        assertNull(error.getData());
    }

    @Test
    void testConstructorWithCodeMessageAndData() {
        int code = 500;
        String message = "Internal Server Error";
        Map<String, Object> data = new HashMap<>();
        data.put("stackTrace", "java.lang.Exception: Something went wrong");
        data.put("timestamp", System.currentTimeMillis());
        
        JSONRPCError error = new JSONRPCError(code, message, data);
        
        assertEquals(code, error.getCode());
        assertEquals(message, error.getMessage());
        assertEquals(data, error.getData());
    }

    @Test
    void testSettersAndGetters() {
        JSONRPCError error = new JSONRPCError();
        
        // Test code
        int code = 400;
        error.setCode(code);
        assertEquals(code, error.getCode());
        
        // Test message
        String message = "Bad Request";
        error.setMessage(message);
        assertEquals(message, error.getMessage());
        
        // Test data
        Map<String, Object> data = new HashMap<>();
        data.put("field", "username");
        data.put("issue", "Required field missing");
        error.setData(data);
        assertEquals(data, error.getData());
    }

    @Test
    void testToString() {
        int code = 403;
        String message = "Forbidden";
        Map<String, Object> data = new HashMap<>();
        data.put("reason", "Insufficient permissions");
        
        JSONRPCError error = new JSONRPCError(code, message, data);
        String toStringResult = error.toString();
        
        assertTrue(toStringResult.contains("code=403"));
        assertTrue(toStringResult.contains("message=Forbidden"));
        assertTrue(toStringResult.contains("data={reason=Insufficient permissions}"));
    }

    @Test
    void testEquals() {
        JSONRPCError error1 = new JSONRPCError(429, "Too Many Requests");
        JSONRPCError error2 = new JSONRPCError(429, "Too Many Requests");
        JSONRPCError error3 = new JSONRPCError(503, "Service Unavailable");
        
        assertEquals(error1, error2);
        assertNotEquals(error1, error3);
    }

    @Test
    void testHashCode() {
        JSONRPCError error1 = new JSONRPCError(429, "Too Many Requests");
        JSONRPCError error2 = new JSONRPCError(429, "Too Many Requests");
        
        assertEquals(error1.hashCode(), error2.hashCode());
    }
}