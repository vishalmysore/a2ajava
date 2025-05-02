package io.github.vishalmysore.mcp.domain;

import java.util.Map;

/**
 * A notification which does not expect a response.
 */
public class JSONRPCNotification extends JSONRPCMessage {
    private final String jsonrpc = "2.0";
    private String method;
    private Params params;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public static class Params {
        /**
         * This parameter name is reserved by MCP to allow clients and servers to attach additional metadata to their notifications.
         */
        private Map<String, Object> _meta;

        public Map<String, Object> getMeta() {
            return _meta;
        }

        public void setMeta(Map<String, Object> meta) {
            this._meta = meta;
        }
    }
}