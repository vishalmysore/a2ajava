package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

public class TaskStateManagementTest {

    private Task task;
    private String taskId;

    @BeforeEach
    public void setUp() {
        taskId = UUID.randomUUID().toString();
        task = new Task();
        task.setId(taskId);
    }

    @Test
    public void testInitialState() {
        assertEquals(taskId, task.getId());
        assertNull(task.getStatus());
        assertNull(task.getHistory());
        assertFalse(task.isCancelled());
    }

    @Test
    public void testStateTransitions() {
        // Set to SUBMITTED
        TaskStatus submittedStatus = new TaskStatus(TaskState.SUBMITTED);
        task.setStatus(submittedStatus);
        assertEquals(TaskState.SUBMITTED, task.getStatus().getState());

        // Set to WORKING
        TaskStatus workingStatus = new TaskStatus(TaskState.WORKING);
        task.setStatus(workingStatus);
        assertEquals(TaskState.WORKING, task.getStatus().getState());

        // Set to COMPLETED
        TaskStatus completedStatus = new TaskStatus(TaskState.COMPLETED);
        task.setStatus(completedStatus);
        assertEquals(TaskState.COMPLETED, task.getStatus().getState());
    }

    @Test
    public void testDetailedStateUpdate() {
        String detailedMessage = "Processing task details...";
        task.setDetailedAndMessage(TaskState.WORKING, detailedMessage);

        assertEquals(TaskState.WORKING, task.getStatus().getState());
        assertTrue(task.getStatus().getMessage().getParts().stream()
            .filter(p -> p instanceof TextPart)
            .map(p -> ((TextPart)p).getText())
            .anyMatch(text -> text.equals(detailedMessage)));
    }

    @Test
    public void testHistoryManagement() {
        // Create initial message
        Message msg1 = new Message();
        TextPart part1 = new TextPart();
        part1.setType("text");
        part1.setText("First message");
        msg1.setParts(List.of(part1));

        // Create second message
        Message msg2 = new Message();
        TextPart part2 = new TextPart();
        part2.setType("text");
        part2.setText("Second message");
        msg2.setParts(List.of(part2));

        // Set history
        task.setHistory(List.of(msg1, msg2));

        // Verify history
        List<Message> history = task.getHistory();
        assertEquals(2, history.size());
        
        TextPart firstHistoryPart = (TextPart)history.get(0).getParts().get(0);
        assertEquals("First message", firstHistoryPart.getText());
        
        TextPart secondHistoryPart = (TextPart)history.get(1).getParts().get(0);
        assertEquals("Second message", secondHistoryPart.getText());
    }

    @Test
    public void testArtifactManagement() {
        // Create test artifact
        Artifact artifact = new Artifact();
        artifact.setName("Test Artifact");
        artifact.setDescription("Test Description");

        TextPart artifactContent = new TextPart();
        artifactContent.setType("text");
        artifactContent.setText("Artifact content");
        artifact.setParts(List.of(artifactContent));

        // Set artifacts
        task.setArtifacts(List.of(artifact));

        // Verify artifacts
        List<Artifact> artifacts = task.getArtifacts();
        assertEquals(1, artifacts.size());
        assertEquals("Test Artifact", artifacts.get(0).getName());
        assertEquals("Test Description", artifacts.get(0).getDescription());
        
        TextPart retrievedContent = (TextPart)artifacts.get(0).getParts().get(0);
        assertEquals("Artifact content", retrievedContent.getText());
    }

    @Test
    public void testCancellation() {
        assertFalse(task.isCancelled());
        task.setCancelled(true);
        assertTrue(task.isCancelled());
    }
}
