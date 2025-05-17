package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Data

public class TaskStatus {

    @JsonIgnore
    private String id;


    private TaskState state;


    private Message message;

    private String timestamp;

    public TaskStatus() {
        this.state = TaskState.SUBMITTED;
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
        this.timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
    }
}