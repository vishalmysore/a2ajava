package io.github.vishalmysore.a2a.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskArtifactUpdateEvent {
    private String id;
    private Artifact artifact;
    private Map<String, Object> metadata;


    public TaskArtifactUpdateEvent(String taskId, Artifact artifact) {
    }
}

