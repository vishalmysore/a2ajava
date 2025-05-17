package io.github.vishalmysore.mcp.server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class MCPToolsControllerTest {


    public void testMCPToolsControllerInitialization() {
        String openAiKey = System.getProperty("openAiKey");
        Properties properties = new Properties();

        try {
            // First try to load from properties file
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("tools4ai.properties")) {
                if (input != null) {
                    properties.load(input);
                }
            }

            // Check both system property and properties file
            if (openAiKey == null || openAiKey.trim().isEmpty()) {
                openAiKey = properties.getProperty("openAiKey");
            }

            if (openAiKey == null || openAiKey.trim().isEmpty()) {
                // Test for missing key scenario
                Exception exception = assertThrows(RuntimeException.class, () -> {
                    new MCPToolsController();
                });
                assertTrue(exception.getMessage().contains("because the return value of \"com.t4a.predict.PredictionLoader.getOpenAiChatModel()\" is null"));
            } else {
                // Test for valid key scenario
                MCPToolsController controller = new MCPToolsController();
                assertNotNull(controller, "Controller should be initialized with valid key");
            }
        } catch (IOException e) {
            fail("Failed to load properties file: " + e.getMessage());
        }
    }

    // Additional tests for MCPToolsController methods can be added here
}