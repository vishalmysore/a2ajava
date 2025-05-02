package io.github.vishalmysore.mcp.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ToolCallRequest {

        private String name;
        private Map<String, Object> arguments = new HashMap<>();

}
