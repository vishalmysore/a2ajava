package io.github.vishalmysore.domain;

import lombok.Data;

@Data
public class TaskPushNotificationConfigRequest {
    private String id;
    private TaskPushNotificationConfig pushNotificationConfig;


}

