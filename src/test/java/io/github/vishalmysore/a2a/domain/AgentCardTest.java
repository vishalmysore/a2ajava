package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AgentCardTest {

    @Test
    public void testAgentCardInitialization() {
        AgentCard agentCard = new AgentCard();
        assertNotNull(agentCard, "AgentCard should be initialized successfully");
    }

    // Additional tests for AgentCard methods can be added here
}