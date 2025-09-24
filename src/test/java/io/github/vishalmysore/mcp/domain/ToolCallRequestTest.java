package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ToolCallRequestTest {

    @Test
    public void testDefaultConstructor() {
        ToolCallRequest request = new ToolCallRequest();
        
        assertNotNull(request.getArguments());
        assertEquals(0, request.getArguments().size());
    }

    @Test
    public void testGetSetName() {
        ToolCallRequest request = new ToolCallRequest();
        request.setName("testTool");
        
        assertEquals("testTool", request.getName());
    }

    @Test
    public void testGetSetArguments() {
        ToolCallRequest request = new ToolCallRequest();
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("key1", "value1");
        arguments.put("key2", 42);
        
        request.setArguments(arguments);
        
        assertEquals(arguments, request.getArguments());
        assertEquals(2, request.getArguments().size());
        assertEquals("value1", request.getArguments().get("key1"));
        assertEquals(42, request.getArguments().get("key2"));
    }

    @Test
    public void testAddToArguments() {
        ToolCallRequest request = new ToolCallRequest();
        
        request.getArguments().put("key1", "value1");
        request.getArguments().put("key2", 42);
        
        assertEquals(2, request.getArguments().size());
        assertEquals("value1", request.getArguments().get("key1"));
        assertEquals(42, request.getArguments().get("key2"));
    }
    
    @Test
    public void testEquals() {
        ToolCallRequest request1 = new ToolCallRequest();
        request1.setName("testTool");
        request1.getArguments().put("key", "value");
        
        ToolCallRequest request2 = new ToolCallRequest();
        request2.setName("testTool");
        request2.getArguments().put("key", "value");
        
        ToolCallRequest request3 = new ToolCallRequest();
        request3.setName("otherTool");
        request3.getArguments().put("key", "value");
        
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, null);
        assertNotEquals(request1, "not a ToolCallRequest");
    }
    
    @Test
    public void testHashCode() {
        ToolCallRequest request1 = new ToolCallRequest();
        request1.setName("testTool");
        request1.getArguments().put("key", "value");
        
        ToolCallRequest request2 = new ToolCallRequest();
        request2.setName("testTool");
        request2.getArguments().put("key", "value");
        
        assertEquals(request1.hashCode(), request2.hashCode());
    }
    
    @Test
    public void testToString() {
        ToolCallRequest request = new ToolCallRequest();
        request.setName("testTool");
        request.getArguments().put("key", "value");
        
        String toString = request.toString();
        
        assertTrue(toString.contains("name=testTool"));
        assertTrue(toString.contains("arguments={key=value}"));
    }
}