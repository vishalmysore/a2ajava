package io.github.vishalmysore.mcp.domain;

/**
 * Sent from the client to request a list of resource templates the server has.
 */
public class ListResourceTemplatesRequest extends ClientRequest {
    private final String method = "resourceTemplates/list";
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
         * An opaque token representing the current pagination position.
         * If provided, the server should return results starting after this cursor.
         */
        private String cursor;  // Changed from Cursor to String

        public String getCursor() {
            return cursor;
        }

        public void setCursor(String cursor) {
            this.cursor = cursor;
        }
    }
}
