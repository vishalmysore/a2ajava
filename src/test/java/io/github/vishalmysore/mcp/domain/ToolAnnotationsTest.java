package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class ToolAnnotationsTest {

    private ToolAnnotations annotations;

    @BeforeEach
    public void setUp() {
        annotations = new ToolAnnotations();
    }

    @Test
    public void testToolAnnotationsInitialization() {
        assertNotNull(annotations, "ToolAnnotations should be initialized successfully");
        assertNotNull(annotations.getProperties(), "Properties map should be initialized");
        assertTrue(annotations.getProperties().isEmpty(), "Properties map should be empty initially");
    }

    @Test
    public void testSetAndGetProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("key1", "value1");
        properties.put("key2", 123);
        properties.put("key3", true);
        
        annotations.setProperties(properties);
        
        assertNotNull(annotations.getProperties(), "Properties should not be null after setting");
        assertEquals(3, annotations.getProperties().size(), "Properties should contain 3 items");
        assertEquals("value1", annotations.getProperties().get("key1"), "key1 should have value1");
        assertEquals(123, annotations.getProperties().get("key2"), "key2 should have value 123");
        assertEquals(true, annotations.getProperties().get("key3"), "key3 should have value true");
    }

    @Test
    public void testAddProperty() {
        annotations.getProperties().put("testKey", "testValue");
        
        assertEquals(1, annotations.getProperties().size(), "Properties should contain 1 item");
        assertEquals("testValue", annotations.getProperties().get("testKey"), "testKey should have testValue");
    }

    @Test
    public void testToString() {
        annotations.getProperties().put("key1", "value1");
        annotations.getProperties().put("key2", 123);
        
        String result = annotations.toString();
        
        assertTrue(result.contains("key1"), "toString should contain key1");
        assertTrue(result.contains("value1"), "toString should contain value1");
        assertTrue(result.contains("key2"), "toString should contain key2");
        assertTrue(result.contains("123"), "toString should contain 123");
    }

    @Test
    public void testEqualsAndHashCode() {
        ToolAnnotations anno1 = new ToolAnnotations();
        anno1.getProperties().put("key", "value");
        
        ToolAnnotations anno2 = new ToolAnnotations();
        anno2.getProperties().put("key", "value");
        
        ToolAnnotations anno3 = new ToolAnnotations();
        anno3.getProperties().put("differentKey", "differentValue");
        
        assertEquals(anno1, anno2, "Annotations with same properties should be equal");
        assertNotEquals(anno1, anno3, "Annotations with different properties should not be equal");
        assertEquals(anno1.hashCode(), anno2.hashCode(), "Hash codes should be equal for equal annotations");
    }
    
    @Test
    public void testComplexPropertyValues() {
        Map<String, Object> nestedMap = new HashMap<>();
        nestedMap.put("nestedKey1", "nestedValue1");
        nestedMap.put("nestedKey2", 456);
        
        annotations.getProperties().put("simpleKey", "simpleValue");
        annotations.getProperties().put("numericKey", 123);
        annotations.getProperties().put("booleanKey", true);
        annotations.getProperties().put("nullKey", null);
        annotations.getProperties().put("complexKey", nestedMap);
        
        assertEquals(5, annotations.getProperties().size(), "Properties should contain 5 items");
        assertEquals("simpleValue", annotations.getProperties().get("simpleKey"), "simpleKey should have correct value");
        assertEquals(123, annotations.getProperties().get("numericKey"), "numericKey should have correct value");
        assertEquals(true, annotations.getProperties().get("booleanKey"), "booleanKey should have correct value");
        assertNull(annotations.getProperties().get("nullKey"), "nullKey should have null value");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> retrievedNestedMap = (Map<String, Object>) annotations.getProperties().get("complexKey");
        
        assertNotNull(retrievedNestedMap, "Retrieved nested map should not be null");
        assertEquals(2, retrievedNestedMap.size(), "Retrieved nested map should have 2 entries");
        assertEquals("nestedValue1", retrievedNestedMap.get("nestedKey1"), "Nested value 1 should be correct");
        assertEquals(456, retrievedNestedMap.get("nestedKey2"), "Nested value 2 should be correct");
    }
    
    @Test
    public void testClearProperties() {
        annotations.getProperties().put("key1", "value1");
        annotations.getProperties().put("key2", "value2");
        
        assertEquals(2, annotations.getProperties().size(), "Properties should contain 2 items before clearing");
        
        annotations.getProperties().clear();
        
        assertEquals(0, annotations.getProperties().size(), "Properties should be empty after clearing");
    }
}