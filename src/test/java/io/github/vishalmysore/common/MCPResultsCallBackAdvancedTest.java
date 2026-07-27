package io.github.vishalmysore.common;

import com.t4a.detect.ActionState;
import io.github.vishalmysore.mcp.domain.CallToolResult;
import io.github.vishalmysore.mcp.domain.Content;
import io.github.vishalmysore.mcp.domain.DataContent;
import io.github.vishalmysore.mcp.domain.TextContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Advanced unit tests for MCPResultsCallBack covering inheritance behavior,
 * no-op sendtStatus override, context management, and A2UI support inherited
 * from MCPActionCallback.
 */
public class MCPResultsCallBackAdvancedTest {

    private MCPResultsCallBack callback;

    @BeforeEach
    void setUp() {
        callback = new MCPResultsCallBack();
    }

    // ── Type inheritance ─────────────────────────────────────────────

    @Test
    void getType_shouldReturnMCPFromParent() {
        assertEquals(CallBackType.MCP.name(), callback.getType());
        assertEquals("MCP", callback.getType());
    }

    @Test
    void shouldBeInstanceOfMCPActionCallback() {
        assertTrue(callback instanceof MCPActionCallback);
    }

    // ── No-op sendtStatus ────────────────────────────────────────────

    @Test
    void sendtStatus_shouldBeNoOp_contentRemainsNull() {
        CallToolResult result = new CallToolResult();
        callback.setContext(new AtomicReference<>(result));

        callback.sendtStatus("any status", ActionState.WORKING);
        callback.sendtStatus("another", ActionState.COMPLETED);

        // Content should remain null because sendtStatus is a no-op override
        assertNull(result.getContent());
    }

    @Test
    void sendtStatus_repeatedCalls_shouldNeverModifyContent() {
        CallToolResult result = new CallToolResult();
        callback.setContext(new AtomicReference<>(result));

        for (int i = 0; i < 100; i++) {
            callback.sendtStatus("status-" + i, ActionState.WORKING);
        }

        assertNull(result.getContent());
    }

    @Test
    void sendtStatus_withExistingContent_shouldNotModifyIt() {
        CallToolResult result = new CallToolResult();
        List<Content> existingContent = new ArrayList<>();
        TextContent tc = new TextContent();
        tc.setType("text");
        tc.setText("pre-existing");
        existingContent.add(tc);
        result.setContent(existingContent);

        callback.setContext(new AtomicReference<>(result));
        callback.sendtStatus("should not appear", ActionState.WORKING);

        assertEquals(1, result.getContent().size());
        assertEquals("pre-existing", ((TextContent) result.getContent().get(0)).getText());
    }

    @Test
    void sendtStatus_withNullArguments_shouldNotThrow() {
        CallToolResult result = new CallToolResult();
        callback.setContext(new AtomicReference<>(result));

        assertDoesNotThrow(() -> callback.sendtStatus(null, null));
        assertDoesNotThrow(() -> callback.sendtStatus(null, ActionState.WORKING));
        assertDoesNotThrow(() -> callback.sendtStatus("msg", null));
    }

    // ── Context management (overridden in MCPResultsCallBack) ────────

    @Test
    void contextShouldUseOwnFieldNotParent() {
        CallToolResult r1 = new CallToolResult();
        CallToolResult r2 = new CallToolResult();

        callback.setContext(new AtomicReference<>(r1));
        assertSame(r1, callback.getContext().get());

        callback.setContext(new AtomicReference<>(r2));
        assertSame(r2, callback.getContext().get());
    }

    @Test
    void setContextNull_shouldReturnNull() {
        callback.setContext(null);
        assertNull(callback.getContext());
    }

    @Test
    void contextIsolation_fromParentClass() {
        // Create parent and child and verify they don't interfere
        MCPActionCallback parent = new MCPActionCallback();
        MCPResultsCallBack child = new MCPResultsCallBack();

        CallToolResult pr = new CallToolResult();
        CallToolResult cr = new CallToolResult();

        parent.setContext(new AtomicReference<>(pr));
        child.setContext(new AtomicReference<>(cr));

        assertSame(pr, parent.getContext().get());
        assertSame(cr, child.getContext().get());
    }

    // ── addA2UIContent inherited from parent ─────────────────────────

    @Test
    void addA2UIContent_shouldWorkThroughInheritance() {
        CallToolResult result = new CallToolResult();
        callback.setContext(new AtomicReference<>(result));

        callback.addA2UIContent(Map.of("beginRendering", Map.of("surfaceId", "s1")));

        assertNotNull(result.getContent());
        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().get(0) instanceof DataContent);
        assertTrue(((DataContent) result.getContent().get(0)).isA2UIData());
    }

    @Test
    void addA2UIContent_whileSendtStatusIsNoOp_shouldStillWork() {
        CallToolResult result = new CallToolResult();
        callback.setContext(new AtomicReference<>(result));

        // sendtStatus is no-op
        callback.sendtStatus("ignored", ActionState.WORKING);
        assertNull(result.getContent());

        // addA2UIContent should still work via parent's implementation
        callback.addA2UIContent(Map.of("surfaceUpdate", Map.of("surfaceId", "s1")));

        assertNotNull(result.getContent());
        assertEquals(1, result.getContent().size());
        assertTrue(((DataContent) result.getContent().get(0)).isA2UIData());
    }

    @Test
    void mixedUsage_sendtStatusNoOp_andA2UIAccumulate() {
        CallToolResult result = new CallToolResult();
        callback.setContext(new AtomicReference<>(result));

        // These are all no-ops
        callback.sendtStatus("a", ActionState.SUBMITTED);
        callback.sendtStatus("b", ActionState.WORKING);

        // These should accumulate
        callback.addA2UIContent(Map.of("begin", Map.of("surfaceId", "s1")));
        callback.addA2UIContent(Map.of("update", Map.of("surfaceId", "s2")));

        assertEquals(2, result.getContent().size());
        for (Content c : result.getContent()) {
            assertTrue(c instanceof DataContent);
            assertTrue(((DataContent) c).isA2UIData());
        }
    }

    // ── Behavioral difference from parent ────────────────────────────

    @Test
    void behaviorDifference_parentAccumulatesStatus_childDoesNot() {
        MCPActionCallback parentCb = new MCPActionCallback();
        MCPResultsCallBack childCb = new MCPResultsCallBack();

        CallToolResult parentResult = new CallToolResult();
        CallToolResult childResult = new CallToolResult();

        parentCb.setContext(new AtomicReference<>(parentResult));
        childCb.setContext(new AtomicReference<>(childResult));

        parentCb.sendtStatus("parent ", ActionState.WORKING);
        childCb.sendtStatus("child ", ActionState.WORKING);

        // Parent should have accumulated content
        assertNotNull(parentResult.getContent());
        assertEquals(1, parentResult.getContent().size());

        // Child should have null content (no-op)
        assertNull(childResult.getContent());
    }
}
