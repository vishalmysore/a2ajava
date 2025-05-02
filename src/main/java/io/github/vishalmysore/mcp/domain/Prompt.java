package io.github.vishalmysore.mcp.domain;

import java.util.List;

/**
 * A prompt or prompt template.
 */
public class Prompt {
    /**
     * Optional annotations for the client.
     */
    private Annotations annotations;
    /**
     * An optional description for the prompt.
     */
    private String description;
    /**
     * The messages in the prompt.
     */
    private List<PromptMessage> messages;
    /**
     * The name of the prompt or prompt template.
     */
    private String name;

    public Annotations getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotations annotations) {
        this.annotations = annotations;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}