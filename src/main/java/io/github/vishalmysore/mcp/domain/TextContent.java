package io.github.vishalmysore.mcp.domain;

public class TextContent implements Content {
    /**
     * Optional annotations for the client.
     */
    private Annotations annotations;
    /**
     * The text string.
     */
    private String text;
    /**
     * The media type of the text, e.g. "text/plain" or "text/html".
     */
    private String type;

    public Annotations getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotations annotations) {
        this.annotations = annotations;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}