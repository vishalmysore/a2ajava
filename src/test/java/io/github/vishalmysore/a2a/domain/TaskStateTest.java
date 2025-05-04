package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskStateTest {

    @Test
    public void testTaskStateInitialization() {
        TaskState taskState = TaskState.SUBMITTED;
        assertNotNull(taskState, "TaskState should be initialized successfully");
    }

    @Test
    public void testTaskStateValues() {
        TaskState[] states = TaskState.values();
        assertTrue(states.length > 0, "TaskState should have defined values");
    }
}