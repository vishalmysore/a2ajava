package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class DataPartTest {
    
    private DataPart dataPart;

    @BeforeEach
    void setUp() {
        dataPart = new DataPart();
    }

    @Test
    void testBasicDataPartCreation() {
        assertNotNull(dataPart, "DataPart should be created successfully");
        assertEquals("data", dataPart.getType(), "Default type should be 'data'");
    }

    @Test
    void testDataOperations() {
        // Create test data
        Map<String, Object> data = new HashMap<>();
        data.put("stringValue", "test");
        data.put("intValue", 42);
        data.put("boolValue", true);
        
        dataPart.setData(data);
        assertEquals(data, dataPart.getData(), "Data should match set value");
        assertEquals("test", dataPart.getData().get("stringValue"), "String value should match");
        assertEquals(42, dataPart.getData().get("intValue"), "Integer value should match");
        assertEquals(true, dataPart.getData().get("boolValue"), "Boolean value should match");
    }

    @Test
    void testMetadataOperations() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key1", "value1");
        metadata.put("key2", "value2");
        
        dataPart.setMetadata(metadata);
        assertEquals(metadata, dataPart.getMetadata(), "Metadata should match set value");
        assertEquals("value1", dataPart.getMetadata().get("key1"), "Metadata value 1 should match");
        assertEquals("value2", dataPart.getMetadata().get("key2"), "Metadata value 2 should match");
    }

    @Test
    void testTypeOperations() {
        assertEquals("data", dataPart.getType(), "Initial type should be 'data'");
        
        dataPart.setType("custom_data");
        assertEquals("custom_data", dataPart.getType(), "Type should be updated");
    }

    @Test
    void testNullValues() {
        dataPart.setData(null);
        assertNull(dataPart.getData(), "Data should allow null value");
        
        dataPart.setMetadata(null);
        assertNull(dataPart.getMetadata(), "Metadata should allow null value");
        
        dataPart.setType(null);
        assertNull(dataPart.getType(), "Type should allow null value");
    }

    @Test
    void testComplexDataStructures() {
        Map<String, Object> complexData = new HashMap<>();
        
        // Add nested map
        Map<String, Object> nestedMap = new HashMap<>();
        nestedMap.put("nestedKey", "nestedValue");
        complexData.put("mapValue", nestedMap);
        
        // Add array/list values
        complexData.put("arrayValue", new String[]{"one", "two", "three"});
        
        // Add null value
        complexData.put("nullValue", null);
        
        dataPart.setData(complexData);
        assertEquals(complexData, dataPart.getData(), "Complex data structure should be preserved");
        assertNotNull(dataPart.getData().get("mapValue"), "Nested map should be preserved");
        assertNull(dataPart.getData().get("nullValue"), "Null values should be preserved");
    }

    @Test
    void testToString() {
        Map<String, Object> data = new HashMap<>();
        data.put("key", "value");
        dataPart.setData(data);
        
        Map<String, String> metadata = new HashMap<>();
        metadata.put("metaKey", "metaValue");
        dataPart.setMetadata(metadata);
        
        String toString = dataPart.toString();
        assertNotNull(toString, "toString should not be null");
        assertTrue(toString.contains("data"), "toString should contain type");
        assertTrue(toString.contains("key"), "toString should contain data key");
        assertTrue(toString.contains("value"), "toString should contain data value");
        assertTrue(toString.contains("metaKey"), "toString should contain metadata key");
        assertTrue(toString.contains("metaValue"), "toString should contain metadata value");
    }
}
