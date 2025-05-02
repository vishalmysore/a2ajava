package io.github.vishalmysore.mcp.domain;

public class UnsubscribeRequest extends ClientRequest {
    private final String method = "resources/unsubscribe";
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
         * The URI of the resource to unsubscribe from.
         */
        private String uri;  // Changed from URI to String

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }
}