package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskResubscriptionParamsTest {

    @Test
    public void testTaskResubscriptionParamsInitialization() {
        TaskResubscriptionParams params = new TaskResubscriptionParams();
        params.setTaskId("98765");
        assertNotNull(params, "TaskResubscriptionParams should be initialized successfully");
        assertEquals("98765", params.getTaskId(), "TaskResubscriptionParams task ID should be '98765'");
    }

    // Additional tests for TaskResubscriptionParams methods can be added here
}