package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private String id;

    @Enumerated(EnumType.STRING)
    private TaskState state;

    @OneToOne(cascade = CascadeType.ALL)
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