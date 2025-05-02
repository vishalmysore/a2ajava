package io.github.vishalmysore.a2a.server;

import com.t4a.api.GroupInfo;
import com.t4a.predict.PredictionLoader;
import com.t4a.processor.AIProcessingException;
import com.t4a.transform.GeminiV2PromptTransformer;
import com.t4a.transform.PromptTransformer;
import io.github.vishalmysore.a2a.domain.AgentCard;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * This controller serves the real-time agent card for the TicketQueen agent.
 * It provides information about the agent's capabilities, skills, and actions.
 * Generates the agent card dynamically based on the available actions and groups rather than
 * hardcoding it.
 * In real applicaiton you can extend his class and call the super class
 */
@Service
public class RealTimeAgentCardController implements A2AAgentCardController {

    @Getter
    private AgentCard cachedAgentCard;

    @PostConstruct
    public void init() {
        Map<GroupInfo, String> groupActions = PredictionLoader.getInstance().getActionGroupList().getGroupActions();
        StringBuilder realTimeDescription = new StringBuilder("This agent provides the following capabilities: ");

        for (Map.Entry<GroupInfo, String> entry : groupActions.entrySet()) {
            GroupInfo group = entry.getKey();
            realTimeDescription.append(group.getGroupName())
                    .append(" (")
                    .append(group.getGroupDescription())
                    .append("), with actions: ")
                    .append(entry.getValue())
                    .append("; ");
        }

        if (realTimeDescription.length() > 2) {
            realTimeDescription.setLength(realTimeDescription.length() - 2);
        }

        String finalDescription = realTimeDescription.toString();
        PromptTransformer promptTransformer = new GeminiV2PromptTransformer();
        try {
            this.cachedAgentCard = (AgentCard) promptTransformer.transformIntoPojo(
                    "use this description and also populate skills in detail" + finalDescription,
                    AgentCard.class
            );
        } catch (AIProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public ResponseEntity<AgentCard> getAgentCard() {
        return ResponseEntity.ok(cachedAgentCard);
    }
}
