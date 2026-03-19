package io.github.vishalmysore.common;

import com.t4a.detect.ActionState;
import io.github.vishalmysore.a2a.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Advanced unit tests for A2AActionCallBack covering concurrency,
 * state machine transitions, context isolation, and edge cases.
 */
public class A2AActionCallBackAdvancedTest {

    private A2AActionCallBack callback;

    @BeforeEach
    void setUp() {
        callback = new A2AActionCallBack();
    }

    // ── Type contract ────────────────────────────────────────────────

    @Test
    void getType_shouldAlwaysReturnA2A() {
        assertEquals(CallBackType.A2A.name(), callback.getType());
        // Calling multiple times should be stable
        for (int i = 0; i < 100; i++) {
            assertEquals("A2A", callback.getType());
        }
    }

    // ── Context management ───────────────────────────────────────────

    @Test
    void setContext_thenGetContext_shouldReturnSameReference() {
        Task task = new Task();
        task.setId("task-ctx-1");
        AtomicReference<Object> ref = new AtomicReference<>(task);

        callback.setContext(ref);

        assertSame(ref, callback.getContext());
        assertSame(task, callback.getContext().get());
    }

    @Test
    void contextReplacementShouldNotLeakPrevious() {
        Task first = new Task();
        first.setId("first");
        Task second = new Task();
        second.setId("second");

        callback.setContext(new AtomicReference<>(first));
        assertEquals("first", ((Task) callback.getContext().get()).getId());

        callback.setContext(new AtomicReference<>(second));
        assertEquals("second", ((Task) callback.getContext().get()).getId());
    }

    @Test
    void setContextNull_shouldReturnNull() {
        callback.setContext(null);
        assertNull(callback.getContext());
    }

    // ── sendtStatus full state machine ───────────────────────────────

    @Test
    void sendtStatus_shouldMapAllActionStatesToTaskStates() {
        Map<ActionState, TaskState> expectedMapping = Map.of(
                ActionState.SUBMITTED, TaskState.SUBMITTED,
                ActionState.WORKING, TaskState.WORKING,
                ActionState.COMPLETED, TaskState.COMPLETED
        );

        for (var entry : expectedMapping.entrySet()) {
            Task task = new Task();
            callback.setContext(new AtomicReference<>(task));
            callback.sendtStatus("msg", entry.getKey());

            assertEquals(entry.getValue(), task.getStatus().getState(),
                    "ActionState." + entry.getKey() + " should map to TaskState." + entry.getValue());
        }
    }

    @Test
    void sendtStatus_shouldSetAgentRoleAndTextPart() {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        callback.sendtStatus("Processing order #42", ActionState.WORKING);

        TaskStatus status = task.getStatus();
        assertNotNull(status);
        Message message = status.getMessage();
        assertNotNull(message);
        assertEquals("agent", message.getRole());
        assertNotNull(message.getParts());
        assertEquals(1, message.getParts().size());
        assertTrue(message.getParts().get(0) instanceof TextPart);
        assertEquals("Processing order #42", ((TextPart) message.getParts().get(0)).getText());
    }

    @Test
    void sendtStatus_multipleCallsShouldOverwriteStatus() {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        callback.sendtStatus("step-1", ActionState.SUBMITTED);
        assertEquals(TaskState.SUBMITTED, task.getStatus().getState());

        callback.sendtStatus("step-2", ActionState.WORKING);
        assertEquals(TaskState.WORKING, task.getStatus().getState());
        assertEquals("step-2", ((TextPart) task.getStatus().getMessage().getParts().get(0)).getText());

        callback.sendtStatus("step-3", ActionState.COMPLETED);
        assertEquals(TaskState.COMPLETED, task.getStatus().getState());
        assertEquals("step-3", ((TextPart) task.getStatus().getMessage().getParts().get(0)).getText());
    }

    @Test
    void sendtStatus_withEmptyString() {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        callback.sendtStatus("", ActionState.WORKING);

        assertEquals("", ((TextPart) task.getStatus().getMessage().getParts().get(0)).getText());
    }

