package io.github.vishalmysore.mcp.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class EmbeddedResource implements Content {
    /**
     * Optional annotations for the client.
     */
    private Annotations annotations;
    
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = TextResourceContents.class, name = "text"),
        @JsonSubTypes.Type(value = BlobResourceContents.class, name = "blob")
    })
    private Object resource;  // Can be TextResourceContents or BlobResourceContents

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