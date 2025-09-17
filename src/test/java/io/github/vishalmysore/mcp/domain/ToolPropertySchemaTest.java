package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class ToolPropertySchemaTest {

    private ToolPropertySchema propertySchema;

    @BeforeEach
    public void setUp() {
        propertySchema = new ToolPropertySchema();
    }

    @Test
    public void testToolPropertySchemaInitialization() {
        assertNotNull(propertySchema, "ToolPropertySchema should be initialized successfully");
        assertNull(propertySchema.getType(), "Type should be null initially");
        assertNull(propertySchema.getDescription(), "Description should be null initially");
        assertNull(propertySchema.getAdditionalProperties(), "AdditionalProperties should be null initially");
        assertFalse(propertySchema.isItems(), "Items should be false initially");
    }

    @Test
    public void testSetAndGetType() {
        String type = "string";
        propertySchema.setType(type);
        assertEquals(type, propertySchema.getType(), "Type should be set and retrieved correctly");
    }

    @Test
    public void testSetAndGetDescription() {
        String description = "Test property description";
        propertySchema.setDescription(description);
        assertEquals(description, propertySchema.getDescription(), "Description should be set and retrieved correctly");
    }

    @Test
    public void testSetAndGetAdditionalProperties() {
        Map<String, Object> additionalProps = new HashMap<>();
        additionalProps.put("format", "date-time");
        additionalProps.put("minimum", 0);
        additionalProps.put("maximum", 100);
        
        propertySchema.setAdditionalProperties(additionalProps);
        
        assertNotNull(propertySchema.getAdditionalProperties(), "AdditionalProperties should not be null after setting");
        assertEquals(3, propertySchema.getAdditionalProperties().size(), "AdditionalProperties should have 3 entries");
        assertEquals("date-time", propertySchema.getAdditionalProperties().get("format"), "Format should be date-time");
        assertEquals(0, propertySchema.getAdditionalProperties().get("minimum"), "Minimum should be 0");
        assertEquals(100, propertySchema.getAdditionalProperties().get("maximum"), "Maximum should be 100");
    }

    @Test
    public void testSetAndGetItems() {
        propertySchema.setItems(true);
        assertTrue(propertySchema.isItems(), "Items should be true after setting to true");
        
        propertySchema.setItems(false);
        assertFalse(propertySchema.isItems(), "Items should be false after setting to false");
    }

    @Test
    public void testToString() {
        propertySchema.setType("number");
        propertySchema.setDescription("A numeric property");
        propertySchema.setItems(true);
        
        Map<String, Object> additionalProps = new HashMap<>();
        additionalProps.put("format", "float");
        propertySchema.setAdditionalProperties(additionalProps);
        
        String result = propertySchema.toString();
        
        assertTrue(result.contains("number"), "toString should contain the type");
        assertTrue(result.contains("A numeric property"), "toString should contain the description");
        assertTrue(result.contains("items=true"), "toString should contain the items value");
        assertTrue(result.contains("format"), "toString should contain the additionalProperties keys");
        assertTrue(result.contains("float"), "toString should contain the additionalProperties values");
    }

    @Test
    public void testEqualsAndHashCode() {
        ToolPropertySchema schema1 = new ToolPropertySchema();
        schema1.setType("boolean");
        schema1.setDescription("A boolean property");
        schema1.setItems(false);
        
        ToolPropertySchema schema2 = new ToolPropertySchema();
        schema2.setType("boolean");
        schema2.setDescription("A boolean property");
        schema2.setItems(false);
        
        ToolPropertySchema schema3 = new ToolPropertySchema();
        schema3.setType("array");
        schema3.setDescription("An array property");
        schema3.setItems(true);
        
        assertEquals(schema1, schema2, "Property schemas with same values should be equal");
        assertNotEquals(schema1, schema3, "Property schemas with different values should not be equal");
        assertEquals(schema1.hashCode(), schema2.hashCode(), "Hash codes should be equal for equal property schemas");
    }
    
    @Test
    public void testVariousTypes() {
        String[] types = {"string", "number", "integer", "boolean", "object", "array", "null"};
        
        for (String type : types) {
            propertySchema.setType(type);
            assertEquals(type, propertySchema.getType(), "Type " + type + " should be set and retrieved correctly");
        }
    }
    
    @Test
    public void testNestedAdditionalProperties() {
        Map<String, Object> nestedMap = new HashMap<>();
        nestedMap.put("nestedKey", "nestedValue");
        
        Map<String, Object> additionalProps = new HashMap<>();
        additionalProps.put("simpleKey", "simpleValue");
        additionalProps.put("nestedObject", nestedMap);
        
        propertySchema.setAdditionalProperties(additionalProps);
        
        assertEquals("simpleValue", propertySchema.getAdditionalProperties().get("simpleKey"), 
                    "Simple value should be retrieved correctly");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> retrievedNestedMap = 
            (Map<String, Object>) propertySchema.getAdditionalProperties().get("nestedObject");
        
        assertNotNull(retrievedNestedMap, "Nested map should not be null");
        assertEquals("nestedValue", retrievedNestedMap.get("nestedKey"), 
                    "Nested value should be retrieved correctly");
    }
}