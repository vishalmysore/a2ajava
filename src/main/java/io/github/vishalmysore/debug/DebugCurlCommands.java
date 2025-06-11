package io.github.vishalmysore.debug;

import com.t4a.JsonUtils;
import com.t4a.api.AIAction;
import com.t4a.api.GenericJavaMethodAction;
import com.t4a.api.GroupInfo;
import com.t4a.predict.PredictionLoader;
import com.t4a.processor.AIProcessingException;
import com.t4a.transform.PromptTransformer;
import lombok.extern.java.Log;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Log
public class DebugCurlCommands {
    public static Map<String,String> getCurlCommands(String[] args) throws AIProcessingException {
        Map<GroupInfo, String> groupActions = PredictionLoader.getInstance().getActionGroupList().getGroupActions();
        PromptTransformer transformer = PredictionLoader.getInstance().createOrGetPromptTransformer();
        Map<String, AIAction> predictions = PredictionLoader.getInstance().getPredictions();
        JsonUtils jsonUtils = new JsonUtils();
      //  StringBuilder realTimeDescription = new StringBuilder("This agent provides the following capabilities: ");

        String textPrmt = "your prompt goes here"; // You can set this dynamically
        Map<String,String> curlCommands = new HashMap();
        for (Map.Entry<GroupInfo, String> entry : groupActions.entrySet()) {
            GroupInfo group = entry.getKey();
            String[] actionNames = entry.getValue().split(",");

            for (String actionName : actionNames) {
                AIAction action = predictions.get(actionName.trim());
                if (action instanceof GenericJavaMethodAction) {
                    Method m = ((GenericJavaMethodAction) action).getActionMethod();
                    String methodName = m.getName();
                    String jsonParam= ((GenericJavaMethodAction) action).getActionParameters();
                    String json = jsonUtils.createJson("textPromptToTestThisMethod").toString();
                    json = (String)transformer.transformIntoJson(json,"This is the method name: " + methodName + " and the parameters are: " + jsonParam + " build a text prompt for this method with parameter values in english");
                    textPrmt = jsonUtils.getFieldValue(json, "textPromptToTestThisMethod");
                    textPrmt= textPrmt.replaceAll("[\"']", "");
                    // Build MCP curl with textPrmt variable
                    String curlMCP = "curl  \\\n" +
                            "  -H \"Content-Type: application/json\" \\\n" +
                            "  -d '{\n" +
                            "    \"jsonrpc\": \"2.0\",\n" +
                            "    \"method\": \"tools/call\",\n" +
                            "    \"params\": {\n" +
                            "      \"name\": \"" + methodName + "\",\n" +
                            "      \"arguments\": {\n" +
                            "        \"provideAllValuesInPlainEnglish\": {\n" +
                            "          \"value\": \"" + textPrmt + "\"\n" +
                            "        }\n" +
                            "      }\n" +
                            "    },\n" +
                            "    \"id\": 25\n" +
                            "  }' http://localhost:7860";

                    // Build A2A curl with textPrmt variable
                    String curlA2A = "curl -v \\\n" +
                            "  -H \"Content-Type: application/json\" \\\n" +
                            "  -d '{\n" +
                            "    \"jsonrpc\": \"2.0\",\n" +
                            "    \"method\": \"tasks/send\",\n" +
                            "    \"params\": {\n" +
                            "      \"id\": \"999789\",\n" +
                            "      \"sessionId\": \"sdf44555\",\n" +
                            "      \"message\": {\n" +
                            "        \"role\": \"user\",\n" +
                            "        \"parts\": [\n" +
                            "          {\n" +
                            "            \"type\": \"text\",\n" +
                            "            \"text\": \"" + textPrmt + "\",\n" +
                            "            \"metadata\": null\n" +
                            "          }\n" +
                            "        ]\n" +
                            "      }\n" +
                            "    }\n" +
                            "  }' http://localhost:7860";

                    log.info("MCP Curl Command for " + methodName + ":\n" + curlMCP);
                    curlCommands.put("MCP_"+methodName,curlMCP);
                    log.info("A2A Curl Command for " + methodName + ":\n" + curlA2A);
                    curlCommands.put("A2A_"+methodName,curlA2A);
                }
            }
        }
        curlCommands.put("agent-Card","curl http://localhost:7860/.well-known/agent.json");
        curlCommands.put("tool-list","curl -H \"Content-Type: application/json\" -d '{\"jsonrpc\":\"2.0\",\"method\":\"tools/list\",\"params\":{},\"id\":9}' http://localhost:7860");
        return curlCommands;
    }


}
