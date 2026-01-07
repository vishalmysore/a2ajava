package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to verify A2A v1.0 JSON serialization format.
 * In v1.0, Part types use wrapper object discriminator (e.g., {"text":"value"})
 * instead of "kind" property discriminator.
 */
public class A2AV1SerializationTest {
    
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        // Configure to ignore unknown properties during deserialization
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    void testTextPartSerialization() throws Exception {
        // Create a TextPart
        TextPart textPart = new TextPart();
        textPart.setText("Hello, A2A v1.0!");
        Map<String, String> metadata = new HashMap<>();
        metadata.put("source", "test");
        textPart.setMetadata(metadata);

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(textPart);
        System.out.println("TextPart JSON: " + json);

        // Verify: Should use wrapper object format {"text":{...}} not {"kind":"TextPart",...}
        assertTrue(json.contains("\"text\""), "JSON should contain 'text' wrapper");
        assertFalse(json.contains("\"kind\""), "JSON should NOT contain 'kind' discriminator (v1.0)");
        assertFalse(json.contains("\"type\""), "JSON should NOT contain 'type' field (marked @JsonIgnore)");
        assertTrue(json.contains("Hello, A2A v1.0!"), "JSON should contain text content");
        assertTrue(json.contains("\"metadata\""), "JSON should contain metadata");

        // Deserialize back
        Part deserialized = objectMapper.readValue(json, Part.class);
        assertTrue(deserialized instanceof TextPart, "Should deserialize back to TextPart");
        assertEquals("Hello, A2A v1.0!", ((TextPart) deserialized).getText());
    }

    @Test
    void testFilePartSerialization() throws Exception {
        // Create a FilePart
        FilePart filePart = new FilePart();
        FileContent fileContent = new FileContent();
        fileContent.setUri("file:///example.txt");
        fileContent.setMimeType("text/plain");
        filePart.setFile(fileContent);
        
        Map<String, String> metadata = new HashMap<>();
        metadata.put("size", "1024");
        filePart.setMetadata(metadata);

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(filePart);
        System.out.println("FilePart JSON: " + json);

        // Verify: Should use wrapper object format {"file":{...}} not {"kind":"FilePart",...}
        assertTrue(json.contains("\"file\""), "JSON should contain 'file' wrapper");
        assertFalse(json.contains("\"kind\""), "JSON should NOT contain 'kind' discriminator (v1.0)");
        assertFalse(json.contains("\"type\""), "JSON should NOT contain 'type' field (marked @JsonIgnore)");
        assertTrue(json.contains("file:///example.txt"), "JSON should contain file URI");

        // Deserialize back
        Part deserialized = objectMapper.readValue(json, Part.class);
        assertTrue(deserialized instanceof FilePart, "Should deserialize back to FilePart");
        assertEquals("file:///example.txt", ((FilePart) deserialized).getFile().getUri());
    }

    @Test
    void testDataPartSerialization() throws Exception {
        // Create a DataPart
        DataPart dataPart = new DataPart();
        Map<String, Object> data = new HashMap<>();
        data.put("temperature", 25);
        data.put("unit", "celsius");
        dataPart.setData(data);
        
        Map<String, String> metadata = new HashMap<>();
        metadata.put("sensor", "temp-01");
        dataPart.setMetadata(metadata);

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(dataPart);
        System.out.println("DataPart JSON: " + json);

        // Verify: Should use wrapper object format {"data":{...}} not {"kind":"DataPart",...}
        assertTrue(json.contains("\"data\""), "JSON should contain 'data' wrapper");
        assertFalse(json.contains("\"kind\""), "JSON should NOT contain 'kind' discriminator (v1.0)");
        assertFalse(json.contains("\"type\""), "JSON should NOT contain 'type' field (marked @JsonIgnore)");
        assertTrue(json.contains("temperature"), "JSON should contain data content");

        // Deserialize back
        Part deserialized = objectMapper.readValue(json, Part.class);
        assertTrue(deserialized instanceof DataPart, "Should deserialize back to DataPart");
        assertEquals(25, ((DataPart) deserialized).getData().get("temperature"));
    }

