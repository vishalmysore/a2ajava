package io.github.vishalmysore;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TaskSendParams {
    private String id;
    private String sessionId;
    private Message message;
    private int historyLength;
    private TaskPushNotificationConfig pushNotification;
    private Map<String, Object> metadata;
    private List<String> acceptedOutputModes;
}