    @Test
    void sendtStatus_withSpecialCharacters() {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        String specialMsg = "Status: 100% done! <html>&amp;\"quotes\"</html>\nnewline\ttab";
        callback.sendtStatus(specialMsg, ActionState.COMPLETED);

        assertEquals(specialMsg, ((TextPart) task.getStatus().getMessage().getParts().get(0)).getText());
    }

    @Test
    void sendtStatus_withVeryLongMessage() {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        String longMsg = "x".repeat(100_000);
        callback.sendtStatus(longMsg, ActionState.WORKING);

        assertEquals(longMsg, ((TextPart) task.getStatus().getMessage().getParts().get(0)).getText());
    }

    // ── addA2UIContent ───────────────────────────────────────────────

    @Test
    void addA2UIContent_shouldCreateStatusAndMessageWhenBothNull() {
        Task task = new Task();
        assertNull(task.getStatus());

        callback.setContext(new AtomicReference<>(task));

        Map<String, Object> a2ui = Map.of("beginRendering", Map.of("surfaceId", "s1"));
        callback.addA2UIContent(a2ui);

        assertNotNull(task.getStatus());
        assertNotNull(task.getStatus().getMessage());
        assertEquals("agent", task.getStatus().getMessage().getRole());
        assertEquals(1, task.getStatus().getMessage().getParts().size());

        DataPart dp = (DataPart) task.getStatus().getMessage().getParts().get(0);
        assertTrue(dp.isA2UIData());
    }

    @Test
    void addA2UIContent_shouldPreserveExistingTextParts() {
        Task task = new Task();
        TaskStatus status = new TaskStatus(TaskState.WORKING);
        Message msg = new Message();
        msg.setRole("agent");
        TextPart tp = new TextPart();
        tp.setText("existing");
        msg.setParts(new ArrayList<>(List.of(tp)));
        status.setMessage(msg);
        task.setStatus(status);

        callback.setContext(new AtomicReference<>(task));
        callback.addA2UIContent(Map.of("surfaceUpdate", Map.of("surfaceId", "s1")));

        List<Part> parts = task.getStatus().getMessage().getParts();
        assertEquals(2, parts.size());
        assertTrue(parts.get(0) instanceof TextPart);
        assertTrue(parts.get(1) instanceof DataPart);
    }

    @Test
    void addA2UIContent_multipleCalls_shouldAccumulate() {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        for (int i = 0; i < 50; i++) {
            callback.addA2UIContent(Map.of("surfaceUpdate", Map.of("surfaceId", "s" + i)));
        }

        assertEquals(50, task.getStatus().getMessage().getParts().size());
        for (Part part : task.getStatus().getMessage().getParts()) {
            assertTrue(part instanceof DataPart);
            assertTrue(((DataPart) part).isA2UIData());
        }
    }

    @Test
    void addA2UIContent_shouldHandleDeeplyNestedStructure() {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        Map<String, Object> deep = new HashMap<>();
        Map<String, Object> current = deep;
        for (int i = 0; i < 20; i++) {
            Map<String, Object> child = new HashMap<>();
            current.put("level" + i, child);
            current = child;
        }
        current.put("leaf", "value");

        callback.addA2UIContent(deep);

        DataPart dp = (DataPart) task.getStatus().getMessage().getParts().get(0);
        assertTrue(dp.isA2UIData());
        assertNotNull(dp.getData());
    }

    @Test
    void addA2UIContent_afterSendtStatus_shouldAppendToNewMessage() {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        // sendtStatus replaces the entire status + message
        callback.sendtStatus("working", ActionState.WORKING);
        assertNotNull(task.getStatus().getMessage());

        // addA2UIContent should work with the message that sendtStatus created
        callback.addA2UIContent(Map.of("beginRendering", Map.of("surfaceId", "s1")));

        List<Part> parts = task.getStatus().getMessage().getParts();
        // sendtStatus creates a new message with 1 TextPart, then addA2UIContent adds to it
        assertTrue(parts.size() >= 1);
        boolean hasA2UI = parts.stream()
                .anyMatch(p -> p instanceof DataPart && ((DataPart) p).isA2UIData());
        assertTrue(hasA2UI);
    }

    // ── Concurrent access ────────────────────────────────────────────

