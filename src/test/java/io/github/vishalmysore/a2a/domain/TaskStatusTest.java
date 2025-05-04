package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskStatusTest {

    @Test
    public void testTaskStatusInitialization() {
        TaskStatus status = new TaskStatus("COMPLETED");
        assertNotNull(status, "TaskStatus should be initialized successfully");
        assertEquals(TaskState.COMPLETED, status.getState(), "TaskStatus state should be 'COMPLETED'");
    }

    // Additional tests for TaskStatus methods can be added here
}