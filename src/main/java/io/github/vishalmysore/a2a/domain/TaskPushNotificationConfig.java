package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data

public class TaskPushNotificationConfig {

    @JsonIgnore
    private String id;

    private String url;
    private String token;


    private Authentication authentication;


}

