package io.github.vishalmysore.mcp.domain;

public class EmbeddedResource implements Content {
    /**
     * Optional annotations for the client.
     */
    private Annotations annotations;
    private Object resource;  // Can be TextResourceContents or BlobResourceContents.  Use Object
    private final String type = "resource";

    public Annotations getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotations annotations) {
        this.annotations = annotations;
    }

    public Object getResource() {
        return resource;
    }

    public void setResource(Object resource) {
        this.resource = resource;
    }

    public String getType() {
        return type;
    }
}