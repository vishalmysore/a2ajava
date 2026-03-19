package io.github.vishalmysore.common;

import com.t4a.detect.ActionState;
import io.github.vishalmysore.mcp.domain.CallToolResult;
import io.github.vishalmysore.mcp.domain.Content;
import io.github.vishalmysore.mcp.domain.DataContent;
import io.github.vishalmysore.mcp.domain.TextContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Advanced unit tests for MCPActionCallback covering concurrency,
 * multi-status accumulation, A2UI interleaving, and edge cases.
 */
public class MCPActionCallbackAdvancedTest {

    private MCPActionCallback callback;

    @BeforeEach
    void setUp() {
        callback = new MCPActionCallback();
    }

    // ── Type contract ────────────────────────────────────────────────

    @Test
    void getType_shouldAlwaysReturnMCP() {
        assertEquals(CallBackType.MCP.name(), callback.getType());
        for (int i = 0; i < 100; i++) {
            assertEquals("MCP", callback.getType());
        }
    }

    // ── Context management ───────────────────────────────────────────

    @Test
    void contextReplacementShouldNotLeakPrevious() {
        CallToolResult first = new CallToolResult();
        CallToolResult second = new CallToolResult();

        callback.setContext(new AtomicReference<>(first));
        assertSame(first, callback.getContext().get());

        callback.setContext(new AtomicReference<>(second));
        assertSame(second, callback.getContext().get());
    }

    @Test
    void setContextNull_shouldReturnNull() {
        callback.setContext(null);
        assertNull(callback.getContext());
    }

    // ── sendtStatus accumulation ─────────────────────────────────────

    @Test
    void sendtStatus_shouldAccumulateTextContentEntries() {
        CallToolResult result = new CallToolResult();
        callback.setContext(new AtomicReference<>(result));

        callback.sendtStatus("First ", ActionState.SUBMITTED);
        callback.sendtStatus("Second ", ActionState.WORKING);
        callback.sendtStatus("Third ", ActionState.COMPLETED);

        List<Content> contents = result.getContent();
        assertNotNull(contents);
        assertEquals(3, contents.size());

        assertEquals("First submitted", ((TextContent) contents.get(0)).getText());
        assertEquals("Second working", ((TextContent) contents.get(1)).getText());
        assertEquals("Third completed", ((TextContent) contents.get(2)).getText());
    }

