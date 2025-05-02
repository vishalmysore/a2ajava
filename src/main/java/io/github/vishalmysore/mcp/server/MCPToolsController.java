package io.github.vishalmysore.mcp.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.t4a.api.AIAction;
import com.t4a.api.GenericJavaMethodAction;
import com.t4a.api.GroupInfo;
import com.t4a.predict.PredictionLoader;
import io.github.vishalmysore.mcp.domain.*;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

//@//RestController
//@RequestMapping("/mcp")
@Log
@Service
public class MCPToolsController {

    @Getter
    private ListToolsResult toolsResult;

    @PostConstruct
    public void init() {
        Map<GroupInfo, String> groupActions = PredictionLoader.getInstance().getActionGroupList().getGroupActions();
        List<Tool> tools = convertGroupActionsToTools(groupActions);

        toolsResult = new ListToolsResult();
        toolsResult.setTools(tools);
    }

    private List<Tool> convertGroupActionsToTools(Map<GroupInfo, String> groupActions) {
        List<Tool> tools = new ArrayList<>();
        Map<String, AIAction> predictions = PredictionLoader.getInstance().getPredictions();

        for (Map.Entry<GroupInfo, String> entry : groupActions.entrySet()) {
            GroupInfo group = entry.getKey();
            String[] actionNames = entry.getValue().split(",");

            for (String actionName : actionNames) {
                AIAction action = predictions.get(actionName.trim());
                if (action instanceof GenericJavaMethodAction) {
                    GenericJavaMethodAction methodAction = (GenericJavaMethodAction) action;
                    log.info("Processing action: " + actionName);
                    Tool tool = new Tool();
                    tool.setName(action.getActionName());
                    tool.setDescription(action.getDescription());

                    // Create MCP-compatible parameters object
                    ToolParameters parameters = new ToolParameters();
                    Map<String, ToolPropertySchema> schemaProperties = new HashMap<>();
                    List<String> requiredFields = new ArrayList<>();

                    Method method = methodAction.getActionMethod();
                    Parameter[] methodParams = method.getParameters();

                    for (Parameter param : methodParams) {
                        String paramName = param.getName();

                        // ToolParameters (for Claude/LLM)
                        ToolParameter toolParam = new ToolParameter();
                        toolParam.setType(getJsonType(param.getType()));
                        toolParam.setDescription("Parameter: " + paramName);
                        parameters.getProperties().put(paramName, toolParam);

                        // InputSchema (for MCP)
                        ToolPropertySchema schema = new ToolPropertySchema();
                        schema.setType(getJsonType(param.getType()));
                        schema.setDescription("Parameter: " + paramName);
                        schemaProperties.put(paramName, schema);

                        if (!param.isAnnotationPresent(Nullable.class)) {
                            parameters.getRequired().add(paramName);
                            requiredFields.add(paramName);
                        }
                    }

                    tool.setParameters(parameters);

                    ToolInputSchema inputSchema = new ToolInputSchema();
                    inputSchema.setProperties(schemaProperties);
                    inputSchema.setRequired(requiredFields);
                    tool.setInputSchema(inputSchema);

                    tools.add(tool);
                }
            }
        }
        return tools;
    }

    private String getJsonType(Class<?> type) {
        if (type == String.class) return "string";
        if (type == Integer.class || type == int.class
                || type == Double.class || type == double.class
                || type == Float.class || type == float.class) return "number";
        if (type == Boolean.class || type == boolean.class) return "boolean";
        if (type.isArray() || Collection.class.isAssignableFrom(type)) return "array";
        return "object";
    }

    @GetMapping("/list-tools")
    public ResponseEntity<Map<String, List<Tool>>> listTools() {
        Map<String, List<Tool>> response = new HashMap<>();
        response.put("tools", toolsResult.getTools());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/call-tool")
    public ResponseEntity<CallToolResult> callTool(@RequestBody ToolCallRequest request) {
        Map<String, AIAction> predictions = PredictionLoader.getInstance().getPredictions();
        AIAction action = predictions.get(request.getName());

        if (action == null) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "Tool not found: " + request.getName());
            CallToolResult errorResult = new CallToolResult();
            errorResult.setError(errorMap);
            return ResponseEntity.badRequest().body(errorResult);
        }

        try {
            if (action instanceof GenericJavaMethodAction) {
                GenericJavaMethodAction methodAction = (GenericJavaMethodAction) action;
                Method method = methodAction.getActionMethod();
                Object[] args = buildMethodArguments(method, methodAction.getActionParameters());
                Object result = method.invoke(methodAction.getActionInstance(), args);
                CallToolResult toolResult = new CallToolResult();
                toolResult.setResult(result);
                return ResponseEntity.ok(toolResult);
            }
            CallToolResult errorResult = new CallToolResult();
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "Unsupported action type");
            errorResult.setError(errorMap);
            return ResponseEntity.badRequest().body(errorResult);
        } catch (Exception e) {
            CallToolResult errorResult = new CallToolResult();
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "Unsupported action type");
            errorResult.setError(errorMap);
            return ResponseEntity.internalServerError().body(errorResult);
        }
    }
    private Object[] buildMethodArguments(Method method, String jsonStr) throws Exception {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonStr);

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            JsonNode paramJson = root.get(param.getName());

            if (paramJson != null) {
                args[i] = objectMapper.treeToValue(paramJson, param.getType());
            } else {
                args[i] = null;
            }
        }

        return args;
    }



}