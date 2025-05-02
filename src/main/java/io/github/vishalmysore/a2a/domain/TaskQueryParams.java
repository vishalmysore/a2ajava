package io.github.vishalmysore.a2a.domain;

import lombok.Data;

import java.util.Map;

@Data
public class TaskQueryParams {
    private String id;
    private Integer historyLength;
    private Map<String, Object> metadata;


}
