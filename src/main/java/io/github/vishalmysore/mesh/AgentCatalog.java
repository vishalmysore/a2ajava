package io.github.vishalmysore.mesh;


import com.t4a.JsonUtils;
import com.t4a.predict.PredictionLoader;
import com.t4a.processor.AIProcessingException;
import com.t4a.transform.PromptTransformer;
import io.github.vishalmysore.a2a.client.A2AAgent;
import io.github.vishalmysore.common.Agent;
import io.github.vishalmysore.common.AgentIdentity;
import io.github.vishalmysore.common.AgentInfo;
import io.github.vishalmysore.common.CommonClientResponse;
import io.github.vishalmysore.mcp.client.MCPAgent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AgentCatalog is a class that manages a collection of agents, allowing for adding, retrieving, and processing queries through these agents.
 * It supports both A2A and MCP agents, providing a unified interface for interacting with them.
 */
@Getter
@Setter
@Log
public class AgentCatalog {
    private Map<AgentIdentity,Agent> agents;
    private PromptTransformer promptTransformer;
    JsonUtils jsonUtils = new JsonUtils();

    public AgentCatalog(){
        log.info("Initializing AgentCatalog");
        agents = new HashMap<>();
        promptTransformer = PredictionLoader.getInstance().createOrGetPromptTransformer();
    }

    public Agent addAgent(String url) {
        Agent a2agent = null;
        try {
            a2agent = new A2AAgent();
            a2agent.connect(url);
        } catch (Exception e) {
            log.warning("Failed to connect to A2A agent at " + url + ": " + e.getMessage()+" will add as mcp agent");
        }
        if((a2agent ==null)||(!a2agent.isConnected())){
            try {
                a2agent = new MCPAgent();
                a2agent.connect(url);
            } catch (Exception e) {
                log.warning("Failed to connect to MCP agent at " + url + ": " + e.getMessage());
            }
        }
        if((a2agent ==null)||(!a2agent.isConnected())) {
            log.warning("Not able to Connect to agent at " + url);
        } else {
            AgentIdentity a2agentIdentity = AgentIdentity.builder().info(a2agent.getInfo()).url(url).build();
            agents.put(a2agentIdentity, a2agent);
            log.info("Connected to agent at " + url + " with info: " + a2agent.getInfo());
        }
        return a2agent;
    }

    /**
     * Get a comma-separated list of all agent info
     * @return String containing agent information, empty string if no agents
     */
    public String getAgentsInfo() {
        if (agents.isEmpty()) {
            return "";
        }
        return agents.keySet().stream()
                .map(AgentIdentity::toString)
                .collect(Collectors.joining(", "));
    }

    /**
     * Add an agent directly to the catalog
     * @param agent The agent to add
     * @return true if added successfully, false if agent was null or couldn't be added
     */
    public boolean addAgent(Agent agent) {
        if (agent == null) {
            log.warning("Attempted to add null agent");
            return false;
        }

        AgentInfo info = agent.getInfo();
        if (info == null) {
            log.warning("Agent has null info, cannot add to catalog");
            return false;
        }
        AgentIdentity a2agentIdentity = AgentIdentity.builder().info(agent.getInfo()).build();
        agents.put(a2agentIdentity, agent);
        log.info("Added agent with info: " + info);
        return true;
    }

    /**
     * Retrieve an agent by its info
     * @param info The AgentInfo to look up
     * @return The agent if found, null otherwise
     */
    public Agent retrieveAgent(AgentInfo info) {
        if (info == null) {
            log.warning("Attempted to retrieve agent with null info");
            return null;
        }
        
        // Find agent by matching info
        return agents.entrySet().stream()
            .filter(entry -> info.equals(entry.getKey().getAllTheCapabilitiesOfTheAgent()))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(null);
    }

    public Agent retrieveAgentByIdentity(AgentIdentity identity) {
        if (identity == null) {
            log.warning("Attempted to retrieve agent with null identity");
            return null;
        }

        // Find agent by matching identity
        return agents.get(identity);
    }

    public CommonClientResponse processQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            log.warning("Empty or null query provided");
            return null;
        }

        // Process the query using the prompt transformer
        String identiy = null;
        String uniqueIdStr = "agentUniqueIDTobeUsedToIdentifyTheAgent";
        try {
            identiy = promptTransformer.transformIntoJson(jsonUtils.createJson(uniqueIdStr).toString()," this is user prompt { "+query+"}  I am trying to find which agent can handle it from this info {"+getAgentsInfo()+"}");
        } catch (AIProcessingException e) {
            log.severe("Error transforming query into JSON: " + e.getMessage());
        }
        log.info("Agent Identity: " + identiy);
        Agent agent = retrieveAgentByID(jsonUtils.getFieldValue(identiy, uniqueIdStr));
        log.info("Retrieved Agent: " + agent.getType());
        CommonClientResponse response = agent.remoteMethodCall(query);
        return response;
    }


    public Agent retrieveAgentByID(String agentId) {

        try {

            log.info("Retrieving agent by ID: " + agentId);
            // Create an AgentIdentity to use as map key
            return agents.entrySet().stream()
                .filter(entry -> agentId.equals(entry.getKey().getAgentUniqueIDTobeUsedToIdentifyTheAgent()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
                
        } catch (Exception e) {
            log.warning("Error parsing JSON or retrieving agent: " + e.getMessage());
            return null;
        }
    }
}
