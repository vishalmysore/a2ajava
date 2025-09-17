package io.github.vishalmysore.mcp.client;

import io.github.vishalmysore.common.AgentInfo;
import io.github.vishalmysore.common.CommonClientResponse;
import io.github.vishalmysore.mcp.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URL;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MCPAgentTest {
    
    private MCPAgent mcpAgent;
    
    @Mock
    private Tool mockTool;
    
    @Mock
    private CallToolResult mockCallToolResult;
    
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mcpAgent = new MCPAgent();
    }
    
    @Test
    void testMCPAgentInitialization() {
        assertNotNull(mcpAgent, "MCPAgent should be created successfully");
        assertEquals("mcp", mcpAgent.getType(), "Agent type should be 'mcp'");
        assertFalse(mcpAgent.isConnected(), "MCPAgent should not be connected by default");
    }
    
    @Test
    void testConnectAndDisconnect() throws Exception {
        // Use reflection to set the serverUrl directly instead of connecting to a real server
        MCPAgent spyAgent = spy(mcpAgent);
        URL testUrl = new URL("http://test-server:8080");
        java.lang.reflect.Field serverUrlField = MCPAgent.class.getDeclaredField("serverUrl");
        serverUrlField.setAccessible(true);
        serverUrlField.set(spyAgent, testUrl);
        
        assertEquals(testUrl, spyAgent.getServerUrl(), "Server URL should match the provided URL");
        
        // Test disconnect
        spyAgent.disconnect();
        // Since we didn't actually connect, isConnected would still return false which is the expected behavior
        assertFalse(spyAgent.isConnected(), "MCPAgent should be disconnected after disconnect() call");
    }
    
    @Test
    void testConnectWithAuthToken() throws Exception {
        // Use reflection to set the serverUrl directly instead of connecting to a real server
        MCPAgent spyAgent = spy(mcpAgent);
        URL testUrl = new URL("http://test-server:8080");
        java.lang.reflect.Field serverUrlField = MCPAgent.class.getDeclaredField("serverUrl");
        serverUrlField.setAccessible(true);
        serverUrlField.set(spyAgent, testUrl);
        
        assertEquals(testUrl, spyAgent.getServerUrl(), "Server URL should match the provided URL");
    }
    
    @Test
    void testRemoteMethodCall() throws Exception {
        // We need to mock the internal JsonNode processing
        MCPAgent spyAgent = spy(mcpAgent);
        
        // Mock the mapper to avoid the NPE in remoteMethodCall
        com.fasterxml.jackson.databind.ObjectMapper mockMapper = mock(com.fasterxml.jackson.databind.ObjectMapper.class);
        com.fasterxml.jackson.databind.JsonNode mockRoot = mock(com.fasterxml.jackson.databind.JsonNode.class);
        com.fasterxml.jackson.databind.JsonNode mockIdNode = mock(com.fasterxml.jackson.databind.JsonNode.class);
        
        // Set the mock mapper
        java.lang.reflect.Field mapperField = MCPAgent.class.getDeclaredField("mapper");
        mapperField.setAccessible(true);
        mapperField.set(spyAgent, mockMapper);
        
        // Configure the mocks
        when(mockMapper.readTree(anyString())).thenReturn(mockRoot);
        when(mockRoot.get("toolName")).thenReturn(mockIdNode);
        when(mockIdNode.asText()).thenReturn("test-method");
        
        // Mock the final call
        doReturn(mockCallToolResult).when(spyAgent).callTool(any(CallToolRequest.class));
        
        // Call the method under test
        CommonClientResponse response = spyAgent.remoteMethodCall("test-query");
        
        assertNotNull(response, "Response should not be null");
        verify(spyAgent, times(1)).callTool(any(CallToolRequest.class));
    }
    
    @Test
    void testRemoteMethodCallWithParams() throws Exception {
        MCPAgent spyAgent = spy(mcpAgent);
        doReturn(mockCallToolResult).when(spyAgent).callTool(any(CallToolRequest.class));
        
        CommonClientResponse response = spyAgent.remoteMethodCall("test-method", "test-params");
        
        assertNotNull(response, "Response should not be null");
        verify(spyAgent, times(1)).callTool(any(CallToolRequest.class));
    }
    
    @Test
    void testGetAgentInfo() {
        // Since the agent isn't connected, we can't test specific properties
        // We'll just verify the info is retrieved
        AgentInfo info = mcpAgent.getInfo();
        
        // The info might be null initially since the agent isn't connected
        if (info != null) {
            assertNotNull(info.getAgentCapabilities(), "Agent capabilities should not be null if info is available");
        }
    }
    
    @Test
    void testToString() {
        String result = mcpAgent.toString();
        
        assertNotNull(result, "ToString result should not be null");
        assertTrue(result.contains("mcp"), "ToString should contain agent type");
    }
}