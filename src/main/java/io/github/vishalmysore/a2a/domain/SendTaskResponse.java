package io.github.vishalmysore.a2a.domain;



import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SendTaskResponse {
    private final String jsonrpc = "2.0";
    private String id;
    private Task result;
    private JSONRPCError error;
}