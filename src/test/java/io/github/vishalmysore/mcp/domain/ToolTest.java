package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ToolTest {

    private Tool tool;

    @BeforeEach
    public void setUp() {
        tool = new Tool();
    }

    @Test
    public void testToolInitialization() {
        assertNotNull(tool, "Tool should be initialized successfully");
    }

    @Test
    public void testSetAndGetName() {
        String name = "testToolName";
        tool.setName(name);
        assertEquals(name, tool.getName(), "Tool name should be set and retrieved correctly");
    }

    @Test
    public void testSetAndGetDescription() {
        String description = "This is a test description for the tool";
        tool.setDescription(description);
        assertEquals(description, tool.getDescription(), "Tool description should be set and retrieved correctly");
    }

    @Test
    public void testSetAndGetType() {
        String type = "function";
        tool.setType(type);
        assertEquals(type, tool.getType(), "Tool type should be set and retrieved correctly");
    }

    @Test
    public void testSetAndGetParameters() {
        ToolParameters parameters = new ToolParameters();
        parameters.setType("object");
        
        ToolParameter param = new ToolParameter();
        param.setType("string");
        param.setDescription("Parameter description");
        
        parameters.getProperties().put("testParam", param);
        parameters.getRequired().add("testParam");
        
        tool.setParameters(parameters);
        
        assertNotNull(tool.getParameters(), "Tool parameters should not be null");
        assertEquals("object", tool.getParameters().getType(), "Tool parameters type should match");
        assertEquals(1, tool.getParameters().getProperties().size(), "Tool should have one parameter");
        assertTrue(tool.getParameters().getProperties().containsKey("testParam"), "Tool should contain the test parameter");
        assertEquals(1, tool.getParameters().getRequired().size(), "Tool should have one required parameter");
        assertEquals("testParam", tool.getParameters().getRequired().get(0), "Required parameter should be testParam");
    }

    @Test
    public void testSetAndGetInputSchema() {
        ToolInputSchema inputSchema = new ToolInputSchema();
        
        ToolPropertySchema propSchema = new ToolPropertySchema();
        propSchema.setType("string");
        propSchema.setDescription("Property description");
        
        Map<String, ToolPropertySchema> properties = new HashMap<>();
        properties.put("testProp", propSchema);
        inputSchema.setProperties(properties);
        inputSchema.setRequired(Arrays.asList("testProp"));
        
        tool.setInputSchema(inputSchema);
        
        assertNotNull(tool.getInputSchema(), "Tool input schema should not be null");
        assertEquals("object", tool.getInputSchema().getType(), "Tool input schema type should be object");
        assertEquals(1, tool.getInputSchema().getProperties().size(), "Tool input schema should have one property");
        assertTrue(tool.getInputSchema().getProperties().containsKey("testProp"), "Tool input schema should contain testProp");
        assertEquals(1, tool.getInputSchema().getRequired().size(), "Tool input schema should have one required property");
        assertEquals("testProp", tool.getInputSchema().getRequired().get(0), "Required property should be testProp");
    }

    @Test
    public void testSetAndGetAnnotations() {
        ToolAnnotations annotations = new ToolAnnotations();
        Map<String, Object> properties = new HashMap<>();
        properties.put("key1", "value1");
        properties.put("key2", 123);
        annotations.setProperties(properties);
        
        tool.setAnnotations(annotations);
        
        assertNotNull(tool.getAnnotations(), "Tool annotations should not be null");
        assertEquals(2, tool.getAnnotations().getProperties().size(), "Tool annotations should have two properties");
        assertEquals("value1", tool.getAnnotations().getProperties().get("key1"), "Annotation key1 should have value1");
        assertEquals(123, tool.getAnnotations().getProperties().get("key2"), "Annotation key2 should have value 123");
    }

    @Test
    public void testToString() {
        tool.setName("testTool");
        tool.setDescription("Test description");
        tool.setType("function");
        
        String toolString = tool.toString();
        
        assertTrue(toolString.contains("testTool"), "toString should contain the tool name");
        assertTrue(toolString.contains("Test description"), "toString should contain the tool description");
        assertTrue(toolString.contains("function"), "toString should contain the tool type");
    }
    
    @Test
    public void testEqualsAndHashCode() {
        Tool tool1 = new Tool();
        tool1.setName("tool1");
        tool1.setDescription("desc1");
        
        Tool tool2 = new Tool();
        tool2.setName("tool1");
        tool2.setDescription("desc1");
        
        Tool tool3 = new Tool();
        tool3.setName("tool3");
        tool3.setDescription("desc3");
        
        assertEquals(tool1, tool2, "Tools with same properties should be equal");
        assertNotEquals(tool1, tool3, "Tools with different properties should not be equal");
        assertEquals(tool1.hashCode(), tool2.hashCode(), "Hash codes should be equal for equal tools");
    }
    
    @Test
    public void testNullValues() {
        Tool tool = new Tool();
        
        assertNull(tool.getName(), "Name should be null initially");
        assertNull(tool.getDescription(), "Description should be null initially");
        assertNull(tool.getType(), "Type should be null initially");
        assertNull(tool.getParameters(), "Parameters should be null initially");
        assertNull(tool.getInputSchema(), "InputSchema should be null initially");
        assertNull(tool.getAnnotations(), "Annotations should be null initially");
    }
}