package io.github.vishalmysore.mcp.domain;

import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Data
@ToString
public class ToolCallRequest {

        private String name;
        private Map<String, Object> arguments = new HashMap<>();

}
