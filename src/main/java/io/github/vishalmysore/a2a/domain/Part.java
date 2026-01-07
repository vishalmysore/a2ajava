package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;

/**
 * A2A v1.0: Parts use JSON member names as discriminators.
 * Examples:
 * - TextPart: {"text": "..."}
 * - FilePart: {"file": {...}}
 * - DataPart: {"data": {...}}
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.WRAPPER_OBJECT,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextPart.class, name = "text"),
        @JsonSubTypes.Type(value = FilePart.class, name = "file"),
        @JsonSubTypes.Type(value = DataPart.class, name = "data")
})
public abstract class Part {

    @JsonIgnore
    private String id;

    @JsonIgnore
    private String type;

    private Map<String, String> metadata;

    public abstract String getType();
    public abstract Map<String, String> getMetadata();
    public abstract void setType(String type);
}
