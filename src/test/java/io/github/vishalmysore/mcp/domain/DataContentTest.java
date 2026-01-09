package io.github.vishalmysore.mcp.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test DataContent and A2UI support in MCP
 */
public class DataContentTest {
    
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    void testDataContentCreation() {
        DataContent dataContent = new DataContent();
        Map<String, Object> data = new HashMap<>();
        data.put("key", "value");
        dataContent.setData(data);
        
        assertEquals("data", dataContent.getType());
        assertNotNull(dataContent.getData());
        assertEquals("value", dataContent.getData().get("key"));
    }

    @Test
    void testDataContentWithMetadata() {
        DataContent dataContent = new DataContent();
        Map<String, Object> data = new HashMap<>();
        data.put("test", "data");
        dataContent.setData(data);
        
        Map<String, String> metadata = new HashMap<>();
        metadata.put("format", "json");
        dataContent.setMetadata(metadata);
        
        assertNotNull(dataContent.getMetadata());
        assertEquals("json", dataContent.getMetadata().get("format"));
    }

    @Test
    void testA2UIContentCreation() {
        Map<String, Object> a2uiMessage = new HashMap<>();
        Map<String, Object> beginRendering = new HashMap<>();
        beginRendering.put("surfaceId", "test_surface");
        a2uiMessage.put("beginRendering", beginRendering);
        
        DataContent a2uiContent = DataContent.createA2UIContent(a2uiMessage);
        
        assertNotNull(a2uiContent);
        assertTrue(a2uiContent.isA2UIData());
        assertEquals("application/json+a2ui", a2uiContent.getMetadata().get("mimeType"));
        assertNotNull(a2uiContent.getData());
        assertTrue(a2uiContent.getData().containsKey("beginRendering"));
    }

    @Test
    void testA2UIDetection() {
        // Regular DataContent
        DataContent regularContent = new DataContent();
        regularContent.setData(Map.of("key", "value"));
        assertFalse(regularContent.isA2UIData());
        
        // A2UI DataContent
        DataContent a2uiContent = new DataContent();
        a2uiContent.setData(Map.of("surfaceUpdate", Map.of("surfaceId", "test")));
        Map<String, String> metadata = new HashMap<>();
        metadata.put("mimeType", "application/json+a2ui");
        a2uiContent.setMetadata(metadata);
        assertTrue(a2uiContent.isA2UIData());
    }

    @Test
    void testA2UISurfaceUpdate() {
        Map<String, Object> surfaceUpdate = new HashMap<>();
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("surfaceId", "main_content");
        
        Map<String, Object> component = new HashMap<>();
        component.put("componentType", "Text");
        component.put("id", "text_1");
        
        Map<String, Object> props = new HashMap<>();
        props.put("text", "Hello from A2UI in MCP!");
        component.put("props", props);
        
        updateData.put("rootComponent", component);
        surfaceUpdate.put("surfaceUpdate", updateData);
        
        DataContent a2uiContent = DataContent.createA2UIContent(surfaceUpdate);
        
        assertTrue(a2uiContent.isA2UIData());
        assertEquals("application/json+a2ui", a2uiContent.getMetadata().get("mimeType"));
        assertTrue(a2uiContent.getData().containsKey("surfaceUpdate"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> update = (Map<String, Object>) a2uiContent.getData().get("surfaceUpdate");
        assertEquals("main_content", update.get("surfaceId"));
    }

    @Test
    void testDataContentSerialization() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "test data");
        
        DataContent content = new DataContent();
        content.setData(data);
        
        Map<String, String> metadata = new HashMap<>();
        metadata.put("version", "1.0");
        content.setMetadata(metadata);
        
        String json = objectMapper.writeValueAsString(content);
        System.out.println("DataContent JSON: " + json);
        
        assertTrue(json.contains("\"type\":\"data\""));
        assertTrue(json.contains("\"message\":\"test data\""));
        assertTrue(json.contains("\"metadata\""));
        
        // Deserialize back
        DataContent deserialized = objectMapper.readValue(json, DataContent.class);
        assertEquals("data", deserialized.getType());
        assertEquals("test data", deserialized.getData().get("message"));
        assertEquals("1.0", deserialized.getMetadata().get("version"));
    }

    @Test
    void testA2UIContentSerialization() throws Exception {
        Map<String, Object> a2uiMessage = new HashMap<>();
        Map<String, Object> beginRendering = new HashMap<>();
        beginRendering.put("surfaceId", "chart_surface");
        beginRendering.put("catalogId", "https://github.com/google/A2UI/blob/main/specification/0.8/json/standard_catalog_definition.json");
        a2uiMessage.put("beginRendering", beginRendering);
        
        DataContent a2uiContent = DataContent.createA2UIContent(a2uiMessage);
        
        String json = objectMapper.writeValueAsString(a2uiContent);
        System.out.println("A2UI DataContent JSON: " + json);
        
        assertTrue(json.contains("\"type\":\"data\""));
        assertTrue(json.contains("\"mimeType\":\"application/json+a2ui\""));
        assertTrue(json.contains("beginRendering"));
        assertTrue(json.contains("chart_surface"));
        
        // Deserialize back
        DataContent deserialized = objectMapper.readValue(json, DataContent.class);
        assertTrue(deserialized.isA2UIData());
        assertTrue(deserialized.getData().containsKey("beginRendering"));
    }
}
