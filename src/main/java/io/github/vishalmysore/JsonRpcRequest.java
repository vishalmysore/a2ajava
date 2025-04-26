package io.github.vishalmysore;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JsonRpcRequest {
    private String jsonrpc;
    private String method;
    private Object params;
    private Object id;
    // getters and setters
}
