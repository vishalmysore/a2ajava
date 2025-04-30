package io.github.vishalmysore.domain;



import lombok.Data;

@Data
public class SendTaskResponse {
    private final String jsonrpc = "2.0";
    private String id;
    private Task result;
    private JSONRPCError error;
}