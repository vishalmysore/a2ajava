package io.github.vishalmysore.mcp.domain;

import java.util.List;

/**
 * A JSON-RPC batch request, as described in https://www.jsonrpc.org/specification#batch.
 */
public class JSONRPCBatchRequest {
    private List<Object> jsonRpcRequests;  // Can be JSONRPCRequest or JSONRPCNotification. Use Object

    public List<Object> getJsonRpcRequests() {
        return jsonRpcRequests;
    }

    public void setJsonRpcRequests(List<Object> jsonRpcRequests) {
        this.jsonRpcRequests = jsonRpcRequests;
    }
}
