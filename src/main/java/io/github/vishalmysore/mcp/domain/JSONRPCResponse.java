package io.github.vishalmysore.mcp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A successful (non-error) response to a request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JSONRPCResponse extends JSONRPCMessage {
    private String id;
    private final String jsonrpc = "2.0";
    private Result result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}