    @Test
    void sendtStatus_withNullContentList_shouldCreateNew() {
        CallToolResult result = new CallToolResult();
        result.setContent(null);
        callback.setContext(new AtomicReference<>(result));

        callback.sendtStatus("Test ", ActionState.WORKING);

        assertNotNull(result.getContent());
        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().get(0) instanceof TextContent);
    }

    @Test
    void sendtStatus_withExistingContent_shouldAppend() {
        CallToolResult result = new CallToolResult();
        List<Content> existing = new ArrayList<>();
        TextContent existingText = new TextContent();
        existingText.setType("text");
        existingText.setText("pre-existing");
        existing.add(existingText);
        result.setContent(existing);
        callback.setContext(new AtomicReference<>(result));

        callback.sendtStatus("New ", ActionState.COMPLETED);

        assertEquals(2, result.getContent().size());
        assertEquals("pre-existing", ((TextContent) result.getContent().get(0)).getText());
        assertEquals("New completed", ((TextContent) result.getContent().get(1)).getText());
    }

    @Test
    void sendtStatus_setsTypeToText() {
        CallToolResult result = new CallToolResult();
        callback.setContext(new AtomicReference<>(result));

        callback.sendtStatus("msg ", ActionState.WORKING);

        TextContent tc = (TextContent) result.getContent().get(0);
        assertEquals("text", tc.getType());
    }

    @Test
    void sendtStatus_withEmptyString() {
        CallToolResult result = new CallToolResult();
        callback.setContext(new AtomicReference<>(result));

        callback.sendtStatus("", ActionState.WORKING);

        assertEquals("working", ((TextContent) result.getContent().get(0)).getText());
    }

    @Test
    void sendtStatus_withSpecialCharacters() {
        CallToolResult result = new CallToolResult();
        callback.setContext(new AtomicReference<>(result));

        String special = "<html>&\"'\n\t\0";
        callback.sendtStatus(special, ActionState.COMPLETED);

        assertEquals(special + "completed", ((TextContent) result.getContent().get(0)).getText());
    }

    @Test
    void sendtStatus_largeNumberOfAccumulations() {
        CallToolResult result = new CallToolResult();
        callback.setContext(new AtomicReference<>(result));

        for (int i = 0; i < 1000; i++) {
            callback.sendtStatus("Status-" + i + " ", ActionState.WORKING);
        }

        assertEquals(1000, result.getContent().size());
        for (int i = 0; i < 1000; i++) {
            TextContent tc = (TextContent) result.getContent().get(i);
            assertEquals("Status-" + i + " working", tc.getText());
        }
    }

    // ── addA2UIContent ───────────────────────────────────────────────

    @Test
    void addA2UIContent_withNullContentList_shouldCreateNew() {
        CallToolResult result = new CallToolResult();
        result.setContent(null);
        callback.setContext(new AtomicReference<>(result));

        callback.addA2UIContent(Map.of("beginRendering", Map.of("surfaceId", "s1")));

        assertNotNull(result.getContent());
        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().get(0) instanceof DataContent);
        assertTrue(((DataContent) result.getContent().get(0)).isA2UIData());
    }

    @Test
    void addA2UIContent_shouldAccumulate() {
        CallToolResult result = new CallToolResult();
        callback.setContext(new AtomicReference<>(result));

        for (int i = 0; i < 25; i++) {
            callback.addA2UIContent(Map.of("surfaceUpdate", Map.of("surfaceId", "s" + i)));
        }

        assertEquals(25, result.getContent().size());
        for (Content c : result.getContent()) {
            assertTrue(c instanceof DataContent);
            assertTrue(((DataContent) c).isA2UIData());
        }
    }

    @Test
    void addA2UIContent_withDeeplyNestedStructure() {
        CallToolResult result = new CallToolResult();
        callback.setContext(new AtomicReference<>(result));

        Map<String, Object> deep = new HashMap<>();
        Map<String, Object> current = deep;
        for (int depth = 0; depth < 30; depth++) {
            Map<String, Object> child = new HashMap<>();
            current.put("level-" + depth, child);
            current = child;
        }
        current.put("leaf", "deepValue");

        callback.addA2UIContent(deep);

        assertEquals(1, result.getContent().size());
        DataContent dc = (DataContent) result.getContent().get(0);
        assertTrue(dc.isA2UIData());
        assertNotNull(dc.getData());
    }

    // ── Interleaved sendtStatus + addA2UIContent ─────────────────────

    @Test
    void interleavedStatusAndA2UI_shouldPreserveOrder() {
        CallToolResult result = new CallToolResult();
        callback.setContext(new AtomicReference<>(result));

        callback.sendtStatus("Step1 ", ActionState.SUBMITTED);
        callback.addA2UIContent(Map.of("beginRendering", Map.of("surfaceId", "s1")));
        callback.sendtStatus("Step2 ", ActionState.WORKING);
        callback.addA2UIContent(Map.of("surfaceUpdate", Map.of("surfaceId", "s1")));
        callback.sendtStatus("Step3 ", ActionState.COMPLETED);

        List<Content> contents = result.getContent();
        assertEquals(5, contents.size());

        assertTrue(contents.get(0) instanceof TextContent);
        assertTrue(contents.get(1) instanceof DataContent);
        assertTrue(contents.get(2) instanceof TextContent);
        assertTrue(contents.get(3) instanceof DataContent);
        assertTrue(contents.get(4) instanceof TextContent);

        assertEquals("Step1 submitted", ((TextContent) contents.get(0)).getText());
        assertTrue(((DataContent) contents.get(1)).isA2UIData());
        assertEquals("Step2 working", ((TextContent) contents.get(2)).getText());
        assertTrue(((DataContent) contents.get(3)).isA2UIData());
        assertEquals("Step3 completed", ((TextContent) contents.get(4)).getText());
    }

    // ── Concurrent access ────────────────────────────────────────────

    @Test
    void concurrentSendtStatus_shouldNotThrow() throws Exception {
        CallToolResult result = new CallToolResult();
        result.setContent(Collections.synchronizedList(new ArrayList<>()));
        callback.setContext(new AtomicReference<>(result));

        int threads = 20;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            final int idx = i;
            pool.submit(() -> {
                try {
                    callback.sendtStatus("t-" + idx + " ", ActionState.WORKING);
                } finally {
                    latch.countDown();
                }
            });
        }

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        pool.shutdown();

        assertEquals(threads, result.getContent().size());
    }

    @Test
    void concurrentAddA2UIContent_shouldNotThrow() throws Exception {
        CallToolResult result = new CallToolResult();
        result.setContent(Collections.synchronizedList(new ArrayList<>()));
        callback.setContext(new AtomicReference<>(result));

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

        assertEquals(threads, result.getContent().size());
    }

    // ── Multiple independent instances ───────────────────────────────

    @Test
    void independentInstances_shouldNotShareState() {
        MCPActionCallback cb1 = new MCPActionCallback();
        MCPActionCallback cb2 = new MCPActionCallback();

        CallToolResult r1 = new CallToolResult();
        CallToolResult r2 = new CallToolResult();

        cb1.setContext(new AtomicReference<>(r1));
        cb2.setContext(new AtomicReference<>(r2));

        cb1.sendtStatus("msg1 ", ActionState.WORKING);
        cb2.sendtStatus("msg2 ", ActionState.COMPLETED);

        assertEquals(1, r1.getContent().size());
        assertEquals(1, r2.getContent().size());
        assertEquals("msg1 working", ((TextContent) r1.getContent().get(0)).getText());
        assertEquals("msg2 completed", ((TextContent) r2.getContent().get(0)).getText());
    }

    // ── AtomicReference CAS visibility ───────────────────────────────

    @Test
    void contextAtomicReference_externalCAS_shouldBeVisible() {
        CallToolResult r1 = new CallToolResult();
        CallToolResult r2 = new CallToolResult();

        AtomicReference<Object> ref = new AtomicReference<>(r1);
        callback.setContext(ref);

        boolean swapped = callback.getContext().compareAndSet(r1, r2);
        assertTrue(swapped);
        assertSame(r2, callback.getContext().get());
    }
}
