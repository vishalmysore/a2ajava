package io.github.vishalmysore;



import java.util.List;
import java.util.Map;

public class Task {
    private String id;
    private String sessionId;
    private TaskStatus status;
    private List<Message> history;
    private List<Artifact> artifacts;
    private Map<String, Object> metadata;
    private TaskPushNotificationConfig pushNotificationConfig; // Added

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

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public List<Message> getHistory() {
        return history;
    }

    public void setHistory(List<Message> history) {
        this.history = history;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public TaskPushNotificationConfig getPushNotificationConfig() {
        return pushNotificationConfig;
    }

    public void setPushNotificationConfig(TaskPushNotificationConfig pushNotificationConfig) {
        this.pushNotificationConfig = pushNotificationConfig;
    }
}
