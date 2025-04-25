package io.github.vishalmysore;

import java.util.Date;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
