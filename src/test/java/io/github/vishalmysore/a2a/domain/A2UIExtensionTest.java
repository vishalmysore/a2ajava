package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test A2UI extension support in a2ajava
 */
public class A2UIExtensionTest {
    
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    void testAgentExtensionCreation() {
        AgentExtension extension = new AgentExtension();
        extension.setUri("https://a2ui.org/a2a-extension/a2ui/v0.8");
        extension.setDescription("Ability to render A2UI");
        extension.setRequired(false);
        
        Map<String, Object> params = new HashMap<>();
        params.put("supportedCatalogIds", Arrays.asList(
            "https://github.com/google/A2UI/blob/main/specification/0.8/json/standard_catalog_definition.json"
        ));
        params.put("acceptsInlineCatalogs", true);
        extension.setParams(params);
        
        assertEquals("https://a2ui.org/a2a-extension/a2ui/v0.8", extension.getUri());
        assertFalse(extension.isRequired());
        assertNotNull(extension.getParams());
        assertTrue((Boolean) extension.getParams().get("acceptsInlineCatalogs"));
    }

    @Test
    void testCapabilitiesWithExtensions() {
        Capabilities capabilities = new Capabilities();
        capabilities.setStreaming(true);
        capabilities.setPushNotifications(false);
        capabilities.setStateTransitionHistory(false);
        
        AgentExtension a2uiExtension = new AgentExtension();
        a2uiExtension.setUri("https://a2ui.org/a2a-extension/a2ui/v0.8");
        a2uiExtension.setDescription("Ability to render A2UI");
        a2uiExtension.setRequired(false);
        
        capabilities.setExtensions(Arrays.asList(a2uiExtension));
        
        assertNotNull(capabilities.getExtensions());
        assertEquals(1, capabilities.getExtensions().size());
        assertEquals("https://a2ui.org/a2a-extension/a2ui/v0.8", capabilities.getExtensions().get(0).getUri());
    }

    @Test
    void testAgentCardAddA2UISupport() {
        AgentCard agentCard = new AgentCard();
        agentCard.setName("Test Agent with A2UI");
        agentCard.setProtocolVersion("1.0");
        
        List<String> catalogIds = Arrays.asList(
            "https://github.com/google/A2UI/blob/main/specification/0.8/json/standard_catalog_definition.json"
        );
        
        agentCard.addA2UISupport(catalogIds, true);
        
        assertNotNull(agentCard.getCapabilities());
        assertNotNull(agentCard.getCapabilities().getExtensions());
        assertEquals(1, agentCard.getCapabilities().getExtensions().size());
        
        AgentExtension extension = agentCard.getCapabilities().getExtensions().get(0);
        assertEquals("https://a2ui.org/a2a-extension/a2ui/v0.8", extension.getUri());
        assertEquals("Ability to render A2UI", extension.getDescription());
        assertFalse(extension.isRequired());
        
        assertNotNull(extension.getParams());
        assertTrue((Boolean) extension.getParams().get("acceptsInlineCatalogs"));
        
        @SuppressWarnings("unchecked")
        List<String> supportedCatalogs = (List<String>) extension.getParams().get("supportedCatalogIds");
        assertEquals(1, supportedCatalogs.size());
    }

    @Test
    void testDataPartA2UICreation() {
        Map<String, Object> a2uiMessage = new HashMap<>();
        Map<String, Object> beginRendering = new HashMap<>();
        beginRendering.put("surfaceId", "outlier_stores_map_surface");
        a2uiMessage.put("beginRendering", beginRendering);
        
        DataPart a2uiPart = DataPart.createA2UIPart(a2uiMessage);
        
        assertNotNull(a2uiPart);
        assertTrue(a2uiPart.isA2UIData());
        assertEquals("application/json+a2ui", a2uiPart.getMetadata().get("mimeType"));
        assertNotNull(a2uiPart.getData());
        assertTrue(a2uiPart.getData().containsKey("beginRendering"));
    }