    @Test
    void testMessageWithMultiplePartsSerialization() throws Exception {
        // Create a Message with multiple Part types
        Message message = new Message();
        message.setRole("user");
        
        TextPart textPart = new TextPart();
        textPart.setText("Please process this file:");
        
        FilePart filePart = new FilePart();
        FileContent fileContent = new FileContent();
        fileContent.setUri("file:///data.csv");
        fileContent.setMimeType("text/csv");
        filePart.setFile(fileContent);
        
        DataPart dataPart = new DataPart();
        Map<String, Object> data = new HashMap<>();
        data.put("format", "csv");
        dataPart.setData(data);
        
        message.setParts(Arrays.asList(textPart, filePart, dataPart));

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(message);
        System.out.println("Message with Parts JSON: " + json);

        // Verify: All parts should use wrapper format
        assertFalse(json.contains("\"kind\""), "JSON should NOT contain 'kind' discriminator anywhere (v1.0)");
        assertFalse(json.contains("\"type\":\"text\""), "JSON should NOT contain type field in parts");
        assertTrue(json.contains("\"text\""), "JSON should contain 'text' wrapper for TextPart");
        assertTrue(json.contains("\"file\""), "JSON should contain 'file' wrapper for FilePart");
        assertTrue(json.contains("\"data\""), "JSON should contain 'data' wrapper for DataPart");

        // Deserialize back
        Message deserialized = objectMapper.readValue(json, Message.class);
        assertNotNull(deserialized.getParts(), "Parts should not be null");
        assertEquals(3, deserialized.getParts().size(), "Should have 3 parts");
        assertTrue(deserialized.getParts().get(0) instanceof TextPart, "First part should be TextPart");
        assertTrue(deserialized.getParts().get(1) instanceof FilePart, "Second part should be FilePart");
        assertTrue(deserialized.getParts().get(2) instanceof DataPart, "Third part should be DataPart");
    }

    @Test
    void testAgentCardV1Fields() throws Exception {
        // Create an AgentCard with v1.0 fields
        AgentCard agentCard = new AgentCard();
        agentCard.setName("Test Agent");
        agentCard.setProtocolVersion("1.0");
        
        // Initialize required fields
        agentCard.setCapabilities(new Capabilities());
        
        // Add supportedInterfaces (v1.0 field)
        AgentInterface agentInterface = new AgentInterface();
        agentInterface.setUrl("https://example.com/agent");
        agentInterface.setProtocolBinding("https");
        agentInterface.setTenant("default");
        agentCard.setSupportedInterfaces(Arrays.asList(agentInterface));

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(agentCard);
        System.out.println("AgentCard v1.0 JSON: " + json);

        // Verify v1.0 fields
        assertTrue(json.contains("\"protocolVersion\":\"1.0\""), "JSON should contain protocolVersion 1.0");
        assertTrue(json.contains("\"supportedInterfaces\""), "JSON should contain supportedInterfaces (v1.0)");
        assertTrue(json.contains("\"protocolBinding\""), "JSON should contain protocolBinding in interfaces");

        // Deserialize back
        AgentCard deserialized = objectMapper.readValue(json, AgentCard.class);
        assertEquals("1.0", deserialized.getProtocolVersion(), "Protocol version should be 1.0");
        assertNotNull(deserialized.getSupportedInterfaces(), "Supported interfaces should not be null");
        assertEquals(1, deserialized.getSupportedInterfaces().size(), "Should have one interface");
        assertEquals("https://example.com/agent", deserialized.getSupportedInterfaces().get(0).getUrl());
    }

    @Test
    void testErrorTypesV1() throws Exception {
        // Test VersionNotSupportedError (-32009)
        VersionNotSupportedError versionError = new VersionNotSupportedError("Protocol version 2.0 is not supported");
        
        String json = objectMapper.writeValueAsString(versionError);
        System.out.println("VersionNotSupportedError JSON: " + json);
        
        assertTrue(json.contains("-32009"), "JSON should contain error code -32009");
        assertTrue(json.contains("not supported"), "JSON should contain error message");

        // Test ExtensionSupportRequiredError (-32008)
        ExtensionSupportRequiredError extensionError = new ExtensionSupportRequiredError("Extension 'advanced-streaming' is required");
        
        json = objectMapper.writeValueAsString(extensionError);
        System.out.println("ExtensionSupportRequiredError JSON: " + json);
        
        assertTrue(json.contains("-32008"), "JSON should contain error code -32008");
        assertTrue(json.contains("required"), "JSON should contain error message");
    }
}
