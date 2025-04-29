package io.github.vishalmysore.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetTaskPushNotificationResponse {
    private final String jsonrpc = "2.0";
    private Object id;
    private TaskPushNotificationConfig result;


    public GetTaskPushNotificationResponse() {
    }

    public GetTaskPushNotificationResponse(Object id, TaskPushNotificationConfig result) {
        this.id = id;
        this.result = result;

    }

    public GetTaskPushNotificationResponse(Object id) {
        this.id = id;
        this.result = null;

    }
}