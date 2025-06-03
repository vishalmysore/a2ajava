package io.github.vishalmysore.mcp.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vishalmysore.common.CommonClientRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Used by the client to invoke a tool provided by the server.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallToolRequest implements CommonClientRequest {
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
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
