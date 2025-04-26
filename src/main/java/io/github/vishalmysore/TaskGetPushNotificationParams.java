package io.github.vishalmysore;

import lombok.Data;

@Data
public class TaskGetPushNotificationParams {
    private String taskId; // Task ID to retrieve the push notification settings
}
