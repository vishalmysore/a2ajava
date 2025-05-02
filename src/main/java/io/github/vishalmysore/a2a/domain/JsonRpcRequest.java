package io.github.vishalmysore.a2a.domain;

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

}
