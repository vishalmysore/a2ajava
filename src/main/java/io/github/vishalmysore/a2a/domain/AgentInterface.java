package io.github.vishalmysore.a2a.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Declares a combination of a target URL and a transport protocol for interacting with the agent.
 * This allows agents to expose the same functionality over multiple protocol binding mechanisms.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentInterface {
    /**
     * The URL where this interface is available. Must be a valid absolute HTTPS URL in production.
     * Example: "https://api.example.com/a2a/v1", "https://grpc.example.com/a2a"
     */
    private String url;

    /**
     * The protocol binding supported at this URL.
     * Core supported values: "JSONRPC", "GRPC", "HTTP+JSON"
     */
    private String protocolBinding;

    /**
     * Optional tenant to be set in the request when calling the agent.
     */
    private String tenant;
}
