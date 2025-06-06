package io.github.vishalmysore.common;

import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.mcp.domain.CallToolResult;
import io.github.vishalmysore.mcp.domain.ListToolsResult;

//marker interface for agent information
public interface AgentInfo {

    default String getAgentCapabilities() {
        String capabilities = "Capabilities: ";
        if (this instanceof AgentCard) {
            capabilities = ((AgentCard) this).getCapabilities().toString();
        } else if (this instanceof ListToolsResult) {
            capabilities = ((ListToolsResult) this).getTools().toString();
        }
        return capabilities;
    }

}
