package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ToolInputSchemaTest {

    @Test
    public void testToolInputSchemaInitialization() {
        ToolInputSchema schema = new ToolInputSchema();
        assertNotNull(schema, "ToolInputSchema should be initialized successfully");
    }

    // Additional tests for ToolInputSchema methods can be added here
}