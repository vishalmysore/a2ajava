package io.github.vishalmysore.mcp.domain;

/**
 * A response to a request that indicates an error occurred.
 */
public class JSONRPCError {
    private Error error;
    private String id;
    private final String jsonrpc = "2.0";

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public static class Error {
        /**
         * The error type that occurred.
         */
        private Integer code;
        /**
         * Additional information about the error.
         * The value of this member is defined by the sender (e.g. detailed error information, nested errors etc.).
         */
        private Object data;
        /**
         * A short description of the error. The message SHOULD be limited to a concise single sentence.
         */
        private String message;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}