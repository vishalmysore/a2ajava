package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServerCapabilitiesTest {

    @Test
    public void testServerCapabilitiesInitialization() {
        ServerCapabilities capabilities = new ServerCapabilities();
        assertNotNull(capabilities, "ServerCapabilities should be initialized successfully");
    }

    // Additional tests for ServerCapabilities methods can be added here
}