package io.github.vishalmysore;

import java.util.Map;

class TaskStatusUpdateEvent {
    private String id;
    private TaskStatus status;
    private boolean finalValue;
    private Map<String, Object> metadata;

    public TaskStatusUpdateEvent() {
    }

    public TaskStatusUpdateEvent(String id, TaskStatus status, boolean finalValue) {
        this.id = id;
        this.status = status;
        this.finalValue = finalValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public boolean isFinalValue() {
        return finalValue;
    }

    public void setFinalValue(boolean finalValue) {
        this.finalValue = finalValue;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
