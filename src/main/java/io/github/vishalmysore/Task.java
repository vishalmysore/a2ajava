package io.github.vishalmysore;



import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class Task {
    private String id;
    private String sessionId;
    private TaskStatus status;
    private List<Message> history;
    private List<Artifact> artifacts;
    private Map<String, Object> metadata;
    private TaskPushNotificationConfig pushNotificationConfig; // Added
    private String pushNotificationUrl; // Added
    boolean subscribed;
    Date subscriptionDateNow;
    boolean cancelled;

}
