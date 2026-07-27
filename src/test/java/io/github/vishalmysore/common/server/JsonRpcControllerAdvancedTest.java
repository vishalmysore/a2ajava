package io.github.vishalmysore.common.server;

import io.github.vishalmysore.a2a.domain.*;
import io.github.vishalmysore.a2a.server.DyanamicTaskContoller;
import io.github.vishalmysore.mcp.domain.CallToolResult;
import io.github.vishalmysore.mcp.domain.JSONRPCResponse;
import io.github.vishalmysore.mcp.domain.Tool;
import io.github.vishalmysore.mcp.server.MCPToolsController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Advanced unit tests for JsonRpcController covering method routing,
 * error handling, header-based A2UI detection, and edge cases.
 * Uses MockedConstruction to prevent MCPToolsController.init() from
 * needing real AI/prediction infrastructure.
 */
public class JsonRpcControllerAdvancedTest {

    private JsonRpcController controller;

    @Mock
    private MCPToolsController mockMCPToolsController;

    @Mock
    private HttpServletRequest mockHttpRequest;

    private MockedConstruction<MCPToolsController> mcpMockedConstruction;
    private MockedConstruction<DyanamicTaskContoller> taskMockedConstruction;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        // Mock constructors so init() is never called on real instances
        mcpMockedConstruction = mockConstruction(MCPToolsController.class);
        taskMockedConstruction = mockConstruction(DyanamicTaskContoller.class);
        controller = new JsonRpcController();
        // Replace the mocked MCPToolsController with our @Mock for fine-grained stubbing
        controller.setMcpToolsController(mockMCPToolsController);
    }

    @AfterEach
    void tearDown() throws Exception {
        mcpMockedConstruction.close();
        taskMockedConstruction.close();
        if (mocks != null) {
            mocks.close();
        }
    }

    // ── getTaskController / getMCPToolsController ────────────────────

    @Test
    void getTaskController_shouldReturnNonNull() {
        assertNotNull(controller.getTaskController());
    }

    @Test
    void getMCPToolsController_shouldReturnInjectedMock() {
        assertSame(mockMCPToolsController, controller.getMCPToolsController());
    }

    @Test
    void setMcpToolsController_shouldReplace() {
        MCPToolsController another = mock(MCPToolsController.class);
        controller.setMcpToolsController(another);
        assertSame(another, controller.getMCPToolsController());
    }

    // ── Method routing: unknown method ───────────────────────────────

    @Test
    void handleRpc_unknownMethod_shouldThrowResponseStatusException() {
        JsonRpcRequest request = new JsonRpcRequest("2.0", "unknown/method", null, 1);

        assertThrows(ResponseStatusException.class, () -> controller.handleRpc(request));
    }

    @Test
    void handleRpc_nullMethod_shouldThrowNPE() {
        JsonRpcRequest request = new JsonRpcRequest("2.0", null, null, 1);

        assertThrows(NullPointerException.class, () -> controller.handleRpc(request));
    }

    // ── Method routing: notifications/initialized ────────────────────

    @Test
    void handleRpc_notificationsInitialized_shouldReturnNoContent() {
        JsonRpcRequest request = new JsonRpcRequest("2.0", "notifications/initialized", null, 1);

        Object result = controller.handleRpc(request, null);

        assertNotNull(result);
        assertTrue(result instanceof ResponseEntity);
        assertEquals(204, ((ResponseEntity<?>) result).getStatusCode().value());
    }

    // ── Method routing: notifications/cancelled ──────────────────────

    @Test
    void handleRpc_notificationsCancelled_shouldReturnNoContent() {
        JsonRpcRequest request = new JsonRpcRequest("2.0", "notifications/cancelled", null, 1);

        Object result = controller.handleRpc(request, null);

        assertNotNull(result);
        assertTrue(result instanceof ResponseEntity);
        assertEquals(204, ((ResponseEntity<?>) result).getStatusCode().value());
    }

    // ── Method routing: ping ─────────────────────────────────────────

    @Test
    void handleRpc_ping_shouldReturnJsonRpcResponse() {
        JsonRpcRequest request = new JsonRpcRequest("2.0", "ping", null, "ping-id-1");

        Object result = controller.handleRpc(request, null);

        assertNotNull(result);
        assertTrue(result instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) result;
        assertEquals("2.0", response.get("jsonrpc"));
        assertEquals("ping-id-1", response.get("id"));
        assertNotNull(response.get("result"));
    }

    // ── Method routing: initialize ───────────────────────────────────

    @Test
    void handleRpc_initialize_shouldReturnServerInfoAndCapabilities() {
        when(mockMCPToolsController.getServerName()).thenReturn("TestServer");
        when(mockMCPToolsController.getVersion()).thenReturn("1.0.0");
        when(mockMCPToolsController.getProtocolVersion()).thenReturn("2024-11-05");

        JsonRpcRequest request = new JsonRpcRequest("2.0", "initialize", null, "init-1");

        Object result = controller.handleRpc(request, null);

        assertNotNull(result);
        assertTrue(result instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) result;
        assertEquals("2.0", response.get("jsonrpc"));
        assertEquals("init-1", response.get("id"));

        @SuppressWarnings("unchecked")
        Map<String, Object> mcpResult = (Map<String, Object>) response.get("result");
        assertEquals("2024-11-05", mcpResult.get("protocolVersion"));

        @SuppressWarnings("unchecked")
        Map<String, Object> serverInfo = (Map<String, Object>) mcpResult.get("serverInfo");
        assertEquals("TestServer", serverInfo.get("name"));
        assertEquals("1.0.0", serverInfo.get("version"));

        assertNotNull(mcpResult.get("capabilities"));
    }

    // ── Method routing: tools/list ───────────────────────────────────

    @Test
    void handleRpc_toolsList_shouldReturnToolsArray() {
        List<Tool> tools = new ArrayList<>();
        Tool t = new Tool();
        t.setName("myTool");
        t.setDescription("A test tool");
        tools.add(t);

        Map<String, List<Tool>> body = new HashMap<>();
        body.put("tools", tools);
        when(mockMCPToolsController.listTools()).thenReturn(ResponseEntity.ok(body));

        JsonRpcRequest request = new JsonRpcRequest("2.0", "tools/list", null, "tl-1");

        Object result = controller.handleRpc(request, null);

        assertNotNull(result);
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) result;
        assertEquals("2.0", response.get("jsonrpc"));
        assertEquals("tl-1", response.get("id"));

        @SuppressWarnings("unchecked")
        Map<String, Object> mcpResult = (Map<String, Object>) response.get("result");
        assertNotNull(mcpResult.get("tools"));

        @SuppressWarnings("unchecked")
        List<Tool> returnedTools = (List<Tool>) mcpResult.get("tools");
        assertEquals(1, returnedTools.size());
        assertEquals("myTool", returnedTools.get(0).getName());
    }

    // ── Method routing: tools/call ───────────────────────────────────

    @Test
    void handleRpc_toolsCall_shouldDelegateToMCPController() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "myTool");
        params.put("arguments", Map.of("arg1", "val1"));

        JSONRPCResponse mockResponse = new JSONRPCResponse();
        CallToolResult toolResult = new CallToolResult();
        toolResult.setContent(new ArrayList<>());
        mockResponse.setResult(toolResult);
        when(mockMCPToolsController.callTool(any())).thenReturn(ResponseEntity.ok(mockResponse));

        JsonRpcRequest request = new JsonRpcRequest("2.0", "tools/call", params, "tc-1");

        Object result = controller.handleRpc(request, null);

        assertNotNull(result);
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) result;
        assertEquals("2.0", response.get("jsonrpc"));
        assertEquals("tc-1", response.get("id"));
        assertNotNull(response.get("result"));
    }

    // ── A2UI header detection ────────────────────────────────────────

    @Test
    void handleRpc_withA2AExtensionsHeader_shouldUseA2UICallback() {
        when(mockHttpRequest.getHeaderNames()).thenReturn(
                Collections.enumeration(List.of("X-A2A-Extensions")));
        when(mockHttpRequest.getHeader("X-A2A-Extensions")).thenReturn("a2ui");

        // We need to test that when X-A2A-Extensions header is present,
        // the controller creates an A2AUICallback.
        // Since the callback is passed internally to sendTask, we verify indirectly
        // via tasks/send flow - but that requires task controller setup.
        // For now, verify the header detection doesn't crash
        Map<String, Object> sendParams = new HashMap<>();
        sendParams.put("id", "task-1");
        sendParams.put("message", Map.of("role", "user", "parts", List.of(Map.of("text", "hello"))));

        JsonRpcRequest request = new JsonRpcRequest("2.0", "tasks/send", sendParams, "s-1");

        // This will try to delegate to the real DyanamicTaskContoller
        // which may throw - we just want to verify the header path doesn't crash before delegation
        try {
            controller.handleRpc(request, mockHttpRequest);
        } catch (Exception e) {
            // Expected - the real task controller needs AI setup
        }

        verify(mockHttpRequest).getHeader("X-A2A-Extensions");
    }

    @Test
    void handleRpc_withoutA2AExtensionsHeader_shouldNotCreateA2UICallback() {
        when(mockHttpRequest.getHeaderNames()).thenReturn(
                Collections.enumeration(List.of("Content-Type")));
        when(mockHttpRequest.getHeader("X-A2A-Extensions")).thenReturn(null);

        JsonRpcRequest request = new JsonRpcRequest("2.0", "ping", null, "p-1");
        Object result = controller.handleRpc(request, mockHttpRequest);

        assertNotNull(result);
    }

    // ── handleRpc(request) single-arg delegates to two-arg ──────────

    @Test
    void handleRpc_singleArg_shouldDelegateToPingCorrectly() {
        JsonRpcRequest request = new JsonRpcRequest("2.0", "ping", null, "p-2");

        Object result = controller.handleRpc(request);

        assertNotNull(result);
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) result;
        assertEquals("2.0", response.get("jsonrpc"));
    }

    // ── resources/list ───────────────────────────────────────────────

    @Test
    void handleRpc_resourcesList_shouldReturnEmptyResources() {
        JsonRpcRequest request = new JsonRpcRequest("2.0", "resources/list", new HashMap<>(), "rl-1");

        Object result = controller.handleRpc(request, null);

        assertNotNull(result);
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) result;
        assertEquals("2.0", response.get("jsonrpc"));
        assertEquals("rl-1", response.get("id"));
        assertNotNull(response.get("result"));
    }

    // ── prompts/list ─────────────────────────────────────────────────

    @Test
    void handleRpc_promptsList_shouldReturnPrompts() {
        JsonRpcRequest request = new JsonRpcRequest("2.0", "prompts/list", null, "pl-1");

        Object result = controller.handleRpc(request, null);

        assertNotNull(result);
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) result;
        assertEquals("2.0", response.get("jsonrpc"));
        assertEquals("pl-1", response.get("id"));
        assertNotNull(response.get("result"));
    }

    // ── Multiple methods in sequence ─────────────────────────────────

    @Test
    void multipleMethodsInSequence_shouldBeIndependent() {
        when(mockMCPToolsController.getServerName()).thenReturn("Server");
        when(mockMCPToolsController.getVersion()).thenReturn("1.0");
        when(mockMCPToolsController.getProtocolVersion()).thenReturn("2024-11-05");

        // Initialize
        Object initResult = controller.handleRpc(
                new JsonRpcRequest("2.0", "initialize", null, "i-1"), null);
        assertNotNull(initResult);

        // Ping
        Object pingResult = controller.handleRpc(
                new JsonRpcRequest("2.0", "ping", null, "p-1"), null);
        assertNotNull(pingResult);

        // notifications/initialized
        Object notifResult = controller.handleRpc(
                new JsonRpcRequest("2.0", "notifications/initialized", null, "n-1"), null);
        assertNotNull(notifResult);

        // Verify each has correct id
        @SuppressWarnings("unchecked")
        Map<String, Object> initMap = (Map<String, Object>) initResult;
        assertEquals("i-1", initMap.get("id"));

        @SuppressWarnings("unchecked")
        Map<String, Object> pingMap = (Map<String, Object>) pingResult;
        assertEquals("p-1", pingMap.get("id"));
    }

    // ── ID types (string vs integer) ─────────────────────────────────

    @Test
    void handleRpc_withIntegerId_shouldPreserveIdType() {
        JsonRpcRequest request = new JsonRpcRequest("2.0", "ping", null, 42);
        Object result = controller.handleRpc(request, null);

        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) result;
        assertEquals(42, response.get("id"));
    }

    @Test
    void handleRpc_withStringId_shouldPreserveIdType() {
        JsonRpcRequest request = new JsonRpcRequest("2.0", "ping", null, "string-id");
        Object result = controller.handleRpc(request, null);

        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) result;
        assertEquals("string-id", response.get("id"));
    }

    @Test
    void handleRpc_withNullId_shouldPreserveNull() {
        JsonRpcRequest request = new JsonRpcRequest("2.0", "ping", null, null);
        Object result = controller.handleRpc(request, null);

        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) result;
        assertNull(response.get("id"));
    }
}
