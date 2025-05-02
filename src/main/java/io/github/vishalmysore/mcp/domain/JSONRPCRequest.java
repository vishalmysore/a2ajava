package io.github.vishalmysore.mcp.domain;

/**
 * A request that expects a response.
 */
public class JSONRPCRequest extends JSONRPCMessage {
    private String id;
    private final String jsonrpc = "2.0";
    private String method;
    private Params params;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
         * This parameter name is reserved by MCP to allow clients and servers to attach additional metadata to their requests.
         */
        private Meta _meta;

        public Meta getMeta() {
            return _meta;
        }

        public void setMeta(Meta meta) {
            this._meta = meta;
        }

        public static class Meta {
            /**
             * If specified, the caller is requesting out-of-band progress notifications for this request
             * (as represented by notifications/progress). The value of this parameter is an opaque token
             * that will be attached to any subsequent notifications. The receiver is not obligated to provide these notifications.
             */
            private String progressToken;  // Changed from ProgressToken to String

            public String getProgressToken() {
                return progressToken;
            }

            public void setProgressToken(String progressToken) {
                this.progressToken = progressToken;
            }
        }
    }
}