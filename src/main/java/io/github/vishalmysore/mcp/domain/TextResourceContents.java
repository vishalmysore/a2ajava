package io.github.vishalmysore.mcp.domain;

public class TextResourceContents {
    /**
     * Optional annotations for the client.
     */
    private Annotations annotations;
    /**
     * The text string.
     */
    private String text;
    /**
     * The MIME type of the text.
     */
    private String mimeType;

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

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}