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
 * 
 * Jackson uses DEDUCTION to determine which Part type based on presence of specific fields.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.DEDUCTION
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextPart.class),
        @JsonSubTypes.Type(value = FilePart.class),
        @JsonSubTypes.Type(value = DataPart.class)
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
