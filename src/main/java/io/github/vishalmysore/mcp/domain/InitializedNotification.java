package io.github.vishalmysore.mcp.domain;

import java.util.Map;

/**
 * This notification is sent from the client to the server after initialization has finished.
 */
public class InitializedNotification extends ClientNotification {
    private final String method = "notifications/initialized";
    private Params params;

    public String getMethod() {
        return method;
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