package io.github.vishalmysore.domain;

import lombok.Data;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Data
public class TaskStatus {
    private TaskState state;
    private Message message;
    private String timestamp;

    public TaskStatus() {
        setCurrentTimestamp();
    }

    public TaskStatus(TaskState state) {
        this.state = state;
        setCurrentTimestamp();
    }

    public TaskStatus(String state) {
        this.state = TaskState.forValue(state);
        setCurrentTimestamp();
    }
    private void setCurrentTimestamp() {
        // Format: 2025-04-28T18:40:24.123Z
        this.timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
    }
}
