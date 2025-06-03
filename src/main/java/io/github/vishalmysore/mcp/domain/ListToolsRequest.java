package io.github.vishalmysore.mcp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Sent from the client to request a list of tools the server provides.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListToolsRequest extends ClientRequest {
    private final String method = "tools/list";
    private String jsonrpc ="2.0";
    private Object params;
    private Object id;


}