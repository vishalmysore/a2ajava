package io.github.vishalmysore.mcp.domain;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class ToolParameters {
    private String $schema = "http://json-schema.org/draft-07/schema#";
    private String type = "object";
    private Map<String, ToolParameter> properties = new HashMap<>();
    private List<String> required = new ArrayList<>();
    private boolean additionalProperties = false;
}