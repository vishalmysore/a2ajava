package io.github.vishalmysore.mcp.domain;


/**
 * A resource, which may be text or binary data.
 */
public class Resource {
    /**
     * Optional annotations for the client.
     */
    private Annotations annotations;
    /**
     * The contents of the resource.
     */
    private Object contents;  // Can be TextResourceContents or BlobResourceContents. Use Object
    /**
     * The URI of the resource.
     */
    private String uri;  // Changed from URI to String

    public Annotations getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotations annotations) {
        this.annotations = annotations;
    }

    public Object getContents() {
        return contents;
    }

    public void setContents(Object contents) {
        this.contents = contents;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
