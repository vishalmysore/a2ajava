package io.github.vishalmysore.mcp.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.t4a.JsonUtils;
import com.t4a.predict.PredictionLoader;
import com.t4a.processor.AIProcessingException;
import com.t4a.processor.AIProcessor;
import com.t4a.transform.PromptTransformer;
import io.github.vishalmysore.common.Agent;
import io.github.vishalmysore.common.AgentInfo;
import io.github.vishalmysore.common.CommonClientRequest;
import io.github.vishalmysore.common.CommonClientResponse;
import io.github.vishalmysore.mcp.domain.*;
import lombok.extern.java.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Log
public class MCPAgent implements Agent {
    private URL serverUrl;
    private ListToolsResult toolsResult;
    private JsonUtils utils = new JsonUtils();
    private String availableTools = "[]";

    private String type ="mcp";
    private ObjectMapper mapper;

    public MCPAgent() {
        this(null);
    }
    public MCPAgent(URL serverUrl) {
        this.serverUrl = serverUrl;
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    @Override
    public String getType() {
        return type;
    }

    @Override
    public CommonClientResponse remoteMethodCall(String query) {
        PromptTransformer processor = PredictionLoader.getInstance().createOrGetPromptTransformer();
        String toolNameJson = null;
        try {
            toolNameJson = processor.transformIntoJson("{toolName:''}", query);
        } catch (AIProcessingException e) {
            log.severe("Error processing query: " + e.getMessage());
        }
        toolNameJson = utils.extractJson(toolNameJson);
        JsonNode root = null;
        try {
            root = mapper.readTree(toolNameJson);
        } catch (JsonProcessingException e) {
            log.severe("Error processing query: " + e.getMessage());
        }

        JsonNode idNode = root.get("toolName");
        if (idNode == null || idNode.asText().trim().isEmpty()) {
            log.warning("No valid agent ID found in JSON");
            return null;
        }
        String methodName = idNode.asText();
        return remoteMethodCall(methodName, query);
    }

    @Override
    public CommonClientResponse remoteMethodCall(String methodName, String query) {
        CallToolRequest request = new CallToolRequest();
        request.putArgument("provideAllValuesInPlainEnglish", query);
        request.putArgument("name", methodName);
        return callTool(request);

    }

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }

    @Override
    public void connect(String url, String token) {
        try {
            serverUrl = new URL(url);
        } catch (MalformedURLException e) {
            log.severe("Invalid server URL: " + e.getMessage());
        }
        ListToolsRequest request = new ListToolsRequest();
        MCPGenericResponse<ListToolsResult> response = getRemoteData(request, new TypeReference<MCPGenericResponse<ListToolsResult>>() {});

        toolsResult =(ListToolsResult) response.getResult();
        if (toolsResult != null) {
            availableTools = toolsResult.retrieveToolList();
        }
    }

    @Override
    public void disconnect() {
      log.info("Disconnecting MCPAgent");
    }

    @Override
    public AgentInfo getInfo() {
        return toolsResult;
    }

    @Override
    public boolean isConnected() {
       return serverUrl!= null && toolsResult != null;
    }

    @Override
    public URL getServerUrl() {
        return serverUrl;
    }



    public CallToolResult  callTool(CallToolRequest request) {
        MCPGenericResponse<CallToolResult> response =  getRemoteData(request, new TypeReference<MCPGenericResponse<CallToolResult>>() {});
        return response.getResult();

    }
}
