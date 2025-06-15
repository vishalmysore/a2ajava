package io.github.vishalmysore.a2a.domain;




import io.github.vishalmysore.common.CommonClientRequest;
import io.github.vishalmysore.common.CommonClientResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
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
 * All implementation of Task can be done in applications extending this library
 */
@Data
@Getter
@Setter
@ToString
public class Task implements A2ATask, CommonClientRequest, CommonClientResponse {

    private String id;
    private String sessionId;

    private TaskStatus status;

    private List<Message> history;

    private List<Artifact> artifacts;


    private Map<String, String> metadata;

    private TaskPushNotificationConfig pushNotificationConfig; // Added
    private String pushNotificationUrl; // Added
    boolean subscribed;

    private Date subscriptionDateNow;
    boolean cancelled;

    public void setDetailedAndMessage(TaskState state, String messageStr) {
        TextPart textPart = new TextPart();
        textPart.setType("text");
        textPart.setText(messageStr);

        Message message = new Message();
        message.setRole("agent");
        message.setParts(new ArrayList<>(List.of(textPart)));

        TaskStatus processingStatus = new TaskStatus(state);
        processingStatus.setMessage(message);
        this.setStatus(processingStatus);
    }

}
