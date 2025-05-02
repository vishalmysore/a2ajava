package io.github.vishalmysore.a2a.domain;

import lombok.Data;

@Data
public class TaskGetPushNotificationParams {
    private String taskId; // Task ID to retrieve the push notification settings
}
