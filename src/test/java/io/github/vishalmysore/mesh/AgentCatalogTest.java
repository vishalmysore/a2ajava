package io.github.vishalmysore.mesh;

import io.github.vishalmysore.a2a.client.A2AAgent;
import io.github.vishalmysore.common.Agent;

import io.github.vishalmysore.common.AgentInfo;
import io.github.vishalmysore.mcp.client.MCPAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.net.URL;

public class AgentCatalogTest {
    
    private AgentCatalog catalog;
    
    @Mock
    private AgentInfo mockAgentInfo;
    
    @Mock
    private MCPAgent mockMcpAgent;
    
    @Mock
    private A2AAgent mockA2aAgent;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        catalog = new AgentCatalog();
        
        // Configure mocks
        when(mockMcpAgent.getInfo()).thenReturn(mockAgentInfo);
        when(mockMcpAgent.getType()).thenReturn("mcp");
        when(mockA2aAgent.getInfo()).thenReturn(mockAgentInfo);
        when(mockA2aAgent.getType()).thenReturn("a2a");
    }
    


    


    
    @Test
    public void testAddAgentDirectly() {
        // Setup
        when(mockMcpAgent.isConnected()).thenReturn(true);
        
        // Test
        boolean result = catalog.addAgent(mockMcpAgent);
        
        // Verify
        assertTrue(result);
        assertTrue(catalog.getAgents().containsValue(mockMcpAgent));
    }
    
    @Test
    public void testAddNullAgent() {
        assertFalse(catalog.addAgent((Agent)null));
        assertEquals(0, catalog.getAgents().size());
    }
    
    @Test
    public void testRetrieveAgentByInfo() {
        // Setup
        catalog.addAgent(mockMcpAgent);
        
        // Test
        Agent result = catalog.retrieveAgent(mockAgentInfo);
        
        // Verify
        assertNotNull(result);
        assertEquals(mockMcpAgent, result);
    }
    

    

    
    @Test
    public void testGetAgentsInfoEmpty() {
        String result = catalog.getAgentsInfo();
        assertEquals("", result);
    }
}
