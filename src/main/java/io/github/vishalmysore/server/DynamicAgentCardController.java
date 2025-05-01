package io.github.vishalmysore.server;

import com.t4a.annotations.Agent;
import com.t4a.api.AIAction;
import com.t4a.api.GroupInfo;
import com.t4a.predict.PredictionLoader;
import com.t4a.processor.AIProcessingException;
import com.t4a.transform.GeminiV2PromptTransformer;
import com.t4a.transform.PromptTransformer;
import io.github.vishalmysore.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This controller serves the dynamic agent card for the TicketQueen agent.
 * It provides information about the agent's capabilities, skills, and actions.
 * Generates the agent card dynamically based on the available actions and groups rather than
 * hardcoding it.
 */
@RestController
@RequestMapping("/.well-known")
public class DynamicAgentCardController implements A2AAgentCardController {
    @GetMapping(value = "/dynaagent.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentCard> getAgentCard() {



        AgentCard agentCard = new AgentCard();
        agentCard.setName("TicketQueen : Ticket Booking Agent");
        Map<String, AIAction> actions = PredictionLoader.getInstance().getPredictions();
        StringBuilder description = new StringBuilder("This agent can help you with: ");
        actions.values().forEach(action -> {
            description.append(action.getActionName())
                    .append(" - ")
                    .append(action.getDescription())
                    .append(", ");
        });

        // Remove trailing comma and space
        if (description.length() > 2) {
            description.setLength(description.length() - 2);
        }

        agentCard.setDescription(description.toString());

      //  agentCard.setDescription("book your airlines ticket, hotel ticket, and train ticket , also find out the preference of food of a person, it will also provide details on your existing booking or help in cancelling it or provideing realt time update ");
        agentCard.setUrl("http://localhost:8080"); //  Replace with actual URL
        agentCard.setProvider(new Provider("Ticket Corp", "https://github.com/vishalmysore/choturobo"));
        agentCard.setVersion("1.0.0");
        agentCard.setDocumentationUrl("https://github.com/vishalmysore/Tools4AI");  // Replace
        agentCard.setCapabilities(new Capabilities(false, false, false));
        agentCard.setAuthentication(new Authentication(new String[]{"Bearer"}));
        agentCard.setDefaultInputModes(new String[]{"text/plain"});
        agentCard.setDefaultOutputModes(new String[]{"application/json"});

        Map<GroupInfo, String> groupActions = PredictionLoader.getInstance().getActionGroupList().getGroupActions();
        List<Skill> skills = new ArrayList<>();

        for (Map.Entry<GroupInfo, String> entry : groupActions.entrySet()) {
            GroupInfo groupInfo = entry.getKey();
            String actionTags = entry.getValue();

            Skill skill = new Skill();
            skill.setId(groupInfo.getGroupName().toLowerCase().replace(" ", "-"));
            skill.setName(groupInfo.getGroupName());
            skill.setDescription(groupInfo.getGroupDescription());

            // Convert action names to tags
            String[] tags = actionTags != null ?
                    actionTags.toLowerCase().split(",") :
                    new String[]{groupInfo.getGroupName().toLowerCase()};

            skill.setTags(tags);
            skill.setExamples(new String[]{"Example for " + groupInfo.getGroupName()});
            skill.setInputModes(new String[]{"application/json"});
            skill.setOutputModes(new String[]{"application/json"});
            skills.add(skill);
        }

        agentCard.setSkills(List.of(skills.toArray(new Skill[0])));





        return ResponseEntity.ok(agentCard);
    }
}
