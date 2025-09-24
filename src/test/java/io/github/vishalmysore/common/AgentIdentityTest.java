package io.github.vishalmysore.common;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AgentIdentityTest {

    @Test
    public void testAgentIdentityConstructor() {
        // Test constructor with parameters
        MockAgentInfo agentInfo = new MockAgentInfo("TestAgent");
        String url = "http://localhost:8080";
        
        AgentIdentity identity = new AgentIdentity(agentInfo, url);
        
        assertEquals(agentInfo, identity.getAllTheCapabilitiesOfTheAgent());
        assertEquals(url, identity.getUrl());
        assertNotNull(identity.getAgentUniqueIDTobeUsedToIdentifyTheAgent()); // UUID should be generated
    }
    
    @Test
    public void testAgentIdentityConstructorWithThreeArgs() {
        // Test constructor with three parameters
        MockAgentInfo agentInfo = new MockAgentInfo("TestAgent");
        String url = "http://localhost:8080";
        String uniqueId = "agent-123";
        
        AgentIdentity identity = new AgentIdentity(uniqueId, agentInfo, url);
        
        assertEquals(uniqueId, identity.getAgentUniqueIDTobeUsedToIdentifyTheAgent());
        assertEquals(agentInfo, identity.getAllTheCapabilitiesOfTheAgent());
        assertEquals(url, identity.getUrl());
    }
    
    @Test
    public void testSetterMethods() {
        // Test setter methods
        MockAgentInfo agentInfo = new MockAgentInfo("TestAgent");
        String url = "http://localhost:8080";
        String uniqueId = "agent-123";
        
        // Create with constructor first
        AgentIdentity identity = new AgentIdentity("initial", new MockAgentInfo("Initial"), "http://initial");
        
        // Then test setters
        identity.setAgentUniqueIDTobeUsedToIdentifyTheAgent(uniqueId);
        identity.setAllTheCapabilitiesOfTheAgent(agentInfo);
        identity.setUrl(url);
        
        assertEquals(uniqueId, identity.getAgentUniqueIDTobeUsedToIdentifyTheAgent());
        assertEquals(agentInfo, identity.getAllTheCapabilitiesOfTheAgent());
        assertEquals(url, identity.getUrl());
    }
    
    @Test
    public void testEqualsAndHashCode() {
        // Test equals and hashCode methods
        MockAgentInfo agentInfo1 = new MockAgentInfo("TestAgent1");
        MockAgentInfo agentInfo2 = new MockAgentInfo("TestAgent2");
        
        AgentIdentity identity1 = new AgentIdentity("agent-123", agentInfo1, "http://localhost:8080");
        AgentIdentity identity2 = new AgentIdentity("agent-123", agentInfo2, "http://localhost:8080");
        AgentIdentity identity3 = new AgentIdentity("agent-456", agentInfo1, "http://localhost:8080");
        
        // Test equals - should only compare agentUniqueIDTobeUsedToIdentifyTheAgent (EqualsAndHashCode.Include annotation)
        assertEquals(identity1, identity1); // Self equality
        assertEquals(identity1, identity2); // Equal objects with same ID but different agent info
        assertNotEquals(identity1, identity3); // Different uniqueId
        assertNotEquals(identity1, null); // Null comparison
        assertNotEquals(identity1, new Object()); // Different types
        
        // Test hashCode - should only be based on agentUniqueIDTobeUsedToIdentifyTheAgent
        assertEquals(identity1.hashCode(), identity2.hashCode()); // Equal objects have equal hash codes
        assertNotEquals(identity1.hashCode(), identity3.hashCode()); // Different ID means different hash
    }
    
    @Test
    public void testToString() {
        // Test toString method
        MockAgentInfo agentInfo = new MockAgentInfo("TestAgent");
        
        AgentIdentity identity = new AgentIdentity("agent-123", agentInfo, "http://localhost:8080");
        String toString = identity.toString();
        
        // Verify the toString contains important fields - URL should not be included based on @ToString annotation
        assertTrue(toString.contains("agent-123"), "toString should contain ID");
        assertTrue(toString.contains("MockAgentInfo") || toString.contains("allTheCapabilitiesOfTheAgent"), 
                "toString should contain agent info");
        assertFalse(toString.contains("http://localhost:8080"), "toString should not contain URL");
    }
    
    @Test
    public void testBuilderPattern() {
        // Test builder pattern
        MockAgentInfo agentInfo = new MockAgentInfo("TestAgent");
        
        AgentIdentity identity = AgentIdentity.builder()
            .info(agentInfo)
            .url("http://localhost:8080")
            .build();
        
        assertNotNull(identity.getAgentUniqueIDTobeUsedToIdentifyTheAgent()); // UUID generated
        assertEquals(agentInfo, identity.getAllTheCapabilitiesOfTheAgent());
        assertEquals("http://localhost:8080", identity.getUrl());
    }
}
