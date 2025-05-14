package io.github.vishalmysore.a2a.domain;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
public class TaskSendSubscribeParams {
    private String id;
    private String sessionId;
    private Message message;
    private List<String> acceptedOutputModes;
    private Object pushNotification; // you can make a special class later if needed
    private Integer historyLength;
    private Map<String, Object> metadata; // dynamic structure, so Map
}