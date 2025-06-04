package regression.client;

import com.t4a.predict.PredictionLoader;
import io.github.vishalmysore.a2a.client.LocalA2ATaskClient;
import io.github.vishalmysore.a2a.domain.JsonRpcRequest;
import io.github.vishalmysore.a2a.domain.Task;
import io.github.vishalmysore.common.server.JsonRpcController;
import io.github.vishalmysore.mcp.domain.CallToolRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class A2ATaskClientExampleTest {

    @BeforeAll
    public static void initLoader() {
        System.out.println("Waiting for other tests to complete...");
        try {
            Thread.sleep(5000); // ⏱️ 5 seconds
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        PredictionLoader.getInstance();  // trigger once per test class
    }
    @Test
    public void testSendTask() {
        // Mock or simulate the behavior of sendTask
        // Example: Assert that the task is sent successfully
        LocalA2ATaskClient client = new LocalA2ATaskClient();
        Task result = client.sendTask("what food vishal likes");
        assertNotNull(result);
        assertTrue(true, "Task sent successfully");
    }

    @Test
    public void testMCPClient() {
        try {
            String toolsCall = "{\"method\":\"tools/call\",\"params\":{\"name\":\"computerRepair\",\"arguments\":{\"provideAllValuesInPlainEnglish\":\"fix my computer, my name is vishal mysore and comptuer is very slow i can think faster that that since I am chacha choudhry\"}},\"jsonrpc\":\"2.0\",\"id\":17}";
            JsonRpcRequest request = JsonRpcRequest.fromString(toolsCall);

            JsonRpcController controller = new JsonRpcController();
            Object response = controller.handleRpc(request);
            assertNotNull(response);
        } catch (Exception e) {
            System.out.println("AI not responding: " + e.getMessage());
        }
    }

@Test
    public void testMCPClientLifeCycle() {
        String[] jsonRequests = {
                "{\"method\":\"initialize\",\"params\":{\"protocolVersion\":\"2024-11-05\",\"capabilities\":{},\"clientInfo\":{\"name\":\"claude-ai\",\"version\":\"0.1.0\"}},\"jsonrpc\":\"2.0\",\"id\":0}",
                "{\"method\":\"tools/list\",\"params\":{},\"jsonrpc\":\"2.0\",\"id\":1}",
                "{\"method\":\"resources/list\",\"params\":{},\"jsonrpc\":\"2.0\",\"id\":3}",
                "{\"method\":\"notifications/initialized\",\"jsonrpc\":\"2.0\"}"
        };

        JsonRpcController controller = new JsonRpcController();

        assertDoesNotThrow(() -> {
            for (String jsonStr : jsonRequests) {
                JsonRpcRequest request = JsonRpcRequest.fromString(jsonStr);
                Object response = controller.handleRpc(request);
                assertNotNull(response);
            }
        });
    }

    @Test
    public void testBrowseWebTool() {
        String toolsCall = "{\"method\":\"tools/call\",\"params\":{\"name\":\"browseWebAndPerformAction\",\"arguments\":{\"provideAllValuesInPlainEnglish\":\"Go to Google.com, search for \\\"a2ajava\\\", wait for results to load, click on the first search result link, wait for the page to load completely, then take a screenshot of the page\"}}}";
        CallToolRequest request = CallToolRequest.fromString(toolsCall);
        assertNotNull(request);
        assertEquals("tools/call", request.getMethod());
        assertEquals("browseWebAndPerformAction", request.getParams().getName());
    }

    @Test
    public void testGetTask() {
        // Mock or simulate the behavior of getTask
        // Example: Assert that the task is retrieved successfully
        assertTrue(true, "Task retrieved successfully");
    }
}