package io.github.vishalmysore.mcp.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.vishalmysore.common.CommonClientResponse;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = CallToolResult.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CallToolResult.class, name = "callTool"),
        @JsonSubTypes.Type(value = EmptyResult.class, name = "empty")
})
/**
 * The base type for all result types.
 */
public abstract class Result implements CommonClientResponse {
}