    @Test
    void testDataPartA2UIDetection() {
        // Regular DataPart
        DataPart regularPart = new DataPart();
        regularPart.setData(Map.of("key", "value"));
        assertFalse(regularPart.isA2UIData());
        
        // A2UI DataPart
        DataPart a2uiPart = new DataPart();
        a2uiPart.setData(Map.of("surfaceUpdate", Map.of("surfaceId", "test")));
        Map<String, String> metadata = new HashMap<>();
        metadata.put("mimeType", "application/json+a2ui");
        a2uiPart.setMetadata(metadata);
        assertTrue(a2uiPart.isA2UIData());
    }

    @Test
    void testAgentCardWithA2UISerialization() throws Exception {
        AgentCard agentCard = new AgentCard();
        agentCard.setName("A2UI-enabled Agent");
        agentCard.setProtocolVersion("1.0");
        agentCard.setCapabilities(new Capabilities());
        
        agentCard.addA2UISupport(
            Arrays.asList("https://github.com/google/A2UI/blob/main/specification/0.8/json/standard_catalog_definition.json"),
            true
        );
        
        String json = objectMapper.writeValueAsString(agentCard);
        System.out.println("AgentCard with A2UI: " + json);
        
        assertTrue(json.contains("https://a2ui.org/a2a-extension/a2ui/v0.8"));
        assertTrue(json.contains("supportedCatalogIds"));
        assertTrue(json.contains("acceptsInlineCatalogs"));
        assertTrue(json.contains("\"extensions\""));
        
        // Deserialize back
        AgentCard deserialized = objectMapper.readValue(json, AgentCard.class);
        assertNotNull(deserialized.getCapabilities());
        assertNotNull(deserialized.getCapabilities().getExtensions());
        assertEquals(1, deserialized.getCapabilities().getExtensions().size());
    }

    @Test
    void testA2UIClientCapabilities() {
        A2UIClientCapabilities clientCaps = new A2UIClientCapabilities();
        clientCaps.setSupportedCatalogIds(Arrays.asList(
            "https://github.com/google/A2UI/blob/main/specification/0.8/json/standard_catalog_definition.json"
        ));
        
        assertNotNull(clientCaps.getSupportedCatalogIds());
        assertEquals(1, clientCaps.getSupportedCatalogIds().size());
    }

    @Test
    void testMessageWithA2UIDataPart() throws Exception {
        Message message = new Message();
        message.setRole("agent");
        
        // Create A2UI surface update
        Map<String, Object> surfaceUpdate = new HashMap<>();
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("surfaceId", "main_content");
        Map<String, Object> component = new HashMap<>();
        component.put("componentType", "Text");
        component.put("id", "text_1");
        Map<String, Object> props = new HashMap<>();
        props.put("text", "Hello from A2UI!");
        component.put("props", props);
        updateData.put("rootComponent", component);
        surfaceUpdate.put("surfaceUpdate", updateData);
        
        DataPart a2uiPart = DataPart.createA2UIPart(surfaceUpdate);
        message.setParts(Arrays.asList(a2uiPart));
        
        String json = objectMapper.writeValueAsString(message);
        System.out.println("Message with A2UI DataPart: " + json);
        
        assertTrue(json.contains("application/json+a2ui"));
        assertTrue(json.contains("surfaceUpdate"));
        assertTrue(json.contains("Hello from A2UI!"));
        
        // Verify it deserializes correctly
        Message deserialized = objectMapper.readValue(json, Message.class);
        assertNotNull(deserialized.getParts());
        assertEquals(1, deserialized.getParts().size());
        assertTrue(deserialized.getParts().get(0) instanceof DataPart);
        
        DataPart deserializedPart = (DataPart) deserialized.getParts().get(0);
        assertTrue(deserializedPart.isA2UIData());
    }
}
