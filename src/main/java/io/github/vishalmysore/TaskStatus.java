package io.github.vishalmysore;

import lombok.Data;

import java.util.Date;

@Data
class TaskStatus {
    private String state;
    private Message message;
    private String timestamp;

    public TaskStatus() {
    }

    public TaskStatus(String state) {
        this.state = state;
        this.timestamp = new Date().toString();
    }


}
