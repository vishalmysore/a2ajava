package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolInputSchemaTest {

    private ToolInputSchema schema;

    @BeforeEach
    public void setUp() {
        schema = new ToolInputSchema();
    }

    @Test
    public void testToolInputSchemaInitialization() {
        assertNotNull(schema, "ToolInputSchema should be initialized successfully");
        assertEquals("object", schema.getType(), "Type should be initialized to 'object'");
        assertNull(schema.getProperties(), "Properties should be null initially");
        assertNull(schema.getRequired(), "Required list should be null initially");
    }

    @Test
    public void testGetType() {
        assertEquals("object", schema.getType(), "Type should always be 'object'");
    }

    @Test
    public void testSetAndGetProperties() {
        Map<String, ToolPropertySchema> properties = new HashMap<>();
        
        ToolPropertySchema prop1 = new ToolPropertySchema();
        prop1.setType("string");
        prop1.setDescription("First property");
        
        ToolPropertySchema prop2 = new ToolPropertySchema();
        prop2.setType("integer");
        prop2.setDescription("Second property");
        
        properties.put("prop1", prop1);
        properties.put("prop2", prop2);
        
        schema.setProperties(properties);
        
        assertNotNull(schema.getProperties(), "Properties should not be null after setting");
        assertEquals(2, schema.getProperties().size(), "Properties should contain 2 items");
        assertTrue(schema.getProperties().containsKey("prop1"), "Properties should contain prop1");
        assertTrue(schema.getProperties().containsKey("prop2"), "Properties should contain prop2");
        assertEquals("string", schema.getProperties().get("prop1").getType(), "prop1 should be of type string");
        assertEquals("integer", schema.getProperties().get("prop2").getType(), "prop2 should be of type integer");
    }
    
    @Test
    public void testSetAndGetRequired() {
        List<String> required = Arrays.asList("prop1", "prop2");
        schema.setRequired(required);
        
        assertNotNull(schema.getRequired(), "Required list should not be null after setting");
        assertEquals(2, schema.getRequired().size(), "Required list should contain 2 items");
        assertEquals("prop1", schema.getRequired().get(0), "First required property should be prop1");
        assertEquals("prop2", schema.getRequired().get(1), "Second required property should be prop2");
    }
    
    @Test
    public void testToString() {
        Map<String, ToolPropertySchema> properties = new HashMap<>();
        ToolPropertySchema prop = new ToolPropertySchema();
        prop.setType("string");
        prop.setDescription("Test property");
        properties.put("testProp", prop);
        schema.setProperties(properties);
        
        List<String> required = Arrays.asList("testProp");
        schema.setRequired(required);
        
        String result = schema.toString();
        
        assertTrue(result.contains("testProp"), "toString should contain property name");
        assertTrue(result.contains("type=object"), "toString should contain type value");
        assertTrue(result.contains("string"), "toString should contain property type");
    }
    
    @Test
    public void testEqualsAndHashCode() {
        ToolInputSchema schema1 = new ToolInputSchema();
        Map<String, ToolPropertySchema> properties1 = new HashMap<>();
        ToolPropertySchema prop1 = new ToolPropertySchema();
        prop1.setType("string");
        properties1.put("testProp", prop1);
        schema1.setProperties(properties1);
        schema1.setRequired(Arrays.asList("testProp"));
        
        ToolInputSchema schema2 = new ToolInputSchema();
        Map<String, ToolPropertySchema> properties2 = new HashMap<>();
        ToolPropertySchema prop2 = new ToolPropertySchema();
        prop2.setType("string");
        properties2.put("testProp", prop2);
        schema2.setProperties(properties2);
        schema2.setRequired(Arrays.asList("testProp"));
        
        ToolInputSchema schema3 = new ToolInputSchema();
        Map<String, ToolPropertySchema> properties3 = new HashMap<>();
        ToolPropertySchema prop3 = new ToolPropertySchema();
        prop3.setType("integer");
        properties3.put("differentProp", prop3);
        schema3.setProperties(properties3);
        
        assertEquals(schema1, schema2, "Schemas with same properties should be equal");
        assertNotEquals(schema1, schema3, "Schemas with different properties should not be equal");
        assertEquals(schema1.hashCode(), schema2.hashCode(), "Hash codes should be equal for equal schemas");
    }
    
    @Test
    public void testEmptyProperties() {
        Map<String, ToolPropertySchema> emptyProperties = new HashMap<>();
        schema.setProperties(emptyProperties);
        
        assertNotNull(schema.getProperties(), "Properties should not be null even when empty");
        assertTrue(schema.getProperties().isEmpty(), "Properties should be empty");
    }
    
    @Test
    public void testEmptyRequired() {
        List<String> emptyRequired = Arrays.asList();
        schema.setRequired(emptyRequired);
        
        assertNotNull(schema.getRequired(), "Required list should not be null even when empty");
        assertTrue(schema.getRequired().isEmpty(), "Required list should be empty");
    }
}