    @Test
    void concurrentSendtStatus_shouldNotThrow() throws Exception {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        int threads = 20;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            final int idx = i;
            pool.submit(() -> {
                try {
                    callback.sendtStatus("status-" + idx, ActionState.WORKING);
                } finally {
                    latch.countDown();
                }
            });
        }

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        pool.shutdown();

        // Task should have some status set (last-writer-wins is acceptable)
        assertNotNull(task.getStatus());
        assertNotNull(task.getStatus().getMessage());
    }

    @Test
    void concurrentAddA2UIContent_shouldNotThrow() throws Exception {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        // Pre-create status with a synchronized list to avoid ConcurrentModificationException
        TaskStatus status = new TaskStatus(TaskState.WORKING);
        Message msg = new Message();
        msg.setRole("agent");
        msg.setParts(Collections.synchronizedList(new ArrayList<>()));
        status.setMessage(msg);
        task.setStatus(status);

        int threads = 20;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            final int idx = i;
            pool.submit(() -> {
                try {
                    callback.addA2UIContent(Map.of("surfaceUpdate", Map.of("surfaceId", "s" + idx)));
                } finally {
                    latch.countDown();
                }
            });
        }

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        pool.shutdown();

        assertEquals(threads, task.getStatus().getMessage().getParts().size());
    }

    // ── Multiple independent callback instances ──────────────────────

    @Test
    void independentCallbackInstances_shouldNotShareState() {
        A2AActionCallBack cb1 = new A2AActionCallBack();
        A2AActionCallBack cb2 = new A2AActionCallBack();

        Task task1 = new Task();
        task1.setId("t1");
        Task task2 = new Task();
        task2.setId("t2");

        cb1.setContext(new AtomicReference<>(task1));
        cb2.setContext(new AtomicReference<>(task2));

        cb1.sendtStatus("msg1", ActionState.WORKING);
        cb2.sendtStatus("msg2", ActionState.COMPLETED);

        assertEquals(TaskState.WORKING, task1.getStatus().getState());
        assertEquals(TaskState.COMPLETED, task2.getStatus().getState());
        assertEquals("msg1", ((TextPart) task1.getStatus().getMessage().getParts().get(0)).getText());
        assertEquals("msg2", ((TextPart) task2.getStatus().getMessage().getParts().get(0)).getText());
    }

    // ── Context with AtomicReference CAS ─────────────────────────────

    @Test
    void contextAtomicReference_compareAndSetShouldWork() {
        Task task1 = new Task();
        task1.setId("initial");
        Task task2 = new Task();
        task2.setId("updated");

        AtomicReference<Object> ref = new AtomicReference<>(task1);
        callback.setContext(ref);

        // External CAS on the reference should be visible to the callback
        boolean casResult = callback.getContext().compareAndSet(task1, task2);
        assertTrue(casResult);
        assertEquals("updated", ((Task) callback.getContext().get()).getId());
    }

    // ── Rapid state transitions ──────────────────────────────────────

    @Test
    void rapidStateTransitions_shouldAllSucceed() {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        ActionState[] states = {
                ActionState.SUBMITTED, ActionState.WORKING,
                ActionState.WORKING, ActionState.COMPLETED
        };

        for (int round = 0; round < 100; round++) {
            for (ActionState state : states) {
                callback.sendtStatus("round-" + round, state);
            }
        }

        // After the loop, the last state should be COMPLETED
        assertEquals(TaskState.COMPLETED, task.getStatus().getState());
    }

    // ── Mixed sendtStatus + addA2UIContent interleaving ──────────────

    @Test
    void interleavedStatusAndA2UI_shouldWorkCorrectly() {
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));

        // Status then A2UI then Status
        callback.sendtStatus("step1", ActionState.SUBMITTED);
        callback.addA2UIContent(Map.of("beginRendering", Map.of("surfaceId", "s1")));
        // sendtStatus replaces status entirely
        callback.sendtStatus("step2", ActionState.WORKING);

        assertEquals(TaskState.WORKING, task.getStatus().getState());
        assertEquals("step2", ((TextPart) task.getStatus().getMessage().getParts().get(0)).getText());
    }
}
