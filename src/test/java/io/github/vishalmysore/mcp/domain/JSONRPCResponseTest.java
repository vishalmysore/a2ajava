package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JSONRPCResponseTest {

    @Test
    public void testConstructor() {
        Result result = new Result() {};
        JSONRPCResponse response = new JSONRPCResponse("123", result);
        
        assertEquals("123", response.getId());
        assertEquals("2.0", response.getJsonrpc());
        assertEquals(result, response.getResult());
    }
    
    @Test
    public void testDefaultConstructor() {
        JSONRPCResponse response = new JSONRPCResponse();
        
        assertEquals("2.0", response.getJsonrpc()); // jsonrpc is final and always 2.0
    }
    
    @Test
    public void testGetSetId() {
        JSONRPCResponse response = new JSONRPCResponse();
        response.setId("test-id");
        
        assertEquals("test-id", response.getId());
    }
    
    @Test
    public void testGetSetResult() {
        JSONRPCResponse response = new JSONRPCResponse();
        Result result = new Result() {};
        response.setResult(result);
        
        assertEquals(result, response.getResult());
    }
    
    @Test
    public void testEquals() {
        // Create a shared result object to avoid the issue with different anonymous classes
        Result sharedResult = new Result() {};
        
        JSONRPCResponse response1 = new JSONRPCResponse("123", sharedResult);
        JSONRPCResponse response2 = new JSONRPCResponse("123", sharedResult);
        JSONRPCResponse response3 = new JSONRPCResponse("456", sharedResult);
        
        assertEquals(response1, response1); // Same instance
        assertEquals(response1, response2); // Equal properties
        assertNotEquals(response1, response3); // Different id
        assertNotEquals(response1, null); // Different type
        assertNotEquals(response1, "not a response"); // Different class
    }
    
    @Test
    public void testHashCode() {
        // Create a shared result object to avoid the issue with different anonymous classes
        Result sharedResult = new Result() {};
        
        JSONRPCResponse response1 = new JSONRPCResponse("123", sharedResult);
        JSONRPCResponse response2 = new JSONRPCResponse("123", sharedResult);
        JSONRPCResponse response3 = new JSONRPCResponse("456", sharedResult);
        
        assertEquals(response1.hashCode(), response2.hashCode()); // Equal properties, equal hash
        assertNotEquals(response1.hashCode(), response3.hashCode()); // Different properties, different hash
    }
    
    @Test
    public void testToString() {
        Result result = new Result() {};
        JSONRPCResponse response = new JSONRPCResponse("123", result);
        
        String toString = response.toString();
        
        // Verify toString contains important fields
        assertTrue(toString.contains("id=123"));
        assertTrue(toString.contains("jsonrpc=2.0"));
        assertTrue(toString.contains("result="));
    }
}