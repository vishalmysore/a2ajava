package io.github.vishalmysore.mcp.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
public class ToolInputSchema {
    @NotNull
    private final String type = "object";

    private Map<String, ToolPropertySchema> properties;

    private List<String> required;

}