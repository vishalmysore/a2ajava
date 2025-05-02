package io.github.vishalmysore.a2a.domain;



import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CancelTaskResponse {
    private final String jsonrpc = "2.0";
    private Object id;
    private Task result;
    private JSONRPCError error;

    public CancelTaskResponse() {
    }

    public CancelTaskResponse(Object id, Task result) {
        this.id = id;
        this.result = result;
        this.error = null;
    }

    public CancelTaskResponse(Object id, JSONRPCError error) {
        this.id = id;
        this.result = null;
        this.error = error;
    }
}
