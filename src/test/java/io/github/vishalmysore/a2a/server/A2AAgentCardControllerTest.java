package io.github.vishalmysore.a2a.server;

import com.t4a.api.GenericJavaMethodAction;
import com.t4a.predict.PredictionLoader;
import com.t4a.processor.AIProcessingException;
import com.t4a.transform.PromptTransformer;
import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.a2a.domain.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class A2AAgentCardControllerTest {

    @Mock
    private PredictionLoader predictionLoader;
    
    @Mock
    private PromptTransformer promptTransformer;
    
    @Mock
    private GenericJavaMethodAction action;
    
    private A2AAgentCardController controller;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Create a simple implementation of the interface for testing
        controller = new A2AAgentCardController() {
            @Override
            public ResponseEntity<AgentCard> getAgentCard() {
                return ResponseEntity.ok(new AgentCard());
            }
            
            @Override
            public PromptTransformer getPromptTransformer() {
                return promptTransformer;
            }
        };
    }
    
    @Test
    void testGetAgentCard() {
        // Act
        ResponseEntity<AgentCard> response = controller.getAgentCard();
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }
    
    @Test
    void testGetSkill() throws AIProcessingException {
        // Arrange
        Skill expectedSkill = new Skill();
        expectedSkill.setName("TestSkill");
        
        try (MockedStatic<PredictionLoader> mockedStatic = mockStatic(PredictionLoader.class)) {
            mockedStatic.when(PredictionLoader::getInstance).thenReturn(predictionLoader);
            when(predictionLoader.getAiAction("testAction")).thenReturn(action);
            when(action.getActionName()).thenReturn("TestSkill");
            when(action.getDescription()).thenReturn("Test Description");
            when(action.getJsonRPC()).thenReturn("{ \"parameters\": {} }");
            when(promptTransformer.transformIntoPojo(anyString(), eq(Skill.class))).thenReturn(expectedSkill);
            
            // Act
            Skill result = controller.getSkill("testAction");
            
            // Assert
            assertNotNull(result);
            assertEquals(expectedSkill, result);
            verify(promptTransformer).transformIntoPojo(contains("TestSkill"), eq(Skill.class));
        }
    }
    
    @Test
    void testGetSkillThrowsException() throws AIProcessingException {
        try (MockedStatic<PredictionLoader> mockedStatic = mockStatic(PredictionLoader.class)) {
            mockedStatic.when(PredictionLoader::getInstance).thenReturn(predictionLoader);
            when(predictionLoader.getAiAction("testAction")).thenReturn(action);
            when(promptTransformer.transformIntoPojo(anyString(), eq(Skill.class)))
                .thenThrow(new AIProcessingException("Test exception"));
            
            // Act & Assert
            assertThrows(AIProcessingException.class, () -> controller.getSkill("testAction"));
        }
    }
}