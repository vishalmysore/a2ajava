package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ToolParameterTest {

    private ToolParameter toolParameter;

    @BeforeEach
    public void setUp() {
        toolParameter = new ToolParameter();
    }

    @Test
    public void testToolParameterInitialization() {
        assertNotNull(toolParameter, "ToolParameter should be initialized successfully");
        assertNull(toolParameter.getType(), "Type should be null initially");
        assertNull(toolParameter.getDescription(), "Description should be null initially");
    }

    @Test
    public void testSetAndGetType() {
        String type = "string";
        toolParameter.setType(type);
        assertEquals(type, toolParameter.getType(), "Type should be set and retrieved correctly");
    }

    @Test
    public void testSetAndGetDescription() {
        String description = "Test parameter description";
        toolParameter.setDescription(description);
        assertEquals(description, toolParameter.getDescription(), "Description should be set and retrieved correctly");
    }

    @Test
    public void testToString() {
        toolParameter.setType("integer");
        toolParameter.setDescription("A number parameter");
        
        String result = toolParameter.toString();
        
        assertTrue(result.contains("integer"), "toString should contain the type");
        assertTrue(result.contains("A number parameter"), "toString should contain the description");
    }

    @Test
    public void testEqualsAndHashCode() {
        ToolParameter param1 = new ToolParameter();
        param1.setType("string");
        param1.setDescription("Test description");
        
        ToolParameter param2 = new ToolParameter();
        param2.setType("string");
        param2.setDescription("Test description");
        
        ToolParameter param3 = new ToolParameter();
        param3.setType("number");
        param3.setDescription("Different description");
        
        assertEquals(param1, param2, "Parameters with same properties should be equal");
        assertNotEquals(param1, param3, "Parameters with different properties should not be equal");
        assertEquals(param1.hashCode(), param2.hashCode(), "Hash codes should be equal for equal parameters");
    }

    @Test
    public void testSpecialCharactersInDescription() {
        String description = "Parameter with special characters: !@#$%^&*()_+";
        toolParameter.setDescription(description);
        assertEquals(description, toolParameter.getDescription(), "Description with special characters should be handled correctly");
    }
    
    @Test
    public void testNonStringTypes() {
        String[] types = {"string", "number", "integer", "boolean", "object", "array"};
        
        for (String type : types) {
            toolParameter.setType(type);
            assertEquals(type, toolParameter.getType(), "Type " + type + " should be set and retrieved correctly");
        }
    }
}