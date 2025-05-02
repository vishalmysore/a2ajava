package io.github.vishalmysore.mcp.domain;

/**
 * This notification can be sent by either side to indicate that it is cancelling a previously-issued request.
 * <p>
 * The request SHOULD still be in-flight, but due to communication latency, it is always possible that this notification MAY arrive after the request has already finished.
 * <p>
 * This notification indicates that the result will be unused, so any associated processing SHOULD cease.
 * <p>
 * A client MUST NOT attempt to cancel its `initialize` request.
 */
public class CancelledNotification {
    private final String method = "notifications/cancelled";
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
         * An optional string describing the reason for the cancellation. This MAY be logged or presented to the user.
         */
        private String reason;
        /**
         * The ID of the request to cancel.
         * <p>
         * This MUST correspond to the ID of a request previously issued in the same direction.
         */
        private String requestId;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }
    }
}