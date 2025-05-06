package io.github.vishalmysore.a2a.domain;



import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Represents a task in the system. We can have multiple tasks in a session. and each task can have multiple artifacts.
 * A task can be in one of the following states:
 * 1. PENDING: The task is created but not yet started.
 * 2. IN_PROGRESS: The task is currently being processed.
 * 3. COMPLETED: The task has been completed successfully.
 * 4. FAILED: The task has failed.
 * 5. CANCELLED: The task has been cancelled.
 * 6. TIMEOUT: The task has timed out.
 * 7. EXPIRED: The task has expired.
 *
 * Implementation of TaskStatus enum is not provided in the original code.
 * All implmentation of Task can be done in applications extending this library
 */
@Data
@Entity
public class Task implements A2ATask {
    @Id
    private String id;
    private String sessionId;
    @OneToOne(cascade = CascadeType.ALL)
    private TaskStatus status;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private List<Message> history;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private List<Artifact> artifacts;

    @ElementCollection
    @CollectionTable(name = "task_metadata",
            joinColumns = @JoinColumn(name = "task_id"))
    @MapKeyColumn(name = "metadata_key")
    @Column(name = "metadata_value", columnDefinition = "TEXT")
    private Map<String, String> metadata;
    @OneToOne(cascade = CascadeType.ALL)
    private TaskPushNotificationConfig pushNotificationConfig; // Added
    private String pushNotificationUrl; // Added
    boolean subscribed;
    @Temporal(TemporalType.TIMESTAMP)
    private Date subscriptionDateNow;
    boolean cancelled;

    public void setDetailedAndMessage(TaskState state, String messageStr) {
        TextPart textPart = new TextPart();
        textPart.setType("text");
        textPart.setText("messageStr");

        Message message = new Message();
        message.setRole("agent");
        message.setParts(List.of(textPart));

        TaskStatus processingStatus = new TaskStatus(state);
        processingStatus.setMessage(message);
        this.setStatus(processingStatus);
    }

}
