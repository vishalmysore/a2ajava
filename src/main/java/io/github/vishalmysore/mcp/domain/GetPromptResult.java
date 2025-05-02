package io.github.vishalmysore.mcp.domain;

import java.util.List;
import java.util.Map;

/**
 * The server's response to a prompts/get request from the client.
 */
public class GetPromptResult {
    /**
     * This result property is reserved by the protocol to allow clients and servers to attach additional metadata to their responses.
     */
    private Map<String, Object> _meta;
    /**
     * An optional description for the prompt.
     */
    private String description;
    private List<PromptMessage> messages;

    public Map<String, Object> getMeta() {
        return _meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this._meta = meta;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PromptMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<PromptMessage> messages) {
        this.messages = messages;
    }
}

