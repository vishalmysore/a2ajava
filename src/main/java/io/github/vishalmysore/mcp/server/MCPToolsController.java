package io.github.vishalmysore.mcp.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.t4a.JsonUtils;
import com.t4a.api.AIAction;
import com.t4a.api.GenericJavaMethodAction;
import com.t4a.api.GroupInfo;
import com.t4a.detect.ActionCallback;
import com.t4a.predict.PredictionLoader;
import com.t4a.processor.*;
import com.t4a.transform.PromptTransformer;
import io.github.vishalmysore.common.MCPActionCallback;
import io.github.vishalmysore.mcp.domain.*;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

//@//RestController
//@RequestMapping("/mcp")
@Log

public class MCPToolsController  {

    @Getter
    private ListToolsResult toolsResult;

    @Getter
    private ListResourcesResult resourcesResult;

    @Getter
    private ListPromptsResult promptsResult;

    private List<Prompt> prompts;

    @Getter
    private List<Resource> resources;
    private AIProcessor baseProcessor = new GeminiV2ActionProcessor();
    private PromptTransformer promptTransformer;

    private JsonUtils utils = new JsonUtils();
    public AIProcessor getBaseProcessor() {
        return baseProcessor;
    }

    @Getter
    @Setter
    private String serverName = "MCP Tools Server";

    @Getter
    @Setter
    private String version = "1.0.0";

    @Getter
    @Setter
    private String protocolVersion ="2024-11-05";

    public MCPToolsController(){

    }
    @PostConstruct
    public void init() {

        baseProcessor = PredictionLoader.getInstance().createOrGetAIProcessor();
        promptTransformer = PredictionLoader.getInstance().createOrGetPromptTransformer();
        Map<GroupInfo, String> groupActions = PredictionLoader.getInstance().getActionGroupList().getGroupActions();
        List<Tool> tools = convertGroupActionsToTools(groupActions);

        ListToolsResult newToolsResult = new ListToolsResult();
        newToolsResult.setTools(tools);
        storeListToolsResult(newToolsResult);
        storeListReources(resources);
        storeListPrompts(prompts);
        setProperties();
    }

    public void setProperties() {
        Map<Object, Object> tools4AIProperties = PredictionLoader.getInstance().getTools4AIProperties();

        // Set serverName if the property is not null or empty
        String serverName = (String) tools4AIProperties.get("mcp.tools.servername");
        if (serverName != null && !serverName.trim().isEmpty()) {
            setServerName(serverName);
        }

        // Set version if the property is not null or empty
        String version = (String) tools4AIProperties.get("mcp.tools.version");
        if (version != null && !version.trim().isEmpty()) {
            setVersion(version);
        }

        // Set protocolVersion if the property is not null or empty
        String protocolVersion = (String) tools4AIProperties.get("mcp.tools.protocolversion");
        if (protocolVersion != null && !protocolVersion.trim().isEmpty()) {
            setProtocolVersion(protocolVersion);
        }
    }

    public void storeListReources(List<Resource> resources){
        resourcesResult = new ListResourcesResult();
        resourcesResult.setResources(resources);

    }

    public void storeListPrompts(List<Prompt> prompts){
        promptsResult= new ListPromptsResult();
        promptsResult.setPrompts(prompts);

    }

    public void storeListToolsResult(ListToolsResult toolsResult) {
        this.toolsResult = toolsResult;
    }

    /**
     * This will be used to add resources to the tools in subclasses
     * @param result
     */
    public void addResources(ListResourcesResult result) {

    }

    public void addPrompts(ListPromptsResult result) {

    }
    public ResponseEntity<Map<String, String>> getServerConfig() {
        return ResponseEntity.ok(Map.of(
                "name", getServerName(),
                "version", getVersion()
        ));
    }

    private boolean validateSchema(ToolInputSchema schema) {
        // Example validation logic (replace with actual JSON schema validation if needed)
        if (schema.getProperties() == null || schema.getRequired() == null) {
            log.severe("Schema validation failed: Missing properties or required fields.");
            return false;
        }
        return true;
    }

    public void addResource(Method method) {
       Class clazz = method.getReturnType();
       if (Resource.class.isAssignableFrom(clazz)) {
           Resource r = new Resource();
           resources.add(r);
       }

    }

    public void addPrompt(Tool tool) {
        Prompt prompt = new Prompt();
       // prompt.setAnnotations(tool.getAnnotations());

    }

