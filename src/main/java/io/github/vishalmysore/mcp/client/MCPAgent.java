package io.github.vishalmysore.mcp.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vishalmysore.common.Agent;
import io.github.vishalmysore.common.AgentInfo;
import io.github.vishalmysore.common.CommonClientRequest;
import io.github.vishalmysore.common.CommonClientResponse;
import io.github.vishalmysore.mcp.domain.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MCPAgent implements Agent {
    private URL serverUrl;
    private ListToolsResult toolsResult;
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
        throw new UnsupportedOperationException("Remote method calls without name are not supported in MCPAgent. Use specific methods for tool calls.");
    }

    @Override
    public CommonClientResponse remoteMethodCall(String methodName, String query) {
        CallToolRequest request = new CallToolRequest();
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("provideAllValuesInPlainEnglish", query);
        arguments.put("name", methodName);
        CallToolRequest.Params params = new CallToolRequest.Params();
        request.setParams(params);
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
            throw new RuntimeException(e);
        }
        ListToolsRequest request = new ListToolsRequest();
        MCPGenericResponse<ListToolsResult> response = getRemoteData(request, new TypeReference<MCPGenericResponse<ListToolsResult>>() {});

        toolsResult =(ListToolsResult) response.getResult();
    }

    @Override
    public void disconnect() {

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
