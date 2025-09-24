package io.github.vishalmysore.common;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CallBackTypeTest {
    
    @Test
    public void testEnumValues() {
        // Test that the enum has exactly two values
        assertEquals(2, CallBackType.values().length);
        
        // Test the enum values are as expected
        assertEquals(CallBackType.MCP, CallBackType.valueOf("MCP"));
        assertEquals(CallBackType.A2A, CallBackType.valueOf("A2A"));
    }
    
    @Test
    public void testEnumOrdinals() {
        // Test ordinal values
        assertEquals(0, CallBackType.MCP.ordinal());
        assertEquals(1, CallBackType.A2A.ordinal());
    }
    
    @Test
    public void testValueOf() {
        // Test valueOf works correctly
        assertEquals(CallBackType.MCP, CallBackType.valueOf("MCP"));
        assertEquals(CallBackType.A2A, CallBackType.valueOf("A2A"));
        
        // Test valueOf with invalid input
        assertThrows(IllegalArgumentException.class, () -> {
            CallBackType.valueOf("INVALID");
        });
    }
}