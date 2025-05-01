package io.github.vishalmysore.server;

import com.t4a.api.GroupInfo;
import com.t4a.predict.PredictionLoader;
import com.t4a.processor.AIProcessingException;
import com.t4a.transform.GeminiV2PromptTransformer;
import com.t4a.transform.PromptTransformer;
import io.github.vishalmysore.domain.AgentCard;
import io.github.vishalmysore.domain.Skill;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * This controller serves the real-time agent card for the TicketQueen agent.
 * It provides information about the agent's capabilities, skills, and actions.
 * Generates the agent card dynamically based on the available actions and groups rather than
 * hardcoding it.
 * In real applicaiton you can extend his class and call the super class
 */
@RestController
@RequestMapping("/.example-known")
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

    @GetMapping(value = "/rtagent.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentCard> getAgentCard() {
        return ResponseEntity.ok(cachedAgentCard);
    }
}
