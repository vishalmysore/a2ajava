package io.github.vishalmysore.a2a.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TaskGetPushNotificationParams {
    private String taskId; // Task ID to retrieve the push notification settings
}
