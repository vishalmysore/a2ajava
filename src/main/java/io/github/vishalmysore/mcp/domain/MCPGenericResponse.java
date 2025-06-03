package io.github.vishalmysore.mcp.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MCPGenericResponse<T> {
    private String jsonrpc;
    private Object id;
    private T result;


}
