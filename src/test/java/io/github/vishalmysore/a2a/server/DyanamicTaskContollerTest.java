package io.github.vishalmysore.a2a.server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DyanamicTaskContollerTest {

    @Test
    public void testDyanamicTaskContollerInitialization() {
        DyanamicTaskContoller controller = new DyanamicTaskContoller();
        assertNotNull(controller, "DyanamicTaskContoller should be initialized successfully");
    }

    // Additional tests for DyanamicTaskContoller methods can be added here
}