package io.github.vishalmysore.mcp.domain;

import java.util.List;

public class SamplingContext {
    /**
     * The messages in the prompt.
     */
    private List<PromptMessage> messages;
    /**
     * The URI of the resource.
     */
    private String uri;  // Changed from URI to String

    public List<PromptMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<PromptMessage> messages) {
        this.messages = messages;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}