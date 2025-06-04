package regression;

import io.github.vishalmysore.a2a.client.A2AAgent;
import io.github.vishalmysore.a2a.client.A2ATaskClient;
import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.common.AgentInfo;
import io.github.vishalmysore.mcp.client.MCPAgent;
import io.github.vishalmysore.mcp.domain.CallToolRequest;
import io.github.vishalmysore.mcp.domain.CallToolResult;
import io.github.vishalmysore.mcp.domain.Content;
import lombok.extern.java.Log;

import java.util.List;

@Log
public class ConnectToServer {
    public static void main(String[] args) {
        A2AAgent a2aagent = new A2AAgent();
         //a2aagent.connect("https://vishalmysore-a2amcpspring.hf.space/");
       // AgentInfo card = a2aagent.getInfo();
       // A2ATaskClient taskClient = new A2ATaskClient("http://localhost:7860");
       // Object task =taskClient.sendTask("vishal is coming home what should i cook");
       // Object task = a2aagent.remoteMethodCall("vishal is coming home what should i cook");
       //log.info("Task result: " + task);
       a2aagent.connect("http://localhost:7860/");
        Object task = a2aagent.remoteMethodCall("get me list of car types");
        log.info("Task result: " + task);

     //   log.info("Connected to server: " + card);

     /*   MCPAgent mcpAgent = new MCPAgent();
        mcpAgent.connect("https://vishalmysore-a2amcpspring.hf.space/");
        AgentInfo mcpCard = mcpAgent.getInfo();
        log.info("Connected to MCP server: " + mcpCard);

        CallToolRequest request = new CallToolRequest();
        String json = "{\n" +
                "    \"method\": \"tools/call\",\n" +
                "    \"params\": {\n" +
                "        \"name\": \"whatThisPersonFavFood\",\n" +
                "        \"arguments\": {\n" +
                "            \"provideAllValuesInPlainEnglish\": \"vishal is coming home what should i cook\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\": 17\n" +
                "}";
        String toolsCall = "{\"method\":\"tools/call\",\"params\":{\"name\":\"browseWebAndPerformAction\",\"arguments\":{\"provideAllValuesInPlainEnglish\":\"Go to Google.com, search for \\\"a2ajava\\\", wait for results to load, click on the first search result link, wait for the page to load completely, then take a screenshot of the page\"}}}";
        request = CallToolRequest.fromString(json);

        CallToolResult result =mcpAgent.callTool(request);
        log.info("Tool call result: " + result);

        result = (CallToolResult) mcpAgent.remoteMethodCall("whatThisPersonFavFood", "vishal is coming home what should i cook");
        log.info("Tool call result: " + result);
        */

    }
}
