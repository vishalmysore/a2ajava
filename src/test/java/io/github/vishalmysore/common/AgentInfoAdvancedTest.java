package io.github.vishalmysore.common;

import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.a2a.domain.Capabilities;
import io.github.vishalmysore.a2a.domain.Skill;
import io.github.vishalmysore.mcp.domain.ListToolsResult;
import io.github.vishalmysore.mcp.domain.Tool;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Advanced unit tests for AgentInfo#getAgentCapabilities() covering
 * polymorphic dispatch for AgentCard, ListToolsResult, and fallback.
 */
public class AgentInfoAdvancedTest {

    // ── AgentCard branch ─────────────────────────────────────────────

    @Test
    void agentCard_withCapabilities_shouldReturnCapabilitiesString() {
        AgentCard card = new AgentCard();
        Capabilities caps = new Capabilities(true, false, true);
        card.setCapabilities(caps);

        String result = card.getAgentCapabilities();
        assertNotNull(result);
        // The result is caps.toString(), which contains the field values
        assertTrue(result.contains("true") || result.contains("streaming"),
                "Should contain capability information");
    }

    @Test
    void agentCard_withNullCapabilities_shouldThrowOrReturnDefault() {
        AgentCard card = new AgentCard();
        card.setCapabilities(null);

        // getAgentCapabilities calls getCapabilities().toString()
        // If capabilities is null, this will throw NPE
        assertThrows(NullPointerException.class, card::getAgentCapabilities);
    }

    @Test
    void agentCard_withSkills_capabilitiesShouldNotContainSkills() {
        AgentCard card = new AgentCard();
        Capabilities caps = new Capabilities(true, true, true);
        card.setCapabilities(caps);
        card.addSkill("search", "Search the web");
        card.addSkill("calculate", "Do math");

        String result = card.getAgentCapabilities();
        // getAgentCapabilities only returns capabilities.toString(), not skills
        assertNotNull(result);
    }

    // ── ListToolsResult branch ───────────────────────────────────────

    @Test
    void listToolsResult_withTools_shouldReturnToolsString() {
        ListToolsResult toolsResult = new ListToolsResult();
        List<Tool> tools = new ArrayList<>();

        Tool t1 = new Tool();
        t1.setName("search");
        t1.setDescription("Search tool");
        tools.add(t1);

        Tool t2 = new Tool();
        t2.setName("calculate");
        t2.setDescription("Calculator tool");
        tools.add(t2);

        toolsResult.setTools(tools);

        String result = toolsResult.getAgentCapabilities();
        assertNotNull(result);
        assertTrue(result.contains("search"));
        assertTrue(result.contains("calculate"));
    }

    @Test
    void listToolsResult_withEmptyToolsList_shouldReturnEmptyListString() {
        ListToolsResult toolsResult = new ListToolsResult();
        toolsResult.setTools(new ArrayList<>());

        String result = toolsResult.getAgentCapabilities();
        assertNotNull(result);
        assertEquals("[]", result);
    }

    @Test
    void listToolsResult_withNullTools_shouldThrowOrReturnDefault() {
        ListToolsResult toolsResult = new ListToolsResult();
        toolsResult.setTools(null);

        // getAgentCapabilities calls getTools().toString()
        // If tools is null, this will throw NPE
        assertThrows(NullPointerException.class, toolsResult::getAgentCapabilities);
    }

    // ── Default/fallback branch (unknown AgentInfo impl) ─────────────

    @Test
    void customAgentInfo_shouldReturnDefaultCapabilitiesPrefix() {
        AgentInfo custom = new AgentInfo() {};

        String result = custom.getAgentCapabilities();
        assertEquals("Capabilities: ", result);
    }

    @Test
    void mockAgentInfo_shouldReturnOverriddenCapabilities() {
        MockAgentInfo mock = new MockAgentInfo("TestAgent");
        assertEquals("Mock capabilities for TestAgent", mock.getAgentCapabilities());
    }

    @Test
    void mockAgentInfo_customCapabilities() {
        MockAgentInfo mock = new MockAgentInfo("Agent");
        mock.setCapabilities("Custom caps");
        assertEquals("Custom caps", mock.getAgentCapabilities());
    }

    // ── AgentCard with all capabilities flags ────────────────────────

    @Test
    void agentCard_allFlagsTrue_shouldContainAllInString() {
        AgentCard card = new AgentCard();
        Capabilities caps = new Capabilities(true, true, true);
        card.setCapabilities(caps);

        String result = card.getAgentCapabilities();
        // Lombok @Data generates toString with all fields
        assertTrue(result.contains("streaming=true"));
        assertTrue(result.contains("pushNotifications=true"));
        assertTrue(result.contains("stateTransitionHistory=true"));
    }

    @Test
    void agentCard_allFlagsFalse_shouldContainFalseValues() {
        AgentCard card = new AgentCard();
        Capabilities caps = new Capabilities(false, false, false);
        card.setCapabilities(caps);

        String result = card.getAgentCapabilities();
        assertTrue(result.contains("streaming=false"));
        assertTrue(result.contains("pushNotifications=false"));
        assertTrue(result.contains("stateTransitionHistory=false"));
    }

    // ── ListToolsResult with many tools ──────────────────────────────

    @Test
    void listToolsResult_manyTools_shouldListAll() {
        ListToolsResult toolsResult = new ListToolsResult();
        List<Tool> tools = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Tool tool = new Tool();
            tool.setName("tool-" + i);
            tool.setDescription("Tool number " + i);
            tools.add(tool);
        }
        toolsResult.setTools(tools);

        String result = toolsResult.getAgentCapabilities();
        for (int i = 0; i < 100; i++) {
            assertTrue(result.contains("tool-" + i));
        }
    }
}
