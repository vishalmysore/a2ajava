package io.github.vishalmysore.mcp.domain;

/**
 * Audio provided to or from an LLM.
 */
public class AudioContent {
    /**
     * Optional annotations for the client.
     */
    private Annotations annotations;
    /**
     * The base64-encoded audio data.
     */
    private String data;
    /**
     * The MIME type of the audio. Different providers may support different audio types.
     */
    private String mimeType;
    private final String type = "audio";

    public Annotations getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotations annotations) {
        this.annotations = annotations;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getType() {
        return type;
    }
}