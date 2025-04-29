package io.github.vishalmysore.domain;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class TaskSendSubscribeParams {
    private String id;
    private String sessionId;
    private Message message;
    private List<String> acceptedOutputModes;
    private Object pushNotification; // you can make a special class later if needed
    private Integer historyLength;
    private Map<String, Object> metadata; // dynamic structure, so Map
}