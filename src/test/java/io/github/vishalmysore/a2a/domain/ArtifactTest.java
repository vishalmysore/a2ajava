package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for Artifact domain object
 */
public class ArtifactTest {

    @Test
    void testDefaultConstructor() {
        Artifact artifact = new Artifact();
        
        assertNull(artifact.getId());
        assertNull(artifact.getName());
        assertNull(artifact.getDescription());
        assertNull(artifact.getParts());
        assertNotNull(artifact.getMetadata());
        assertEquals(0, artifact.getIndex());
        assertFalse(artifact.isAppend());
        assertFalse(artifact.isLastChunk());
    }

    @Test
    void testSettersAndGetters() {
        Artifact artifact = new Artifact();
        
        // Test ID
        String id = "artifact-123";
        artifact.setId(id);
        assertEquals(id, artifact.getId());
        
        // Test name
        String name = "Test Artifact";
        artifact.setName(name);
        assertEquals(name, artifact.getName());
        
        // Test description
        String description = "This is a test artifact";
        artifact.setDescription(description);
        assertEquals(description, artifact.getDescription());
        
        // Test parts
        List<Part> parts = new ArrayList<>();
        TextPart textPart = new TextPart();
        textPart.setText("Hello world");
        parts.add(textPart);
        artifact.setParts(parts);
        assertEquals(parts, artifact.getParts());
        
        // Test metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key1", "value1");
        metadata.put("key2", "value2");
        artifact.setMetadata(metadata);
        assertEquals(metadata, artifact.getMetadata());
        
        // Test index
        int index = 5;
        artifact.setIndex(index);
        assertEquals(index, artifact.getIndex());
        
        // Test append
        artifact.setAppend(true);
        assertTrue(artifact.isAppend());
        
        // Test lastChunk
        artifact.setLastChunk(true);
        assertTrue(artifact.isLastChunk());
    }

    @Test
    void testMetadataInitialization() {
        Artifact artifact = new Artifact();
        
        assertNotNull(artifact.getMetadata());
        assertTrue(artifact.getMetadata().isEmpty());
        
        // Add metadata entries
        artifact.getMetadata().put("created", "2023-01-01");
        artifact.getMetadata().put("creator", "test-user");
        
        assertEquals(2, artifact.getMetadata().size());
        assertEquals("2023-01-01", artifact.getMetadata().get("created"));
        assertEquals("test-user", artifact.getMetadata().get("creator"));
    }

    @Test
    void testToString() {
        Artifact artifact = new Artifact();
        artifact.setId("artifact-123");
        artifact.setName("Test Artifact");
        artifact.setDescription("Test Description");
        artifact.setIndex(1);
        artifact.setAppend(true);
        artifact.setLastChunk(false);
        
        String toStringResult = artifact.toString();
        
        // Check all fields are included in toString
        assertTrue(toStringResult.contains("id=artifact-123"));
        assertTrue(toStringResult.contains("name=Test Artifact"));
        assertTrue(toStringResult.contains("description=Test Description"));
        assertTrue(toStringResult.contains("index=1"));
        assertTrue(toStringResult.contains("append=true"));
        assertTrue(toStringResult.contains("lastChunk=false"));
        assertTrue(toStringResult.contains("metadata={}"));
    }

    @Test
    void testEquals() {
        Artifact artifact1 = new Artifact();
        artifact1.setId("artifact-123");
        artifact1.setName("Test Artifact");
        
        Artifact artifact2 = new Artifact();
        artifact2.setId("artifact-123");
        artifact2.setName("Test Artifact");
        
        Artifact artifact3 = new Artifact();
        artifact3.setId("artifact-456");
        artifact3.setName("Different Artifact");
        
        assertEquals(artifact1, artifact2);
        assertNotEquals(artifact1, artifact3);
    }

    @Test
    void testHashCode() {
        Artifact artifact1 = new Artifact();
        artifact1.setId("artifact-123");
        artifact1.setName("Test Artifact");
        
        Artifact artifact2 = new Artifact();
        artifact2.setId("artifact-123");
        artifact2.setName("Test Artifact");
        
        assertEquals(artifact1.hashCode(), artifact2.hashCode());
    }
}