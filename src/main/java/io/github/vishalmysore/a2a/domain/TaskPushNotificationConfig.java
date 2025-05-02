package io.github.vishalmysore.a2a.domain;

import lombok.Data;

@Data
public class TaskPushNotificationConfig {
    private String url;
    private String token;
    private Authentication authentication;


}

