package io.github.vishalmysore.mcp.domain;

import java.util.List;

/**
 * A JSON-RPC batch response, as described in https://www.jsonrpc.org/specification#batch.
 */
public class JSONRPCBatchResponse {
    private List<Object> jsonRpcResponses; // Can be JSONRPCResponse or JSONRPCError. Use Object

    public List<Object> getJsonRpcResponses() {
        return jsonRpcResponses;
    }

    public void setJsonRpcResponses(List<Object> jsonRpcResponses) {
        this.jsonRpcResponses = jsonRpcResponses;
    }
}
