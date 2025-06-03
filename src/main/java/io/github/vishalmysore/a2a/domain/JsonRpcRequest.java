package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vishalmysore.common.CommonClientRequest;
import io.github.vishalmysore.mcp.domain.ClientRequest;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JsonRpcRequest implements CommonClientRequest {
    private String jsonrpc;
    private String method;
    private Object params;
    private Object id;

    public static JsonRpcRequest fromString(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, JsonRpcRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON RPC request: " + e.getMessage(), e);
        }
    }
}
