package io.github.vishalmysore.mcp.domain;

import ai.djl.util.Progress;

/**
 * A notification that carries progress information about a long-running operation.
 */
public class ProgressNotification extends Notification {
    private final String method = "notifications/progress";
    private Params params;

    public String getMethod() {
        return method;
    }

    public JSONRPCNotification.Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }


}
