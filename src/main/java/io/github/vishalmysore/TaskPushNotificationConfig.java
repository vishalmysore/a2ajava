package io.github.vishalmysore;

import lombok.Data;

@Data
class TaskPushNotificationConfig {
    private String url;
    private String token;
    private Authentication authentication;


}

