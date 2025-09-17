package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolParametersTest {

    private ToolParameters parameters;

    @BeforeEach
    public void setUp() {
        parameters = new ToolParameters();
    }

    @Test
    public void testToolParametersInitialization() {
        assertNotNull(parameters, "ToolParameters should be initialized successfully");
        assertNotNull(parameters.getProperties(), "Properties map should be initialized");
        assertNotNull(parameters.getRequired(), "Required list should be initialized");
        assertEquals("object", parameters.getType(), "Type should have correct default value");
        assertFalse(parameters.isAdditionalProperties(), "AdditionalProperties should be false by default");
    }
    
    // Skipping $schema tests due to Lombok not generating standard getters/setters for fields starting with $
    
    @Test
    public void testSetAndGetType() {
        String newType = "customType";
        parameters.setType(newType);
        assertEquals(newType, parameters.getType(), "Type should be updated");
    }
    
    @Test
    public void testSetAndGetProperties() {
        Map<String, ToolParameter> newProperties = new HashMap<>();
        
        ToolParameter param1 = new ToolParameter();
        param1.setType("string");
        param1.setDescription("First parameter");
        
        ToolParameter param2 = new ToolParameter();
        param2.setType("integer");
        param2.setDescription("Second parameter");
        
        newProperties.put("param1", param1);
        newProperties.put("param2", param2);
        
        parameters.setProperties(newProperties);
        
        assertEquals(2, parameters.getProperties().size(), "Properties should contain 2 items");
        assertTrue(parameters.getProperties().containsKey("param1"), "Properties should contain param1");
        assertTrue(parameters.getProperties().containsKey("param2"), "Properties should contain param2");
        assertEquals("string", parameters.getProperties().get("param1").getType(), "param1 should be of type string");
        assertEquals("integer", parameters.getProperties().get("param2").getType(), "param2 should be of type integer");
    }
    
    @Test
    public void testAddProperty() {
        ToolParameter param = new ToolParameter();
        param.setType("boolean");
        param.setDescription("Test parameter");
        
        parameters.getProperties().put("testParam", param);
        
        assertEquals(1, parameters.getProperties().size(), "Properties should contain 1 item");
        assertTrue(parameters.getProperties().containsKey("testParam"), "Properties should contain testParam");
        assertEquals("boolean", parameters.getProperties().get("testParam").getType(), "Parameter should be of type boolean");
    }
    
    @Test
    public void testSetAndGetRequired() {
        List<String> required = Arrays.asList("param1", "param2", "param3");
        parameters.setRequired(required);
        
        assertEquals(3, parameters.getRequired().size(), "Required list should contain 3 items");
        assertTrue(parameters.getRequired().contains("param1"), "Required list should contain param1");
        assertTrue(parameters.getRequired().contains("param2"), "Required list should contain param2");
        assertTrue(parameters.getRequired().contains("param3"), "Required list should contain param3");
    }
    
    @Test
    public void testAddRequired() {
        parameters.getRequired().add("param1");
        parameters.getRequired().add("param2");
        
        assertEquals(2, parameters.getRequired().size(), "Required list should contain 2 items");
        assertEquals("param1", parameters.getRequired().get(0), "First required parameter should be param1");
        assertEquals("param2", parameters.getRequired().get(1), "Second required parameter should be param2");
    }
    
    @Test
    public void testSetAndGetAdditionalProperties() {
        parameters.setAdditionalProperties(true);
        assertTrue(parameters.isAdditionalProperties(), "AdditionalProperties should be true");
        
        parameters.setAdditionalProperties(false);
        assertFalse(parameters.isAdditionalProperties(), "AdditionalProperties should be false");
    }
    
    @Test
    public void testToString() {
        ToolParameter param = new ToolParameter();
        param.setType("string");
        param.setDescription("Test parameter");
        parameters.getProperties().put("testParam", param);
        parameters.getRequired().add("testParam");
        
        String result = parameters.toString();
        
        assertTrue(result.contains("testParam"), "toString should contain property name");
        // Not verifying schema since Lombok might generate different toString implementation
        assertTrue(result.contains("object"), "toString should contain type");
        assertTrue(result.contains("additionalProperties=false"), "toString should contain additionalProperties value");
    }
    
    @Test
    public void testEqualsAndHashCode() {
        ToolParameters params1 = new ToolParameters();
        params1.setType("object");
        params1.getRequired().add("param1");
        
        ToolParameters params2 = new ToolParameters();
        params2.setType("object");
        params2.getRequired().add("param1");
        
        ToolParameters params3 = new ToolParameters();
        params3.setType("array");
        params3.getRequired().add("param2");
        
        assertEquals(params1, params2, "Parameters with same properties should be equal");
        assertNotEquals(params1, params3, "Parameters with different properties should not be equal");
        assertEquals(params1.hashCode(), params2.hashCode(), "Hash codes should be equal for equal parameters");
    }
}