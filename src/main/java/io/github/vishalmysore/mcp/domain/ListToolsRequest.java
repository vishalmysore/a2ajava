package io.github.vishalmysore.mcp.domain;

/**
 * Sent from the client to request a list of tools the server provides.
 */
public class ListToolsRequest extends ClientRequest {
    private final String method = "tools/list";
}