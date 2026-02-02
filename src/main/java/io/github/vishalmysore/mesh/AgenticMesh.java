package io.github.vishalmysore.mesh;


import com.t4a.JsonUtils;
import com.t4a.predict.PredictionLoader;
import com.t4a.processor.AIProcessingException;
import com.t4a.processor.AIProcessor;
import com.t4a.transform.PromptTransformer;

import io.github.vishalmysore.common.CommonClientResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * AgenticMesh is a class that implements various AI processing patterns such as Pipeline Mesh, Hub and Spoke, and Blackboard.
 * It uses an AgentCatalog to process queries and an AIProcessor to handle AI-related tasks.
 * The class also utilizes a PromptTransformer for transforming prompts into JSON format.
 */
@Slf4j
public class AgenticMesh {
    private final AgentCatalog agentCatalog;
    private final AIProcessor aiProcessor;
    private final PromptTransformer promptTransformer;
    private final JsonUtils jsonUtils = new JsonUtils();

    public AgenticMesh(AgentCatalog agentCatalog) {
        this.agentCatalog = agentCatalog;
        this.aiProcessor = PredictionLoader.getInstance().createOrGetAIProcessor();
        this.promptTransformer = PredictionLoader.getInstance().createOrGetPromptTransformer();

    }

    /**
     * Extracts the first valid JSON object from a response that may contain multiple JSONs or extra text.
     * Handles markdown code blocks and commentary from LLM.
     * @param response The response string that may contain multiple JSON objects
     * @return First valid JSON object as string, or original response if no JSON found
     */
    private String extractFirstJsonObject(String response) {
        if (response == null || response.trim().isEmpty()) {
            return response;
        }
        
        String cleaned = response.trim();
        
        // Remove markdown code blocks first
        if (cleaned.contains("```json")) {
            int startIdx = cleaned.indexOf("```json") + 7;
            int endIdx = cleaned.indexOf("```", startIdx);
            if (endIdx > startIdx) {
                cleaned = cleaned.substring(startIdx, endIdx).trim();
            }
        } else if (cleaned.startsWith("```")) {
            int startIdx = cleaned.indexOf("```") + 3;
            int endIdx = cleaned.indexOf("```", startIdx);
            if (endIdx > startIdx) {
                cleaned = cleaned.substring(startIdx, endIdx).trim();
            }
        }
        
        // Find first { and matching }
        int firstBrace = cleaned.indexOf('{');
        if (firstBrace == -1) {
            return response;
        }
        
        int braceCount = 0;
        int endIdx = -1;
        
        for (int i = firstBrace; i < cleaned.length(); i++) {
            char c = cleaned.charAt(i);
            if (c == '{') {
                braceCount++;
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0) {
                    endIdx = i;
                    break;
                }
            }
        }
        
        if (endIdx > firstBrace) {
            return cleaned.substring(firstBrace, endIdx + 1);
        }
        
