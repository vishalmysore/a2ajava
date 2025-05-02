package io.github.vishalmysore.mcp.domain;

import java.util.Map;

/**
 * The client's response to a sampling/create_message request from the server.
 * The client should inform the user before returning the sampled message,
 * to allow them to inspect the response (human in the loop) and decide whether to allow the server to see it.
 */
public class CreateMessageResult extends ClientResult {
    /**
     * This result property is reserved by the protocol to allow clients and servers to attach additional metadata to their responses.
     */
    private Map<String, Object> _meta;
    private Content content;
    /**
     * The name of the model that generated the message.
     */
    private String model;
    private Role role;
    /**
     * The reason why sampling stopped, if known.
     */
    private String stopReason;

    public Map<String, Object> getMeta() {
        return _meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this._meta = meta;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getStopReason() {
        return stopReason;
    }

    public void setStopReason(String stopReason) {
        this.stopReason = stopReason;
    }
}
