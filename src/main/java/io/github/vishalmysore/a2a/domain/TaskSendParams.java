package io.github.vishalmysore.a2a.domain;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
public class TaskSendParams {
    private String id;
    private String sessionId;
    private Message message;
    private int historyLength;
    private TaskPushNotificationConfig pushNotification;
    private Map<String, Object> metadata;
    private List<String> acceptedOutputModes;
}


