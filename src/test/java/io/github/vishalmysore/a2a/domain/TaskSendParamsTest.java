package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskSendParamsTest {

    @Test
    public void testTaskSendParamsInitialization() {
        TaskSendParams params = new TaskSendParams();
        params.setId("12345");
        assertNotNull(params, "TaskSendParams should be initialized successfully");
        assertEquals("12345", params.getId(), "TaskSendParams ID should be '12345'");
    }

    // Additional tests for TaskSendParams methods can be added here
}