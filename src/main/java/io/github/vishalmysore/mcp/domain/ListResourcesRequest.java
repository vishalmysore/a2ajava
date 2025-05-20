package io.github.vishalmysore.mcp.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Sent from the client to request a list of resources.
 */
public class ListResourcesRequest extends ClientRequest {
    private final String method = "resources/list";

    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String jsonrpc;
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
        private String cursor; // Changed from Cursor to String
        /**
         * A string used to filter resources by matching against their names and/or descriptions.
         */
        private String filter;

        public String getCursor() {
            return cursor;
        }

        public void setCursor(String cursor) {
            this.cursor = cursor;
        }

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }
    }
}
