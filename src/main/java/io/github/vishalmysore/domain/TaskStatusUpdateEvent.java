package io.github.vishalmysore.domain;

import lombok.Data;

import java.util.Map;

@Data
public class TaskStatusUpdateEvent {
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


}
