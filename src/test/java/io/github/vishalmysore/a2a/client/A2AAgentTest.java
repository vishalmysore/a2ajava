package io.github.vishalmysore.a2a.client;

import io.github.vishalmysore.a2a.domain.*;
import io.github.vishalmysore.common.AgentInfo;
import io.github.vishalmysore.common.CommonClientResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class A2AAgentTest {

    private A2AAgent a2aAgent;
    private RestTemplate mockRestTemplate;
    private HttpURLConnection mockConnection;

    @BeforeEach
    void setUp() {
        a2aAgent = new A2AAgent();
        mockRestTemplate = mock(RestTemplate.class);
        mockConnection = mock(HttpURLConnection.class);
    }
    
    @Test
    void testGetType() {
        assertEquals("a2a", a2aAgent.getType());
    }
    
    @Test
    void testRemoteMethodCall() throws Exception {
        // Setup
        String query = "Test query";
        SendTaskResponse mockResponse = new SendTaskResponse();
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        mockResponse.setResult(task);
        
        // Use MockedStatic for RestTemplate
        try (MockedStatic<UUID> mockedUuid = mockStatic(UUID.class)) {
            UUID mockUuid = mock(UUID.class);
            when(mockUuid.toString()).thenReturn("test-uuid");
            mockedUuid.when(UUID::randomUUID).thenReturn(mockUuid);
            
            // Setup the URL
            a2aAgent.connect("http://test-url/.well-known/agent.json", null);
            
            // Mock RestTemplate
            mockRestTemplate = mock(RestTemplate.class);
            when(mockRestTemplate.postForEntity(
                    anyString(),
                    any(),
                    eq(SendTaskResponse.class)
            )).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
            
            // Use reflection to replace the RestTemplate with our mock
            // (can't directly set it since A2AAgent creates a new instance each call)
            try (MockedStatic<RestTemplate> mockedRestTemplate = mockStatic(RestTemplate.class)) {
                mockedRestTemplate.when(RestTemplate::new).thenReturn(mockRestTemplate);
                
                // Execute
                CommonClientResponse response = a2aAgent.remoteMethodCall(query);
                
                // Verify
                assertNotNull(response);
                assertEquals(mockResponse, response);
            }
        }
    }
    
    @Test
    void testRemoteMethodCallWithName() throws Exception {
        // Setup
        String query = "Test query";
        String methodName = "test-method";
        SendTaskResponse mockResponse = new SendTaskResponse();
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        mockResponse.setResult(task);
        
        // Use MockedStatic for RestTemplate
        try (MockedStatic<UUID> mockedUuid = mockStatic(UUID.class)) {
            UUID mockUuid = mock(UUID.class);
            when(mockUuid.toString()).thenReturn("test-uuid");
            mockedUuid.when(UUID::randomUUID).thenReturn(mockUuid);
            
            // Setup the URL
            a2aAgent.connect("http://test-url/.well-known/agent.json", null);
            
            // Mock RestTemplate
            mockRestTemplate = mock(RestTemplate.class);
            when(mockRestTemplate.postForEntity(
                    anyString(),
                    any(),
                    eq(SendTaskResponse.class)
            )).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
            
            // Use reflection to replace the RestTemplate with our mock
            try (MockedStatic<RestTemplate> mockedRestTemplate = mockStatic(RestTemplate.class)) {
                mockedRestTemplate.when(RestTemplate::new).thenReturn(mockRestTemplate);
                
                // Execute
                CommonClientResponse response = a2aAgent.remoteMethodCall(methodName, query);
                
                // Verify
                assertNotNull(response);
                assertEquals(mockResponse, response);
            }
        }
    }
    
    @Test
    void testConnectAndDisconnect() throws Exception {
        // Setup for connect
        String agentCardJson = "{\"name\":\"Test Agent\",\"description\":\"Test Description\",\"agent_type\":\"a2a\"}";
        
        // Mock URL and connection
        URL mockUrl = mock(URL.class);
        try (MockedStatic<URL> mockedUrl = mockStatic(URL.class)) {
            mockedUrl.when(() -> new URL(anyString())).thenReturn(mockUrl);
            when(mockUrl.openConnection()).thenReturn(mockConnection);
            when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(agentCardJson.getBytes()));
            when(mockConnection.getResponseCode()).thenReturn(200);
            
            // Execute connect
            a2aAgent.connect("http://test-url/.well-known/agent.json", null);
            
            // Verify connect
            assertTrue(a2aAgent.isConnected());
            AgentCard agentCard = (AgentCard) a2aAgent.getInfo();
            assertEquals("Test Agent", agentCard.getName());
            assertEquals("Test Description", agentCard.getDescription());
            
            // Execute disconnect
            a2aAgent.disconnect();
            
            // Verify disconnect
            assertFalse(a2aAgent.isConnected());
            assertNull(a2aAgent.getInfo());
        }
    }
    
    @Test
    void testConnectWithNonJsonUrl() throws Exception {
        // Setup for connect with URL not ending in .json
        String agentCardJson = "{\"name\":\"Test Agent\",\"description\":\"Test Description\",\"agent_type\":\"a2a\"}";
        
        // Mock URL and connection
        URL mockUrl = mock(URL.class);
        try (MockedStatic<URL> mockedUrl = mockStatic(URL.class)) {
            mockedUrl.when(() -> new URL(anyString())).thenReturn(mockUrl);
            when(mockUrl.openConnection()).thenReturn(mockConnection);
            when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(agentCardJson.getBytes()));
            when(mockConnection.getResponseCode()).thenReturn(200);
            
            // Execute connect with URL ending with /
            a2aAgent.connect("http://test-url/", null);
            
            // Verify connect
            assertTrue(a2aAgent.isConnected());
            
            // Execute connect with URL not ending with /
            a2aAgent.connect("http://test-url", null);
            
            // Verify connect
            assertTrue(a2aAgent.isConnected());
        }
    }
    
    @Test
    void testConnectFailureInvalidUrl() {
        // Test with invalid URL (not containing .well-known)
        assertThrows(IllegalArgumentException.class, () -> 
            a2aAgent.connect("http://test-url/invalid.json", null)
        );
    }
    
    @Test
    void testConnectFailureHttp() throws Exception {
        // Setup for HTTP failure
        URL mockUrl = mock(URL.class);
        try (MockedStatic<URL> mockedUrl = mockStatic(URL.class)) {
            mockedUrl.when(() -> new URL(anyString())).thenReturn(mockUrl);
            when(mockUrl.openConnection()).thenReturn(mockConnection);
            when(mockConnection.getResponseCode()).thenReturn(404);
            
            // Execute and verify
            assertThrows(RuntimeException.class, () -> 
                a2aAgent.connect("http://test-url/.well-known/agent.json", null)
            );
        }
    }
    
    @Test
    void testConnectFailureIO() throws Exception {
        // Setup for IO Exception
        URL mockUrl = mock(URL.class);
        try (MockedStatic<URL> mockedUrl = mockStatic(URL.class)) {
            mockedUrl.when(() -> new URL(anyString())).thenReturn(mockUrl);
            when(mockUrl.openConnection()).thenThrow(new IOException("Test IO Exception"));
            
            // Execute and verify
            assertThrows(RuntimeException.class, () -> 
                a2aAgent.connect("http://test-url/.well-known/agent.json", null)
            );
        }
    }
    
    @Test
    void testGetterMethods() throws Exception {
        // Setup
        String agentCardJson = "{\"name\":\"Test Agent\",\"description\":\"Test Description\",\"agent_type\":\"a2a\"}";
        
        // Mock URL and connection
        URL mockUrl = mock(URL.class);
        try (MockedStatic<URL> mockedUrl = mockStatic(URL.class)) {
            mockedUrl.when(() -> new URL(anyString())).thenReturn(mockUrl);
            when(mockUrl.openConnection()).thenReturn(mockConnection);
            when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(agentCardJson.getBytes()));
            when(mockConnection.getResponseCode()).thenReturn(200);
            
            // Connect
            a2aAgent.connect("http://test-url/.well-known/agent.json", null);
            
            // Test getters
            assertEquals(mockUrl, a2aAgent.getServerUrl());
            assertNotNull(a2aAgent.getMapper());
            AgentInfo info = a2aAgent.getInfo();
            assertNotNull(info);
            AgentCard agentCard = (AgentCard) info;
            assertEquals("Test Agent", agentCard.getName());
        }
    }
}