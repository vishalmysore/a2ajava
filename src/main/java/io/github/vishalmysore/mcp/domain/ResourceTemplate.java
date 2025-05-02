package io.github.vishalmysore.mcp.domain;


/**
 * A resource template, which can be used to generate resources.
 */
public class ResourceTemplate {
    /**
     * Optional annotations for the client.
     */
    private Annotations annotations;
    /**
     * The name of the resource template.
     */
    private String name;
    /**
     * The URI of the resource template.
     */
    private String uri;  // Changed from URI to String

    public Annotations getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotations annotations) {
        this.annotations = annotations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}