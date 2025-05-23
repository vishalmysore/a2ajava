package io.github.vishalmysore.a2a.server;

import com.t4a.annotations.Agent;
import com.t4a.api.AIAction;
import com.t4a.api.GenericJavaMethodAction;
import com.t4a.api.GroupInfo;
import com.t4a.predict.PredictionLoader;
import com.t4a.processor.AIProcessingException;
import com.t4a.transform.GeminiV2PromptTransformer;
import com.t4a.transform.OpenAIPromptTransformer;
import com.t4a.transform.PromptTransformer;
import io.github.vishalmysore.a2a.domain.AgentCard;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

/**
 * This controller serves the real-time agent card for the TicketQueen agent.
 * It provides information about the agent's capabilities, skills, and actions.
 * Generates the agent card dynamically based on the available actions and groups rather than
 * hardcoding it.
 * In real applicaiton you can extend his class and call the super class
 */

@Log
public class RealTimeAgentCardController implements A2AAgentCardController {

    private PromptTransformer promptTransformer;


    public static final String WELL_KNOWN_PATH = "/.well-known/";
    public static final String AGENT_PATH ="/agent.json";

    @Getter
    private AgentCard cachedAgentCard;
    @Value("${server.port:8080}")
    private String serverPort;

    public RealTimeAgentCardController() {
        promptTransformer = new GeminiV2PromptTransformer();
    }

    public RealTimeAgentCardController(ApplicationContext context) {
        PredictionLoader.getInstance(context);
        promptTransformer = new GeminiV2PromptTransformer();
    }
    public boolean isMethodAllowed(Method method)    {
        return true;
    }
    @Override
    public PromptTransformer getPromptTransformer() {
        return promptTransformer;
    }

    @PostConstruct
    public void init() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("tools4ai.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find tools4ai.properties");
            }
            properties.load(input);

            String provider = properties.getProperty("agent.provider");
            if ("openai".equals(provider)) {
                promptTransformer = new OpenAIPromptTransformer();
            } else if ("gemini".equals(provider)) {
                promptTransformer = new GeminiV2PromptTransformer();
            } else {
                log.info("Provider not found defaulting to Gemini");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file", e);
        }
        Map<GroupInfo, String> groupActions = PredictionLoader.getInstance().getActionGroupList().getGroupActions();
        Map<String, AIAction> predictions = PredictionLoader.getInstance().getPredictions();
        StringBuilder realTimeDescription = new StringBuilder("This agent provides the following capabilities: ");

        for (Map.Entry<GroupInfo, String> entry : groupActions.entrySet()) {
            GroupInfo group = entry.getKey();
            String[] actionNames = entry.getValue().split(",");
            StringBuilder methodNames = new StringBuilder();

            for (String actionName : actionNames) {
                AIAction action = predictions.get(actionName.trim());
                if (action instanceof GenericJavaMethodAction methodAction) {
                    Method m = methodAction.getActionMethod();
                    if (isMethodAllowed(m)) {
                        methodNames.append(",");
                        methodNames.append(actionName.trim());
                    }
                }
            }
            realTimeDescription.append(group.getGroupName())
                    .append(" (")
                    .append(group.getGroupDescription())
                    .append("), with actions: ")
                    .append(methodNames)
                    .append("; ");
        }

        if (realTimeDescription.length() > 2) {
            realTimeDescription.setLength(realTimeDescription.length() - 2);
        }

        String finalDescription = realTimeDescription.toString();

        try {
            if(groupActions.isEmpty()) {
                log.warning("No actions found for the agent card");
                AgentCard card = new AgentCard();
                storeCard(card);

            } else {
                AgentCard card = (AgentCard) promptTransformer.transformIntoPojo(
                        "use this description and also populate skills in detail " + finalDescription,
                        AgentCard.class);
                  storeCard(card);

            }
            String hostName = InetAddress.getLocalHost().getHostName();
            this.cachedAgentCard.setUrl("http://" + hostName + ":" + serverPort);
        } catch (AIProcessingException e) {
            log.severe("The skills are not populate in the agent card as actions are more in number \n you can either try with different processor \n" +
                    " or you can populate skills individually and add to agent card ");
        } catch (UnknownHostException e) {
            log.warning("host not knwon set the url manually card.setUrl");
        }
    }


   public void storeCard(AgentCard card) {
        this.cachedAgentCard = card;
    }

    public ResponseEntity<AgentCard> getAgentCard() {
        return ResponseEntity.ok(cachedAgentCard);
    }
}
