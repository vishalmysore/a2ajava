package io.github.vishalmysore.mesh;


import com.t4a.JsonUtils;
import com.t4a.predict.PredictionLoader;
import com.t4a.processor.AIProcessingException;
import com.t4a.processor.AIProcessor;
import com.t4a.transform.PromptTransformer;

import io.github.vishalmysore.common.Agent;
import io.github.vishalmysore.common.CommonClientResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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

    public CommonClientResponse pipeLineMesh(String query){
        return pipeLineMesh(query, null, 0);
    }

    /**
     * Processes a query using the Pipeline Mesh pattern.
     * in this method the agents process data in sequence, each handling a specific part of the workflow.
     * The method recursively processes the query until a complete response is obtained or no further processing is needed.
     * @param query The initial query to be processed.
     * @param previousResponse
     * @param depth Current recursion depth
     * @return
     */
    public CommonClientResponse pipeLineMesh(String query, CommonClientResponse previousResponse, int depth) {
        if (depth > 10) {
            log.warn("Max recursion depth of 10 reached, returning current response");
            return previousResponse != null ? previousResponse : agentCatalog.processQuery(query);
        }
        
        CommonClientResponse response = agentCatalog.processQuery(query);
        String jsonYesOrNo = null;
        String yesOrNoField = "is_User_Query_Answered_Fully_Yes_Or_No_Only";
        String pendingQuery = "if_No_Then_What_Is_Pending_Query";
        String explain = "explain_why_the_query_is_fully_answered_by_looking_at_each_part_of_the_response_and_query";
        if(response ==null) {
            return previousResponse;
        } else {
            try {
                jsonYesOrNo= promptTransformer.transformIntoJson(jsonUtils.createJson(yesOrNoField,pendingQuery,explain).toString(),"This is the Original query "+query+ " This is the response so far "+response +" ");
                log.info(jsonYesOrNo);
                String yesOrNo = jsonUtils.getFieldValueFromMultipleFields(jsonYesOrNo, yesOrNoField);
                if(yesOrNo.contains("Yes")) {
                    return response;
                } else {
                    String pendingQueryValue = jsonUtils.getFieldValueFromMultipleFields(jsonYesOrNo, pendingQuery);
                    response = pipeLineMesh("Pending query "+pendingQueryValue+ " response so far "+response.getTextResult() ,response, depth + 1);
                }

            } catch (AIProcessingException e) {
                log.warn(e.getMessage());
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
            jsonQueries = promptTransformer.transformIntoJson(
                    jsonUtils.createJson(subQueriesField).toString(),
                    "Original query: " + query + " Initial response: " + mainResponse.getTextResult()
            );

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
            jsonAnalysis = promptTransformer.transformIntoJson(
                    jsonUtils.createJson(knowledgeGapsField, expertAgentsField).toString(),
                    "Analyze knowledge gaps and required experts for: " + initialKnowledge.getTextResult()
            );

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

    /**
     * Processes a query using the Parallel Mesh pattern.
     * In this method, all agents are queried simultaneously and the best response is selected.
     * This pattern provides the fastest response time and allows comparison of different agent capabilities.
     * @param query The query to be processed by all agents
     * @return The best response selected by AI from all agent responses
     */
    public CommonClientResponse parallelMesh(String query) {
        if (query == null || query.trim().isEmpty()) {
            log.warn("Empty or null query provided to parallelMesh");
            return null;
        }

        // Get all agents from catalog
        List<Agent> allAgents = new ArrayList<>(agentCatalog.getAgents().values());
        
        if (allAgents.isEmpty()) {
            log.warn("No agents available in catalog");
            return null;
        }

        // Create fixed thread pool sized to number of agents
        ExecutorService executorService = Executors.newFixedThreadPool(allAgents.size());
        List<Future<CommonClientResponse>> futures = new ArrayList<>();

        try {
            // Submit query to all agents in parallel
            log.info("Submitting query to {} agents in parallel", allAgents.size());
            for (Agent agent : allAgents) {
                Future<CommonClientResponse> future = executorService.submit(() -> {
                    try {
                        log.debug("Querying agent: {}", agent.getType());
                        return agent.remoteMethodCall(query);
                    } catch (Exception e) {
                        log.warn("Error querying agent {}: {}", agent.getType(), e.getMessage());
                        return null;
                    }
                });
                futures.add(future);
            }

            // Collect results with timeout
            List<CommonClientResponse> responses = new ArrayList<>();
            for (int i = 0; i < futures.size(); i++) {
                try {
                    CommonClientResponse response = futures.get(i).get(30, TimeUnit.SECONDS);
                    if (response != null && response.getTextResult() != null && !response.getTextResult().trim().isEmpty()) {
                        responses.add(response);
                        log.debug("Received response from agent {}", i);
                    }
                } catch (TimeoutException e) {
                    log.warn("Agent {} timed out after 30 seconds", i);
                } catch (InterruptedException e) {
                    log.warn("Agent {} was interrupted", i);
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    log.warn("Agent {} threw exception: {}", i, e.getMessage());
                }
            }

            log.info("Received {} valid responses out of {} agents", responses.size(), allAgents.size());

            // Handle cases with no valid responses
            if (responses.isEmpty()) {
                log.warn("No valid responses received from any agent");
                return null;
            }

            // If only one response, return it directly
            if (responses.size() == 1) {
                return responses.get(0);
            }

            // Use AI to select the best response
            return selectBestResponse(query, responses);

        } finally {
            // Clean up thread pool
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Uses AI to select the best response from multiple agent responses.
     * @param query The original query
     * @param responses List of responses from different agents
     * @return The response deemed most appropriate by the AI
     */
    private CommonClientResponse selectBestResponse(String query, List<CommonClientResponse> responses) {
        try {
            // Build context with all responses
            StringBuilder allResponses = new StringBuilder();
            for (int i = 0; i < responses.size(); i++) {
                allResponses.append("Response ").append(i + 1).append(": ")
                           .append(responses.get(i).getTextResult())
                           .append("\n\n");
            }

            // Ask AI to select the best response
            String bestIndexField = "best_response_index_1_based";
            String reasonField = "reason_for_selection";
            String jsonSelection = promptTransformer.transformIntoJson(
                jsonUtils.createJson(bestIndexField, reasonField).toString(),
                "Original query: " + query + "\n\nAll responses:\n" + allResponses + 
                "\nSelect the response that best answers the query. Return the 1-based index."
            );

            String bestIndexStr = jsonUtils.getFieldValueFromMultipleFields(jsonSelection, bestIndexField);
            String reason = jsonUtils.getFieldValueFromMultipleFields(jsonSelection, reasonField);
            
            int bestIndex = Integer.parseInt(bestIndexStr.trim()) - 1; // Convert to 0-based
            
            if (bestIndex >= 0 && bestIndex < responses.size()) {
                log.info("Selected response {} out of {}. Reason: {}", bestIndex + 1, responses.size(), reason);
                return responses.get(bestIndex);
            } else {
                log.warn("Invalid index {} returned by AI, returning first response", bestIndex);
                return responses.get(0);
            }

        } catch (AIProcessingException e) {
            log.warn("Error selecting best response: {}, returning first response", e.getMessage());
            return responses.get(0);
        } catch (Exception e) {
            log.warn("Unexpected error selecting best response: {}, returning first response", e.getMessage());
            return responses.get(0);
        }
    }

}
