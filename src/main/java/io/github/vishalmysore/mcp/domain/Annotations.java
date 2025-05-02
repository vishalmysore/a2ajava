package io.github.vishalmysore.mcp.domain;

import java.util.List;
import java.util.Map;

// Classes generated from JSON schema

/**
 * Optional annotations for the client. The client can use annotations to inform how objects are used or displayed
 */
public class Annotations {
    /**
     * Describes who the intended customer of this object or data is.
     * <p>
     * It can include multiple entries to indicate content useful for multiple audiences (e.g., `["user", "assistant"]`).
     */
    private List<Role> audience;
    /**
     * Describes how important this data is for operating the server.
     * <p>
     * A value of 1 means "most important," and indicates that the data is
     * effectively required, while 0 means "least important," and indicates that
     * the data is entirely optional.
     */
    private Double priority;

    public List<Role> getAudience() {
        return audience;
    }

    public void setAudience(List<Role> audience) {
        this.audience = audience;
    }

    public Double getPriority() {
        return priority;
    }

    public void setPriority(Double priority) {
        this.priority = priority;
    }
}
