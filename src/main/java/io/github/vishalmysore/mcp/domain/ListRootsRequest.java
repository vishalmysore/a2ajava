package io.github.vishalmysore.mcp.domain;

/**
 * Sent from the client to request a list of roots.
 */
public class ListRootsRequest extends ClientRequest {
    private final String method = "roots/list";
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
         * Whether to subscribe to notifications for changes to the roots list.
         */
        private Boolean subscribe;

        public Boolean getSubscribe() {
            return subscribe;
        }

        public void setSubscribe(Boolean subscribe) {
            this.subscribe = subscribe;
        }
    }
}