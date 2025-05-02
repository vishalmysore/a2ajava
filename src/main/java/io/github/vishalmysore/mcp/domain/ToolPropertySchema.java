package io.github.vishalmysore.mcp.domain;

import lombok.Data;

import java.util.Map;

@Data
public class ToolPropertySchema {
    private String type;
    private String description;
    private Map<String, Object> additionalProperties;
    private ToolPropertySchema items;
}
