package io.github.vishalmysore.common;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class ClientRegistryForAgentsTest {
    
    private ClientRegistryForAgents registry;
    
    @Mock
    private AgentInfo mockAgentInfo;
    
    @Mock
    private Agent mockAgent;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        registry = new ClientRegistryForAgents();
        
        // Setup mock agent
        when(mockAgent.getInfo()).thenReturn(mockAgentInfo);
        when(mockAgentInfo.toString()).thenReturn("MockAgent");
    }
    
    @Test
    public void testAddAgent() {
        // When agent is added
        boolean result = registry.addAgent(mockAgent);
        
        // Then the result should be true
        assertTrue(result);
        
        // And the registry should contain the agent
        assertEquals(1, registry.getAgentCount());
        assertTrue(registry.hasAgent(mockAgentInfo));
    }
    
    @Test
    public void testAddNullAgent() {
        // When null agent is added
        boolean result = registry.addAgent(null);
        
        // Then the result should be false
        assertFalse(result);
        
        // And the registry should be empty
        assertEquals(0, registry.getAgentCount());
    }
    
    @Test
    public void testAddAgentWithNullInfo() {
        // Given agent with null info
        when(mockAgent.getInfo()).thenReturn(null);
        
        // When agent is added
        boolean result = registry.addAgent(mockAgent);
        
        // Then the result should be false
        assertFalse(result);
        
        // And the registry should be empty
        assertEquals(0, registry.getAgentCount());
    }
    
    @Test
    public void testRetrieveAgent() {
        // Given an agent in the registry
        registry.addAgent(mockAgent);
        
        // When the agent is retrieved
        Agent retrievedAgent = registry.retrieveAgent(mockAgentInfo);
        
        // Then it should be the same agent
        assertSame(mockAgent, retrievedAgent);
    }
    
    @Test
    public void testRetrieveNonExistentAgent() {
        // Given an empty registry
        
        // When retrieving with a valid AgentInfo
        Agent retrievedAgent = registry.retrieveAgent(mockAgentInfo);
        
        // Then the result should be null
        assertNull(retrievedAgent);
    }
    
    @Test
    public void testRetrieveAgentWithNullInfo() {
        // Given an agent in the registry
        registry.addAgent(mockAgent);
        
        // When retrieving with null AgentInfo
        Agent retrievedAgent = registry.retrieveAgent(null);
        
        // Then the result should be null
        assertNull(retrievedAgent);
    }
    
    @Test
    public void testHasAgent() {
        // Given an agent in the registry
        registry.addAgent(mockAgent);
        
        // When checking if the agent exists
        boolean exists = registry.hasAgent(mockAgentInfo);
        
        // Then the result should be true
        assertTrue(exists);
    }
    
    @Test
    public void testHasAgentWithNullInfo() {
        // Given an agent in the registry
        registry.addAgent(mockAgent);
        
        // When checking with null AgentInfo
        boolean exists = registry.hasAgent(null);
        
        // Then the result should be false
        assertFalse(exists);
    }
    
    @Test
    public void testGetAgentsInfo() {
        // Given an agent in the registry
        registry.addAgent(mockAgent);
        
        // When getting agents info
        String info = registry.getAgentsInfo();
        
        // Then the result should contain the agent info
        assertEquals("MockAgent", info);
    }
    
    @Test
    public void testGetAgentsInfoEmptyRegistry() {
        // Given an empty registry
        
        // When getting agents info
        String info = registry.getAgentsInfo();
        
        // Then the result should be an empty string
        assertEquals("", info);
    }
    
    @Test
    public void testGetAgentsInfoMultipleAgents() {
        // Given multiple agents
        Agent mockAgent2 = mock(Agent.class);
        AgentInfo mockInfo2 = mock(AgentInfo.class);
        when(mockAgent2.getInfo()).thenReturn(mockInfo2);
        when(mockInfo2.toString()).thenReturn("MockAgent2");
        
        // When adding multiple agents
        registry.addAgent(mockAgent);
        registry.addAgent(mockAgent2);
        
        // When getting agents info
        String info = registry.getAgentsInfo();
        
        // Then the result should contain both agents' info
        assertTrue(info.contains("MockAgent"));
        assertTrue(info.contains("MockAgent2"));
        assertTrue(info.contains(", "));
    }
}