package io.github.vishalmysore.debug;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import com.t4a.processor.AIProcessingException;

public class DebugCurlCommandsTest {

    @Test
    public void testGetCurlCommandsReturnsNonNullMap() throws AIProcessingException {
        // Arrange
        String[] args = new String[]{};

        // Act
        Map<String, String> result = DebugCurlCommands.getCurlCommands(args);

        // Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testGetCurlCommandsContainsAgentCard() throws AIProcessingException {
        // Arrange
        String[] args = new String[]{};

        // Act
        Map<String, String> result = DebugCurlCommands.getCurlCommands(args);

        // Assert
        assertTrue(result.containsKey("agent-Card"));
        assertNotNull(result.get("agent-Card"));
    }

    @Test
    public void testGetCurlCommandsContainsToolList() throws AIProcessingException {
        // Arrange
        String[] args = new String[]{};

        // Act
        Map<String, String> result = DebugCurlCommands.getCurlCommands(args);

        // Assert
        assertTrue(result.containsKey("tool-list"));
        assertNotNull(result.get("tool-list"));
    }
}