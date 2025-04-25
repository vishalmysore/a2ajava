package io.github.vishalmysore;

import java.util.Map;

public class TaskSendParams {
    private String id;
    private String sessionId;
    private Message message;
    private int historyLength;
    private TaskPushNotificationConfig pushNotification;
    private Map<String, Object> metadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getHistoryLength() {
        return historyLength;
    }

    public void setHistoryLength(int historyLength) {
        this.historyLength = historyLength;
    }

    public TaskPushNotificationConfig getPushNotification() {
        return pushNotification;
    }

    public void setPushNotification(TaskPushNotificationConfig pushNotification) {
        this.pushNotification= pushNotification;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
