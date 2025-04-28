package io.github.vishalmysore;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendTaskStreamingResponse {

    @JsonProperty("jsonrpc")
    private String jsonrpc = "2.0";

    @JsonProperty("id")
    private Object id; // Can be Integer, String, or null

    @JsonProperty("result")
    private Object result; // Should be TaskStatusUpdateEvent or TaskArtifactUpdateEvent or null




    // Constructors
    public SendTaskStreamingResponse() {
    }

    public SendTaskStreamingResponse(Object id, Object result) {
        this.id = id;
        this.result = result;

    }

    // Getters and Setters
    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