        return response;
    }

    public CommonClientResponse pipeLineMesh(String query){
        return pipeLineMesh(query, null);
    }

    /**
     * Processes a query using the Pipeline Mesh pattern.
     * in this method the agents process data in sequence, each handling a specific part of the workflow.
     * The method recursively processes the query until a complete response is obtained or no further processing is needed.
     * @param query The initial query to be processed.
     * @param previousResponse
     * @return
     */
     public CommonClientResponse pipeLineMesh(String query, CommonClientResponse previousResponse) {
        CommonClientResponse response = agentCatalog.processQuery(query);
        String jsonYesOrNo = null;
        String yesOrNoField = "is_User_Query_Answered_Fully_Yes_Or_No_Only";
         String pendingQuery = "if_No_Then_What_Is_Pending_Query";
        if(response ==null) {
             return previousResponse;
        } else {
            try {
                String prompt = "Original query: " + query + " response: " + response + ". IMPORTANT: Return ONLY a single JSON object, no explanations, no multiple JSONs, no markdown formatting.";
                jsonYesOrNo = promptTransformer.transformIntoJson(
                    jsonUtils.createJson(yesOrNoField, pendingQuery).toString(), 
                    prompt
                );
                
                // Extract first valid JSON object
                jsonYesOrNo = extractFirstJsonObject(jsonYesOrNo);
                log.debug("Extracted JSON: {}", jsonYesOrNo);

                String yesOrNo = jsonUtils.getFieldValueFromMultipleFields(jsonYesOrNo, yesOrNoField);
                if(yesOrNo != null && yesOrNo.contains("Yes")) {
                    return response;
                } else if(yesOrNo != null) {
                    String pendingQueryValue = jsonUtils.getFieldValueFromMultipleFields(jsonYesOrNo, pendingQuery);
                    if(pendingQueryValue != null && !pendingQueryValue.trim().isEmpty()) {
                        response = pipeLineMesh("Pending query: " + pendingQueryValue + " previous response: " + response.getTextResult(), response);
                    } else {
                        return response;
                    }
                } else {
                    log.warn("Could not extract yes/no answer, returning current response");
                    return response;
                }

            } catch (AIProcessingException e) {
               log.warn("AIProcessingException in pipeLineMesh: {}", e.getMessage());
               return response;
            } catch (Exception e) {
               log.error("Unexpected error in pipeLineMesh: {}", e.getMessage(), e);
               return response;
            }

        }

        return response;
     }

    /**
     * Processes a query using the Hub and Spoke pattern.
     * In this method, the main agent processes the query and identifies sub-queries needed to complete the answer.
     * @param query
     * @return
     */
    public CommonClientResponse hubAndSpoke(String query) {
        CommonClientResponse mainResponse = agentCatalog.processQuery(query);
        String jsonQueries = null;
        String subQueriesField = "list_of_sub_queries_needed_to_complete_the_answer";

        if (mainResponse == null) {
            return null;
        }

        try {
            // Get list of sub-queries needed
            String prompt = "Original query: " + query + " Initial response: " + mainResponse.getTextResult() + ". IMPORTANT: Return ONLY a single JSON object, no explanations.";
            jsonQueries = promptTransformer.transformIntoJson(
                    jsonUtils.createJson(subQueriesField).toString(),
                    prompt
            );
            
            jsonQueries = extractFirstJsonObject(jsonQueries);

            String subQueriesString = jsonUtils.getFieldValueFromMultipleFields(jsonQueries, subQueriesField);
            if (subQueriesString == null || subQueriesString.isEmpty()) {
                return mainResponse;
            }

            // Convert comma-separated queries to list
            String[] subQueries = subQueriesString.split(",");
            StringBuilder aggregatedResponse = new StringBuilder(mainResponse.getTextResult());

            // Process each sub-query and aggregate responses
            for (String subQuery : subQueries) {
                CommonClientResponse subResponse = agentCatalog.processQuery(subQuery.trim());
                if (subResponse != null) {
                    aggregatedResponse.append("\n").append(subResponse.getTextResult());
                }
            }

            // Final processing to combine all responses
            return agentCatalog.processQuery("Combine and summarize: " + aggregatedResponse);

        } catch (AIProcessingException e) {
            return mainResponse;
        }
    }

    /**
     * Processes a query using the Blackboard pattern.
     * In this method, the initial knowledge is processed, and then expert agents are queried to fill knowledge gaps.
     * The contributions from each expert are aggregated into a blackboard for final synthesis.
     * @param query
     * @return
     */

    public CommonClientResponse blackboard(String query) {
        CommonClientResponse initialKnowledge = agentCatalog.processQuery(query);
        String jsonAnalysis = null;
        String knowledgeGapsField = "knowledge_gaps_to_investigate";
        String expertAgentsField = "required_expert_agents";

        if (initialKnowledge == null) {
            return null;
        }

        try {
            // Identify knowledge gaps and required expert agents
            String prompt = "Analyze knowledge gaps and required experts for: " + initialKnowledge.getTextResult() + ". IMPORTANT: Return ONLY a single JSON object, no explanations.";
            jsonAnalysis = promptTransformer.transformIntoJson(
                    jsonUtils.createJson(knowledgeGapsField, expertAgentsField).toString(),
                    prompt
            );
            
            jsonAnalysis = extractFirstJsonObject(jsonAnalysis);

            String gaps = jsonUtils.getFieldValueFromMultipleFields(jsonAnalysis, knowledgeGapsField);
            String experts = jsonUtils.getFieldValueFromMultipleFields(jsonAnalysis, expertAgentsField);

            if (gaps == null || experts == null) {
                return initialKnowledge;
            }

            // Knowledge base to store all contributions
            StringBuilder blackboard = new StringBuilder(initialKnowledge.getTextResult());
            String[] requiredExperts = experts.split(",");
            String[] knowledgeGaps = gaps.split(",");

            // Each expert contributes their specialized knowledge
            for (int i = 0; i < requiredExperts.length && i < knowledgeGaps.length; i++) {
                String expertQuery = String.format("As a %s expert, address: %s\nCurrent knowledge: %s",
                        requiredExperts[i].trim(),
                        knowledgeGaps[i].trim(),
                        blackboard.toString());

                CommonClientResponse expertResponse = agentCatalog.processQuery(expertQuery);
                if (expertResponse != null) {
                    blackboard.append("\n\nExpert ").append(requiredExperts[i].trim())
                            .append(" contribution:\n").append(expertResponse.getTextResult());
                }
            }

            // Final synthesis of all contributions
            return agentCatalog.processQuery(
                    "Synthesize all expert contributions into a coherent response:\n" + blackboard.toString()
            );

        } catch (AIProcessingException e) {

            return initialKnowledge;
        }
    }

}
