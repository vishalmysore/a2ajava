package io.github.vishalmysore.a2a.server;

import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.a2a.domain.Artifact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DynamicAgentCardController for testing.
 * This implementation is separate from the main DynamicAgentCardController
 * and is specifically for unit testing with the ActionProvider.
 */
public class TestDynamicAgentCardController {
    
    private ActionProvider actionProvider;
    
    public TestDynamicAgentCardController(ActionProvider actionProvider) {
        this.actionProvider = actionProvider;
    }
    
    /**
     * Gets an agent card for the specified agent.
     * 
     * @param agentName The name of the agent.
     * @return The agent card with actions.
     */
    public AgentCard getAgentCard(String agentName) {
        AgentCard agentCard = new AgentCard();
        agentCard.setName("Test Agent");
        
        Object actionListObj = actionProvider.getActionList();
        List<Artifact> actions = new ArrayList<>();
        
        // Convert action list items to Artifacts
        if (actionListObj instanceof List) {
            List<?> actionList = (List<?>) actionListObj;
            for (Object action : actionList) {
                if (action instanceof Map) {
                    Map<?, ?> actionMap = (Map<?, ?>) action;
                    Artifact artifact = new Artifact();
                    artifact.setName(actionMap.get("title").toString());
                    artifact.setDescription(actionMap.get("description").toString());
                    actions.add(artifact);
                }
            }
        }
        
        return agentCard;
    }
}