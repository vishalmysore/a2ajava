package io.github.vishalmysore.mcp.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ToolParameter {
    private String type;
    private String description;
}