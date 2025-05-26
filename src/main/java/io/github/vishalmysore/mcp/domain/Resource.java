package io.github.vishalmysore.mcp.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A resource, which may be text or binary data.
 */
public class Resource {    /**
     * Optional annotations for the client.
     */
    private Annotations annotations;
    
    /**
     * The contents of the resource.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = TextResourceContents.class, name = "text"),
        @JsonSubTypes.Type(value = BlobResourceContents.class, name = "blob")
    })
    private Object contents;
    
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
