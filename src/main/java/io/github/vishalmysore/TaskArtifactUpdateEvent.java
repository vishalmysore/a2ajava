package io.github.vishalmysore;

import java.util.Map;

class TaskArtifactUpdateEvent {
    private String id;
    private Artifact artifact;
    private Map<String, Object> metadata;

    public TaskArtifactUpdateEvent() {
    }

    public TaskArtifactUpdateEvent(String id, Artifact artifact) {
        this.id = id;
        this.artifact = artifact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}

