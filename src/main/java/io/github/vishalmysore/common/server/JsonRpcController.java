package io.github.vishalmysore.common.server;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.t4a.predict.PredictionLoader;
import io.github.vishalmysore.a2a.domain.*;
import io.github.vishalmysore.a2a.server.A2ARPCController;
import io.github.vishalmysore.a2a.server.A2ATaskController;
import io.github.vishalmysore.a2a.server.DyanamicTaskContoller;
import io.github.vishalmysore.mcp.domain.*;
import io.github.vishalmysore.mcp.server.MCPToolsController;
import lombok.extern.java.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JsonRpcController handles JSON-RPC requests and routes them to the appropriate methods in the TaskController.
 * This is the main entry point for the JSON-RPC API for Google A2A clients
 * You need to look at the sample to see how it works
 */

@Log
public class JsonRpcController implements A2ARPCController {



    /**
     * The DynamicTaskController is responsible for handling dynamic task-related operations.
     * IT can handle any task wheter ticket or food prefernce etc
     */
    private DyanamicTaskContoller dynamicTaskController;

    private MCPToolsController  mcpToolsController;

    public JsonRpcController() {
        dynamicTaskController = new DyanamicTaskContoller();
        mcpToolsController = new MCPToolsController();
    }

    public JsonRpcController(ApplicationContext applicationContext) {
        PredictionLoader.getInstance(applicationContext);
        dynamicTaskController = new DyanamicTaskContoller();
        mcpToolsController = new MCPToolsController();
    }

    public A2ATaskController getTaskController() {
        return dynamicTaskController;
    }

    public MCPToolsController getMCPToolsController() {
        return mcpToolsController;
    }

    public void setMcpToolsController(MCPToolsController mcpToolsController) {
        this.mcpToolsController = mcpToolsController;
    }

    /**
     * This method handles JSON-RPC requests. It is the main entry point for the JSON-RPC API.
     * Will optimize this method later
     * @param request
     * @return
     */
    public Object handleRpc(@RequestBody JsonRpcRequest request) {
        String method = request.getMethod();
        Object params = request.getParams();
        log.info(request.toString());
        preProcessing(method,params);
        Object result = null;
        switch (method) {
            case "tasks/send":
                TaskSendParams sendParams = new ObjectMapper().convertValue(params, TaskSendParams.class);
                result = getTaskController().sendTask(sendParams);
                postProcessing(method,result);
                return result;
            case "tasks/get":
                TaskQueryParams queryParams = new ObjectMapper().convertValue(params, TaskQueryParams.class);
                result = getTaskController().getTask(queryParams.getId(), queryParams.getHistoryLength());
                postProcessing(method,result);
                return result;
            case "tasks/sendSubscribe":
                TaskSendSubscribeParams sendSubscribeParams = new ObjectMapper().convertValue(params, TaskSendSubscribeParams.class);

                 result = getTaskController().sendSubscribeTask(sendSubscribeParams);
                postProcessing(method,result);
                return result;
            case "tasks/cancel":
                TaskCancelParams cancelParams = new ObjectMapper().convertValue(params, TaskCancelParams.class);
                result = getTaskController().cancelTask(cancelParams.getId());
                postProcessing(method,result);
                return result;
            case "tasks/setPushNotification":
                TaskSetPushNotificationParams setPushParams = new ObjectMapper().convertValue(params, TaskSetPushNotificationParams.class);

                result = getTaskController().setTaskPushNotification(setPushParams);
                postProcessing(method,result);
                return result;
            case "tasks/getPushNotification":
                TaskGetPushNotificationParams getPushParams = new ObjectMapper().convertValue(params, TaskGetPushNotificationParams.class);

                result = getTaskController().getTaskPushNotification(getPushParams);
                postProcessing(method,result);
                return result;
            case "tasks/resubscribe":
                TaskResubscriptionParams resubParams = new ObjectMapper().convertValue(params, TaskResubscriptionParams.class);
                result = getTaskController().resubscribeToTask(resubParams);
                postProcessing(method,result);
                return result;
            case "initialize":
               {
                Map<String, Object> response = new HashMap<>();
                Map<String, Object> mcpResult = new HashMap<>();
                Map<String, Object> serverInfo = new HashMap<>();
                Map<String, Object> capabilities = new HashMap<>();
                Map<String, Object> tools = new HashMap<>();
                serverInfo.put("name", "MCP Server");
                serverInfo.put("version", "1.0.0");

                capabilities.put("tools", tools);

                   mcpResult.put("protocolVersion", "2024-11-05");
                   mcpResult.put("serverInfo", serverInfo);
                   mcpResult.put("capabilities", capabilities);

                response.put("jsonrpc", "2.0");
                response.put("id", request.getId());
                response.put("result", mcpResult);
                postProcessing(method,response);
                return response;
            } case "notifications/initialized": {
                // For notifications, return null since no response is expected
                return ResponseEntity.noContent().build();
            } case "tools/list": {

                ResponseEntity<Map<String, List<Tool>>> toolsResponse = getMCPToolsController().listTools();

                Map<String, Object> response = new HashMap<>();
                Map<String, Object> mcpResult = new HashMap<>();

                // Create ListToolsResult structure
                mcpResult.put("tools", toolsResponse.getBody().get("tools"));
                mcpResult.put("_meta", new HashMap<>());

                // Wrap in JSON-RPC response
                response.put("jsonrpc", "2.0");
                response.put("id", request.getId());
                response.put("result", mcpResult);
                postProcessing(method,response);
                return response;
            } case "tools/call": {

                ToolCallRequest toolRequest = new ToolCallRequest();
                if (request.getParams() instanceof Map) {
                    Map<String, Object> mcpParams = (Map<String, Object>) request.getParams();
                    toolRequest.setName((String) mcpParams.get("name"));
                    toolRequest.setArguments((Map<String, Object>) mcpParams.get("arguments"));
                }
                // toolRequest.setName(request.getParams().get("name").toString());
                // toolRequest.setArguments(request.getParams().get("arguments"));

                ResponseEntity<JSONRPCResponse> toolResponse = getMCPToolsController().callTool(toolRequest);

                Map<String, Object> response = new HashMap<>();
                response.put("jsonrpc", "2.0");
                response.put("id", request.getId());
                response.put("result", toolResponse.getBody().getResult());
                postProcessing(method,response);
                return response;
            } case "resources/list": {
                ListResourcesRequest listResourcesRequest = new ObjectMapper().convertValue(request, ListResourcesRequest.class);

                ListResourcesResult listResourcesResult = new ListResourcesResult();
                // Initialize with empty collections

                listResourcesResult.setResources(new ArrayList<>());



                Map<String, Object> response = new HashMap<>();
                response.put("jsonrpc", "2.0");
                response.put("id", request.getId());
                response.put("result", listResourcesResult);
                getMCPToolsController().addResources(listResourcesResult);
                postProcessing(method, response);
                return response;
            } case "notifications/cancelled": {
                 return ResponseEntity.noContent().build();
            } case "prompts/list": {
                ListPromptsResult listPromptsResult = new ListPromptsResult();
                Map<String, Object> response = new HashMap<>();
                response.put("jsonrpc", "2.0");
                response.put("id", request.getId());
                response.put("result", listPromptsResult);
                getMCPToolsController().addPrompts(listPromptsResult);
                postProcessing(method, response);
                return response;
            }
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Method not found: " + method);
        }
    }
}