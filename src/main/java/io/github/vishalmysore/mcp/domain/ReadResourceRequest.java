package io.github.vishalmysore.mcp.domain;


/**
 * Sent from the client to ask the server to read a resource.
 */
public class ReadResourceRequest extends ClientRequest {
    private final String method = "resources/read";
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
         * The URI of the resource to read.
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
