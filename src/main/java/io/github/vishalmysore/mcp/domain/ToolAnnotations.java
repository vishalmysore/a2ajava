package io.github.vishalmysore.mcp.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ToolAnnotations {
    private Map<String, Object> properties = new HashMap<>();
}