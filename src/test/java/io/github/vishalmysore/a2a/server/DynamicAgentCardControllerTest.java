package io.github.vishalmysore.a2a.server;

import com.t4a.api.AIAction;
import com.t4a.api.GroupInfo;
import com.t4a.predict.PredictionLoader;
import com.t4a.transform.PromptTransformer;
import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.a2a.domain.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DynamicAgentCardControllerTest {
    
    @Mock
    private PredictionLoader predictionLoader;
    
    @Mock
    private AIAction action1;
    
    @Mock
    private AIAction action2;
    
    private DynamicAgentCardController controller;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new DynamicAgentCardController();
    }
    
    @Test
    void testGetPromptTransformer() {
        // Act
        PromptTransformer transformer = controller.getPromptTransformer();
        
        // Assert
        assertNotNull(transformer);
    }
    
    @Test
    void testGetAgentCard() {
        // Arrange
        Map<String, AIAction> actions = new HashMap<>();
        actions.put("action1", action1);
        actions.put("action2", action2);
        
        // Set up mock actions
        when(action1.getActionName()).thenReturn("BookFlight");
        when(action1.getDescription()).thenReturn("Book a flight");
        when(action2.getActionName()).thenReturn("CancelFlight");
        when(action2.getDescription()).thenReturn("Cancel a flight");
        
        // Set up mock group actions
        Map<GroupInfo, String> groupActions = new HashMap<>();
        GroupInfo groupInfo1 = new GroupInfo("Flight Booking", "Book and manage flights");
        groupActions.put(groupInfo1, "BookFlight,CancelFlight");
        
        try (MockedStatic<PredictionLoader> mockedStatic = mockStatic(PredictionLoader.class)) {
            mockedStatic.when(PredictionLoader::getInstance).thenReturn(predictionLoader);
            when(predictionLoader.getPredictions()).thenReturn(actions);
            
            // Instead of mocking getActionGroupList().getGroupActions(), directly mock getGroupActions
            // This avoids issues with the ActionGroupList type which we can't easily mock
            when(predictionLoader.getActionGroupList()).thenAnswer(invocation -> {
                // Create a proxy object that returns groupActions when getGroupActions() is called
                return new Object() {
                    public Map<GroupInfo, String> getGroupActions() {
                        return groupActions;
                    }
                };
            });
            
            // Act
            ResponseEntity<AgentCard> response = controller.getAgentCard();
            
            // Assert
            assertNotNull(response);
            assertEquals(200, response.getStatusCode().value());
            
            AgentCard card = response.getBody();
            assertNotNull(card);
            assertEquals("TicketQueen : Ticket Booking Agent", card.getName());
            assertTrue(card.getDescription().contains("BookFlight - Book a flight"));
            assertTrue(card.getDescription().contains("CancelFlight - Cancel a flight"));
            
            // Verify skills
            List<Skill> skills = card.getSkills();
            assertNotNull(skills);
            assertFalse(skills.isEmpty());
            
            // Find the Flight Booking skill
            boolean foundFlightSkill = false;
            for (Skill skill : skills) {
                if (skill.getName().equals("Flight Booking")) {
                    foundFlightSkill = true;
                    assertEquals("flight-booking", skill.getId());
                    assertEquals("Book and manage flights", skill.getDescription());
                    assertArrayEquals(new String[]{"bookflight", "cancelflight"}, skill.getTags());
                    break;
                }
            }
            assertTrue(foundFlightSkill, "Flight Booking skill not found");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
}