package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ToolTest {

    @Test
    public void testToolInitialization() {
        Tool tool = new Tool();
        assertNotNull(tool, "Tool should be initialized successfully");
    }

    // Additional tests for Tool methods can be added here
}