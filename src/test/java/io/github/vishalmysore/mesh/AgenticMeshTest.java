package io.github.vishalmysore.mesh;

import io.github.vishalmysore.common.CommonClientResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AgenticMeshTest {
    
    private AgenticMesh agenticMesh;
    
    @Mock
    private AgentCatalog mockCatalog;
    
    @Mock
    private CommonClientResponse mockResponse;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        agenticMesh = new AgenticMesh(mockCatalog);
    }
    
    @Test
    void testPipeLineMeshWithNullPreviousResponse() {
        // Arrange
        String query = "test query";
        when(mockCatalog.processQuery(query)).thenReturn(mockResponse);
        when(mockResponse.getTextResult()).thenReturn("response text");
        
        // Act
        CommonClientResponse result = agenticMesh.pipeLineMesh(query);
        
        // Assert
        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(mockCatalog).processQuery(query);
    }
    
    @Test
    void testHubAndSpokeWithValidQuery() {
        // Arrange
        String query = "test hub query";
        when(mockCatalog.processQuery(anyString())).thenReturn(mockResponse);
        when(mockResponse.getTextResult()).thenReturn("hub response");
        
        // Act
        CommonClientResponse result = agenticMesh.hubAndSpoke(query);
        
        // Assert
        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(mockCatalog, atLeastOnce()).processQuery(anyString());
    }
    
    @Test
    void testBlackboardWithValidQuery() {
        // Arrange
        String query = "test blackboard query";
        when(mockCatalog.processQuery(anyString())).thenReturn(mockResponse);
        when(mockResponse.getTextResult()).thenReturn("blackboard response");
        
        // Act
        CommonClientResponse result = agenticMesh.blackboard(query);
        
        // Assert
        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(mockCatalog, atLeastOnce()).processQuery(anyString());
    }
    
    @Test
    void testPipeLineMeshWithNullResponse() {
        // Arrange
        String query = "test query";
        when(mockCatalog.processQuery(query)).thenReturn(null);
        
        // Act
        CommonClientResponse result = agenticMesh.pipeLineMesh(query);
        
        // Assert
        assertNull(result);
        verify(mockCatalog).processQuery(query);
    }
    
    @Test
    void testBlackboardWithNullInitialResponse() {
        // Arrange
        String query = "test query";
        when(mockCatalog.processQuery(query)).thenReturn(null);
        
        // Act
        CommonClientResponse result = agenticMesh.blackboard(query);
        
        // Assert
        assertNull(result);
        verify(mockCatalog).processQuery(query);
    }
    
    @Test
    void testHubAndSpokeWithNullResponse() {
        // Arrange
        String query = "test query";
        when(mockCatalog.processQuery(query)).thenReturn(null);
        
        // Act
        CommonClientResponse result = agenticMesh.hubAndSpoke(query);
        
        // Assert
        assertNull(result);
        verify(mockCatalog).processQuery(query);
    }
    
    @Test
    void testPipeLineMeshWithRecursiveCall() {
        // Arrange
        String query = "test recursive query";
        when(mockCatalog.processQuery(anyString())).thenReturn(mockResponse);
        when(mockResponse.getTextResult()).thenReturn("No"); // Simulate a "No" response to force recursion
        
        // Act
        CommonClientResponse result = agenticMesh.pipeLineMesh(query);
        
        // Assert
        assertNotNull(result);
        verify(mockCatalog, atLeast(1)).processQuery(anyString());
    }
}