    /**
     * This can be implmented by subclasses to restrict the methods that can be used
     * @param method
     * @return
     */
    public boolean isMethodAllowed(Method method)    {
        return true;
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
                    Method m = methodAction.getActionMethod();

                    if(!isMethodAllowed(m))
                        continue;
                    addResource(m);

                    log.info("Processing action: " + actionName);
                    Tool tool = new Tool();
                    tool.setName(action.getActionName());
                    tool.setDescription(action.getDescription());

                    // Create MCP-compatible parameters object
                    ToolParameters parameters = new ToolParameters();
                    Map<String, ToolPropertySchema> schemaProperties = new HashMap<>();
                    List<String> requiredFields = new ArrayList<>();

                    String jsonStr = methodAction.getActionParameters();
                    AIProcessor processor = getBaseProcessor();
                    String aiResponse = null;
                    try {
                        aiResponse = processor.query("I am giving you a json string check the parameters section and return the required fields including subfields as simple json, do not include any other commentary, control or special characters " + jsonStr);
                        aiResponse = utils.extractJson(aiResponse);
                        log.info(aiResponse);
                    } catch (AIProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    String customParam = "provideAllValuesInPlainEnglish";

                    // ToolParameters (for Claude/LLM)
                    ToolParameter toolParam = new ToolParameter();
                    toolParam.setType("string");
                    toolParam.setDescription(aiResponse);
                    parameters.getProperties().put(customParam, toolParam);

                    // InputSchema (for MCP)
                    ToolPropertySchema schema = new ToolPropertySchema();
                    schema.setType("string");
                    schema.setDescription(aiResponse);
                    schema.setAdditionalProperties(new HashMap<>()); // Setting it to an empty map as required
                    schemaProperties.put(customParam, schema);

                    parameters.getRequired().add(customParam);
                    requiredFields.add(customParam);
                    ToolInputSchema inputSchema = new ToolInputSchema();
                    inputSchema.setProperties(schemaProperties);
                    inputSchema.setRequired(requiredFields);

                    // Validate schema
                    if (!validateSchema(inputSchema)) {
                        log.severe("Invalid schema for tool: " + tool.getName());
                        continue;
                    }

                    tool.setInputSchema(inputSchema);

                    ToolAnnotations toolAnnotations = null;
                    try {
                        toolAnnotations = (ToolAnnotations)promptTransformer.transformIntoPojo("this is the tool name "+actionName+" and here are the parameterts it takes "+ jsonStr +"  I want you to give name name value pair describing what this tool does so that people can indentify this tool better", ToolAnnotations.class);
                    } catch (AIProcessingException e) {
                        log.warning(e.getMessage());
                    }

                    tool.setAnnotations(toolAnnotations);
                    tools.add(tool);
                    addPrompt(tool);
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


    public ResponseEntity<Map<String, List<Tool>>> listTools() {
        Map<String, List<Tool>> response = new HashMap<>();
        response.put("tools", toolsResult.getTools());
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<JSONRPCResponse> callTool(@RequestBody ToolCallRequest request) {
        CallToolResult result = callToolWithCallback(request, new MCPActionCallback());
        log.info("Received result: " + result);
        JSONRPCResponse response = new JSONRPCResponse();
        response.setId(UUID.randomUUID().toString());
        response.setResult(result);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<JSONRPCResponse> callTool(@RequestBody ToolCallRequest request,ActionCallback callback) {
        CallToolResult result = callToolWithCallback(request, callback);
        log.info("Received result: " + result);
        JSONRPCResponse response = new JSONRPCResponse();
        response.setId(UUID.randomUUID().toString());
        response.setResult(result);
        return ResponseEntity.ok(response);
    }
    public CallToolResult callToolWithCallback(@RequestBody ToolCallRequest request, ActionCallback callback) {
        Map<String, AIAction> predictions = PredictionLoader.getInstance().getPredictions();
        AIAction action = predictions.get(request.getName());
        AIProcessor processor = getBaseProcessor();
        CallToolResult callToolResult = new CallToolResult();
        List<Content> content = new ArrayList<>();

        try {
            callback.setContext(callToolResult);
            Object result = processAction(request, callback, processor, action);

            if (result != null) {
                if(result instanceof  Content) {
                    content.add((Content) result);
                } else {
                    String resultStr = result.toString();
                    // Check if result is Base64 encoded
                        TextContent textContent = new TextContent();
                        textContent.setType("text");
                        textContent.setText(resultStr);
                        content.add(textContent);

                }
            } else {
                TextContent textContent = new TextContent();
                textContent.setType("text");
                textContent.setText("No result available");
                content.add(textContent);
            }
        } catch (AIProcessingException e) {
            TextContent textContent = new TextContent();
            textContent.setType("text");
            content.add(textContent);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            textContent.setText("Technical issue occurred while processing the request: " + sw.toString());
            log.severe(sw.toString());
        }

        callToolResult.setContent(content);
        return callToolResult;
    }

    private boolean isBase64(String str) {
        try {
            Base64.getDecoder().decode(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    protected Object processAction(ToolCallRequest request, ActionCallback callback, AIProcessor processor, AIAction action) throws AIProcessingException {
        Object result = processor.processSingleAction(request.toString(), action, new LoggingHumanDecision(), new LogginggExplainDecision(), callback);
        return result;
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