package io.github.vishalmysore.mcp.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Used by the client to invoke a tool provided by the server.
 */
public class CallToolRequest {
    private final String method = "tools/call";
    private Params params;

    public String getMethod() {
        return method;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public static CallToolRequest fromString(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, CallToolRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing CallToolRequest: " + e.getMessage(), e);
        }
    }

    public static class Params {
        private Map<String, Object> arguments;
        private String name;

        public Map<String, Object> getArguments() {
            return arguments;
        }

        public void setArguments(Map<String, Object> arguments) {
            this.arguments = arguments;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
