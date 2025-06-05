package io.github.vishalmysore.common;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.java.Log;

/**
 * Registry for all the agents and cards for both MCP and A2A
 * I'd like to propose a flexible enhancement to the A2A protocol to support client-specific agent experiences. Currently, AgentCard objects are generally served uniformly to all clients, regardless of the context or identity of the requesting client. In many real-world applications, however, different clients may need different views, capabilities, or even agent skillsets based on:
 *
 * Their identity (e.g., clientId)
 *
 * Their type (e.g., mobile vs. desktop, internal vs. external)
 *
 * Runtime context (e.g., device info, region, tenant)
 *
 * 💡 Proposal Summary
 * Introduce a ClientInfo object in the A2A request schema that allows clients to optionally identify themselves, enabling the server to tailor AgentCard responses accordingly.
 *
 * Example Schema:
 * json
 * Copy
 * Edit
 * "clientInfo": {
 *   "type": "object",
 *   "properties": {
 *     "clientId": { "type": "string" },
 *     "clientType": { "type": "string" },
 *     "deviceId": { "type": "string" },
 *     "tags": {
 *       "type": "array",
 *       "items": { "type": "string" }
 *     }
 *   },
 *   "required": ["clientId"]
 * }
 * Optionally, enhance AgentCard with:
 * json
 * Copy
 * Edit
 * "audience": {
 *   "type": "array",
 *   "items": { "type": "string" },
 *   "description": "List of client IDs or tags this card is intended for."
 * }
 * 🧠 Use Cases
 * Multi-tenant SaaS: Serve different capabilities to clients based on their subscription plan.
 *
 * Context-aware agents: Deliver simplified AgentCards to mobile clients and full-featured ones to desktop.
 *
 * Security filtering: Hide certain skills or capabilities for restricted clients or user roles.
 *
 * A/B testing: Dynamically adjust agent exposure based on experiment tags.
 *
 * ✅ Benefits
 * Backward-compatible: Clients that don’t provide clientInfo can continue receiving the default card.
 *
 * Enhances personalization and UX.
 *
 * Enables richer agent ecosystem logic (like context-specific cards or private/internal agent networks).
 */
@Log
public class ClientRegistryForAgents {
    private Map<AgentInfo, Agent> agents;

    public ClientRegistryForAgents() {
        this.agents = new HashMap<>();
    }

    /**
     * Add an agent to the registry
     * @param agent The agent to add
     * @return true if added successfully, false if agent was null or couldn't be added
     */
    public boolean addAgent(Agent agent) {
        if (agent == null) {
            log.warning("Attempted to add null agent to registry");
            return false;
        }

        AgentInfo info = agent.getInfo();
        if (info == null) {
            log.warning("Agent has null info, cannot add to registry");
            return false;
        }

        agents.put(info, agent);
        log.info("Added agent: " + info);
        return true;
    }

    /**
     * Get a comma-separated list of all agent info
     * @return String containing comma-separated agent info
     */
    public String getAgentsInfo() {
        if (agents.isEmpty()) {
            return "";
        }
        return agents.keySet().stream()
            .map(Object::toString)
            .collect(Collectors.joining(", "));
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
        return agents.get(info);
    }

    /**
     * Get the total number of registered agents
     * @return The number of agents in the registry
     */
    public int getAgentCount() {
        return agents.size();
    }

    /**
     * Check if an agent exists in the registry
     * @param info The AgentInfo to check
     * @return true if the agent exists, false otherwise
     */
    public boolean hasAgent(AgentInfo info) {
        if (info == null) {
            return false;
        }
        return agents.containsKey(info);
    }
}
