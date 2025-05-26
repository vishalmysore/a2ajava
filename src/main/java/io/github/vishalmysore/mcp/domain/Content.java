package io.github.vishalmysore.mcp.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextContent.class, name = "text"),
        @JsonSubTypes.Type(value = ImageContent.class, name = "image")
})
public interface Content {
}
