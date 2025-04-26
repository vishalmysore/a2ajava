package io.github.vishalmysore;

import lombok.Data;

@Data
public class TaskSetPushNotificationParams {
    private String taskId;         // Task ID
    private String notificationUrl; // URL where notifications should be sent

    private String pushNotificationUrl;
}
