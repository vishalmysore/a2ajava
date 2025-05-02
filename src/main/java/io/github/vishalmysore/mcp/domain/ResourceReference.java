package io.github.vishalmysore.mcp.domain;


/**
 * A resource, or a reference to a resource.
 */
public class ResourceReference {
    /**
     * The URI of the resource.
     */
    private String uri; // Changed from URI to String

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}