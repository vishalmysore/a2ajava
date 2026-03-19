package io.github.vishalmysore.common;

import com.t4a.detect.ActionState;
import io.github.vishalmysore.a2a.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Advanced unit tests for A2AUICallback covering type override behavior,
 * inheritance from A2AActionCallBack, and combined A2UI operations.
 */
public class A2AUICallbackAdvancedTest {

    private A2AUICallback callback;

    @BeforeEach
    void setUp() {
        callback = new A2AUICallback();
    }

    // ── Type override ────────────────────────────────────────────────

    @Test
    void getType_shouldReturnA2UI_notA2A() {
        assertEquals(CallBackType.A2UI.name(), callback.getType());
        assertEquals("A2UI", callback.getType());
        assertNotEquals("A2A", callback.getType());
    }

    @Test
    void getType_shouldBeStable() {
        for (int i = 0; i < 100; i++) {
            assertEquals("A2UI", callback.getType());
        }
    }

    // ── Instance type checks ─────────────────────────────────────────

    @Test
    void shouldBeInstanceOfA2AActionCallBack() {
        assertTrue(callback instanceof A2AActionCallBack);
    }

    @Test
    void shouldNotBeInstanceOfMCPActionCallback() {
        // A2AUICallback extends A2AActionCallBack, not MCPActionCallback
        assertFalse(MCPActionCallback.class.isAssignableFrom(callback.getClass()));
    }

    // ── Inherited sendtStatus behavior ───────────────────────────────

    @Test
    void sendtStatus_shouldWorkExactlyLikeParent() {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        callback.sendtStatus("Processing", ActionState.WORKING);

        TaskStatus status = task.getStatus();
        assertNotNull(status);
        assertEquals(TaskState.WORKING, status.getState());

        Message message = status.getMessage();
        assertNotNull(message);
        assertEquals("agent", message.getRole());
        assertEquals(1, message.getParts().size());
        assertTrue(message.getParts().get(0) instanceof TextPart);
        assertEquals("Processing", ((TextPart) message.getParts().get(0)).getText());
    }

    @Test
    void sendtStatus_multipleTransitions() {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        callback.sendtStatus("submitted", ActionState.SUBMITTED);
        assertEquals(TaskState.SUBMITTED, task.getStatus().getState());

        callback.sendtStatus("working", ActionState.WORKING);
        assertEquals(TaskState.WORKING, task.getStatus().getState());

        callback.sendtStatus("done", ActionState.COMPLETED);
        assertEquals(TaskState.COMPLETED, task.getStatus().getState());
    }

    // ── Inherited addA2UIContent behavior ────────────────────────────

    @Test
    void addA2UIContent_shouldWorkThroughInheritance() {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        callback.addA2UIContent(Map.of("beginRendering", Map.of("surfaceId", "dashboard")));

        assertNotNull(task.getStatus());
        assertNotNull(task.getStatus().getMessage());
        assertEquals(1, task.getStatus().getMessage().getParts().size());

        DataPart dp = (DataPart) task.getStatus().getMessage().getParts().get(0);
        assertTrue(dp.isA2UIData());
        assertEquals("application/json+a2ui", dp.getMetadata().get("mimeType"));
    }

    @Test
    void mixedStatusAndA2UI_shouldWorkCorrectly() {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        // First send status (creates status+message)
        callback.sendtStatus("Starting UI render", ActionState.WORKING);

        // Then add A2UI content (appends to the existing message's parts)
        callback.addA2UIContent(Map.of("surfaceUpdate", Map.of(
                "surfaceId", "main",
                "rootComponent", Map.of("componentType", "Text", "id", "t1"))));

        List<Part> parts = task.getStatus().getMessage().getParts();
        assertTrue(parts.size() >= 1);
        boolean hasA2UI = parts.stream()
                .anyMatch(p -> p instanceof DataPart && ((DataPart) p).isA2UIData());
        assertTrue(hasA2UI, "Should contain A2UI DataPart");
    }

    @Test
    void multipleA2UIContentsViaCallback() {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        for (int i = 0; i < 10; i++) {
            callback.addA2UIContent(Map.of("surfaceUpdate",
                    Map.of("surfaceId", "surface-" + i)));
        }

        assertEquals(10, task.getStatus().getMessage().getParts().size());
        for (Part p : task.getStatus().getMessage().getParts()) {
            assertTrue(p instanceof DataPart);
            assertTrue(((DataPart) p).isA2UIData());
        }
    }

    // ── Context management (inherited) ───────────────────────────────

    @Test
    void contextManagement_shouldWorkLikeParent() {
        Task task = new Task();
        task.setId("test-task");

        callback.setContext(new AtomicReference<>(task));
        assertSame(task, callback.getContext().get());

        Task newTask = new Task();
        newTask.setId("new-task");
        callback.setContext(new AtomicReference<>(newTask));
        assertEquals("new-task", ((Task) callback.getContext().get()).getId());
    }

    // ── Comparison between A2A and A2UI callbacks ────────────────────

    @Test
    void a2aAndA2UICallbacks_shouldHaveDifferentTypes() {
        A2AActionCallBack a2a = new A2AActionCallBack();
        A2AUICallback a2ui = new A2AUICallback();

        assertNotEquals(a2a.getType(), a2ui.getType());
        assertEquals("A2A", a2a.getType());
        assertEquals("A2UI", a2ui.getType());
    }

    @Test
    void a2aAndA2UICallbacks_shouldBehaveSameForStatusAndA2UI() {
        A2AActionCallBack a2a = new A2AActionCallBack();
        A2AUICallback a2ui = new A2AUICallback();

        Task task1 = new Task();
        Task task2 = new Task();

        a2a.setContext(new AtomicReference<>(task1));
        a2ui.setContext(new AtomicReference<>(task2));

        a2a.sendtStatus("msg", ActionState.WORKING);
        a2ui.sendtStatus("msg", ActionState.WORKING);

        assertEquals(task1.getStatus().getState(), task2.getStatus().getState());
        assertEquals(
                ((TextPart) task1.getStatus().getMessage().getParts().get(0)).getText(),
                ((TextPart) task2.getStatus().getMessage().getParts().get(0)).getText());
    }
}
