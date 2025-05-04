package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ToolParametersTest {

    @Test
    public void testToolParametersInitialization() {
        ToolParameters parameters = new ToolParameters();
        assertNotNull(parameters, "ToolParameters should be initialized successfully");
    }

    // Additional tests for ToolParameters methods can be added here
}