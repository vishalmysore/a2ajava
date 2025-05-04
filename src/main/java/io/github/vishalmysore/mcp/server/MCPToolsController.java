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
import io.github.vishalmysore.mcp.domain.*;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

//@//RestController
//@RequestMapping("/mcp")
@Log
@Service
public class MCPToolsController  {

    @Getter
    private ListToolsResult toolsResult;

    private AIProcessor baseProcessor = new GeminiV2ActionProcessor();

    private JsonUtils utils = new JsonUtils();
    public AIProcessor getBaseProcessor() {
        return baseProcessor;
    }

    @PostConstruct
    public void init() {

        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("tools4ai.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find tools4ai.properties");
            }
            properties.load(input);

            String provider = properties.getProperty("agent.provider");
            if ("openai".equals(provider)) {
                baseProcessor = new OpenAiActionProcessor();
            } else if ("gemini".equals(provider)) {
                baseProcessor = new GeminiV2ActionProcessor();
            } else {
                log.info("Provider not found defaulting to Gemini");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file", e);
        }
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

                    String jsonStr = methodAction.getActionParameters();
                    AIProcessor processor = getBaseProcessor();
                    String aiResponse = null;
                    try {
                        aiResponse = processor.query("I am giving you a json string check the parameters section and return the required fields including subfields as simple json, do not include any other commentary or special characters "+jsonStr);
                        aiResponse =utils.extractJson(aiResponse);
                        log.info(aiResponse);
                    } catch (AIProcessingException e) {
                        throw new RuntimeException(e);
                    }
                //    Method method = methodAction.getActionMethod();
                 //   Parameter[] methodParams = method.getParameters();

                    //for (Parameter param : methodParams) {
                      //  String paramName = param.getName();

                        // ToolParameters (for Claude/LLM)
                       // ToolParameter toolParam = new ToolParameter();
                       // toolParam.setType(getJsonType(param.getType()));
                       // toolParam.setDescription("Parameter: " + paramName);
                       // parameters.getProperties().put(paramName, toolParam);

                        // InputSchema (for MCP)
                        //ToolPropertySchema schema = new ToolPropertySchema();
                        //schema.setType(getJsonType(param.getType()));
                        //schema.setDescription("Parameter: " + paramName);
                        //schemaProperties.put(paramName, schema);

                        //if (!param.isAnnotationPresent(Nullable.class)) {
                          //  parameters.getRequired().add(paramName);
                            //requiredFields.add(paramName);
                        //}
                    //}

                    tool.setParameters(parameters);



                    String customParam = "provideAllValuesInPlainEnglish";

                    // ToolParameters (for Claude/LLM)
                    ToolParameter toolParam = new ToolParameter();
                    toolParam.setType("string");
                    toolParam.setDescription(aiResponse );
                    parameters.getProperties().put(customParam, toolParam);

                    // InputSchema (for MCP)
                    ToolPropertySchema schema = new ToolPropertySchema();
                    schema.setType("string");
                    schema.setDescription(aiResponse);
                    schemaProperties.put(customParam, schema);


                        parameters.getRequired().add(customParam);
                        requiredFields.add(customParam);
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


    public ResponseEntity<Map<String, List<Tool>>> listTools() {
        Map<String, List<Tool>> response = new HashMap<>();
        response.put("tools", toolsResult.getTools());
        return ResponseEntity.ok(response);
    }


    public CallToolResult callTool(@RequestBody ToolCallRequest request, ActionCallback callback) {
        Map<String, AIAction> predictions = PredictionLoader.getInstance().getPredictions();
        AIAction action = predictions.get(request.getName());
        AIProcessor processor = getBaseProcessor();
        try {
            Object result = processor.processSingleAction(request.toString(),action,new LoggingHumanDecision(),new LogginggExplainDecision(),callback);

            CallToolResult callToolResult = new CallToolResult();
            List<Content> content = new ArrayList<>();
            TextContent textContent = new TextContent();
            textContent.setText(result.toString());
            content.add(textContent);
            callToolResult.setContent(content);
            textContent.setType("text");
            callback.setContext(callToolResult);
            return callToolResult;
        } catch (AIProcessingException e) {
            log.severe(e.getMessage());
        }

        return null;
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