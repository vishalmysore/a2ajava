package io.github.vishalmysore.mcp.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CallToolResult.class, name = "callTool"),
        @JsonSubTypes.Type(value = EmptyResult.class, name = "empty")
})
/**
 * The base type for all result types.
 */
public abstract class Result {
}