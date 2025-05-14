package io.github.vishalmysore.a2a.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TaskSetPushNotificationParams {
    private String taskId;         // Task ID
    private String notificationUrl; // URL where notifications should be sent

    private String pushNotificationUrl;
}
