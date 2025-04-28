package io.github.vishalmysore;

import lombok.Data;

import java.util.Date;

@Data
class TaskStatus {
    private TaskState state;
    private Message message;
    private String timestamp;

    public TaskStatus() {
    }

    public TaskStatus(TaskState state) {
        this.state = state;
        this.timestamp = new Date().toString();
    }

    public TaskStatus(String state) {
        this.state = TaskState.forValue(state);
        this.timestamp = new Date().toString();
    }

}
