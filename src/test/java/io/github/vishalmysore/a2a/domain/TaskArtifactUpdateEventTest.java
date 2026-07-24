package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TaskArtifactUpdateEventTest {

    @Test
    public void testDefaultConstructor() {
        TaskArtifactUpdateEvent event = new TaskArtifactUpdateEvent();
        assertNotNull(event);
        assertNull(event.getId());
        assertNull(event.getArtifact());
        assertNull(event.getMetadata());
    }

    @Test
    public void testAllArgsConstructor() {
        Artifact artifact = new Artifact();
        artifact.setName("test-artifact");
        artifact.setDescription("A test artifact");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key1", "value1");
        metadata.put("key2", 42);

        TaskArtifactUpdateEvent event = new TaskArtifactUpdateEvent("task-123", artifact, metadata);

        assertEquals("task-123", event.getId());
        assertEquals(artifact, event.getArtifact());
        assertEquals(metadata, event.getMetadata());
        assertEquals("value1", event.getMetadata().get("key1"));
        assertEquals(42, event.getMetadata().get("key2"));
    }

    @Test
    public void testSettersAndGetters() {
        TaskArtifactUpdateEvent event = new TaskArtifactUpdateEvent();

        Artifact artifact = new Artifact();
        artifact.setName("updated-artifact");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("status", "completed");

        event.setId("event-456");
        event.setArtifact(artifact);
        event.setMetadata(metadata);

        assertEquals("event-456", event.getId());
        assertEquals("updated-artifact", event.getArtifact().getName());
        assertEquals("completed", event.getMetadata().get("status"));
    }

    @Test
    public void testTwoArgConstructor() {
        Artifact artifact = new Artifact();
        artifact.setName("simple-artifact");

        TaskArtifactUpdateEvent event = new TaskArtifactUpdateEvent("task-789", artifact);
        assertNotNull(event);
    }

    @Test
    public void testEqualsAndHashCode() {
        Artifact artifact = new Artifact();
        artifact.setName("artifact-1");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key", "value");

        TaskArtifactUpdateEvent event1 = new TaskArtifactUpdateEvent("id-1", artifact, metadata);
        TaskArtifactUpdateEvent event2 = new TaskArtifactUpdateEvent("id-1", artifact, metadata);

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }
}
