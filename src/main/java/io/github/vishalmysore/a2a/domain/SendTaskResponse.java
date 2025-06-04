package io.github.vishalmysore.a2a.domain;



import io.github.vishalmysore.common.CommonClientResponse;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SendTaskResponse implements CommonClientResponse {
    private final String jsonrpc = "2.0";
    private String id;
    private Task result;
    private JSONRPCError error;
}