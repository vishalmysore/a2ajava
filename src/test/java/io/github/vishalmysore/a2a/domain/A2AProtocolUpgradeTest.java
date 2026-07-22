package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the newer A2A protocol features: additional task states,
 * message identity/context fields, agent card security metadata and
 * the standard error codes introduced after the initial protocol release.
 */
public class A2AProtocolUpgradeTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testRejectedAndAuthRequiredTaskStates() {
        assertEquals(TaskState.REJECTED, TaskState.forValue("rejected"));
        assertEquals(TaskState.AUTH_REQUIRED, TaskState.forValue("auth-required"));
        assertEquals("rejected", TaskState.REJECTED.getValue());
        assertEquals("auth-required", TaskState.AUTH_REQUIRED.getValue());
    }

    @Test
    void testMessageIdentityAndContextFields() throws Exception {
        Message message = new Message();
        message.setRole("user");
        message.setMessageId("msg-1");
        message.setTaskId("task-1");
        message.setContextId("ctx-1");
        message.setReferenceTaskIds(Arrays.asList("task-0"));
        message.setExtensions(Collections.singletonList("https://a2ui.org/a2a-extension/a2ui/v0.8"));

        String json = objectMapper.writeValueAsString(message);
        assertTrue(json.contains("\"messageId\":\"msg-1\""));
        assertTrue(json.contains("\"contextId\":\"ctx-1\""));

        Message roundTrip = objectMapper.readValue(json, Message.class);
        assertEquals("msg-1", roundTrip.getMessageId());
        assertEquals("task-1", roundTrip.getTaskId());
        assertEquals("ctx-1", roundTrip.getContextId());
        assertEquals(Arrays.asList("task-0"), roundTrip.getReferenceTaskIds());
    }

    @Test
    void testTaskContextId() {
        Task task = new Task();
        task.setId("t1");
        task.setContextId("ctx-9");
        assertEquals("ctx-9", task.getContextId());
    }

    @Test
    void testAgentCardSecurityFields() throws Exception {
        AgentCard card = new AgentCard();
        card.setName("secure-agent");
        card.setCapabilities(new Capabilities(true, false, false));
        card.setIconUrl("https://example.com/icon.png");
        card.setSupportsAuthenticatedExtendedCard(true);

        SecurityScheme bearer = new SecurityScheme();
        bearer.setType("http");
        bearer.setScheme("bearer");
        bearer.setBearerFormat("JWT");
        Map<String, SecurityScheme> schemes = new HashMap<>();
        schemes.put("bearerAuth", bearer);
        card.setSecuritySchemes(schemes);

        Map<String, List<String>> requirement = new HashMap<>();
        requirement.put("bearerAuth", Collections.emptyList());
        card.setSecurity(Collections.singletonList(requirement));

        String json = objectMapper.writeValueAsString(card);
        assertTrue(json.contains("\"iconUrl\":\"https://example.com/icon.png\""));
        assertTrue(json.contains("\"supportsAuthenticatedExtendedCard\":true"));
        assertTrue(json.contains("\"bearerAuth\""));

        ObjectMapper lenientMapper = new ObjectMapper()
                .disable(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        AgentCard roundTrip = lenientMapper.readValue(json, AgentCard.class);
        assertEquals("JWT", roundTrip.getSecuritySchemes().get("bearerAuth").getBearerFormat());
        assertTrue(roundTrip.isSupportsAuthenticatedExtendedCard());
    }

    @Test
    void testAgentCardSignatureUsesProtectedJsonName() throws Exception {
        AgentCardSignature signature = new AgentCardSignature();
        signature.setProtectedHeader("eyJhbGciOiJFUzI1NiJ9");
        signature.setSignature("abc123");

        String json = objectMapper.writeValueAsString(signature);
        assertTrue(json.contains("\"protected\":\"eyJhbGciOiJFUzI1NiJ9\""));
        assertFalse(json.contains("protectedHeader"));

        AgentCardSignature roundTrip = objectMapper.readValue(json, AgentCardSignature.class);
        assertEquals("eyJhbGciOiJFUzI1NiJ9", roundTrip.getProtectedHeader());
    }

    @Test
    void testNewStandardErrorCodes() {
        assertEquals(-32005, new ContentTypeNotSupportedError().getCode());
        assertEquals(-32006, new InvalidAgentResponseError().getCode());
        assertEquals(-32007, new AuthenticatedExtendedCardNotConfiguredError().getCode());
    }
}
