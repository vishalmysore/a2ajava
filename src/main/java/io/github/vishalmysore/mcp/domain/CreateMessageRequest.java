package io.github.vishalmysore.mcp.domain;

import java.util.List;
import java.util.Map;

/**
 * A request from the server to sample an LLM via the client.
 * The client has full discretion over which model to select.
 * The client should also inform the user before beginning sampling,
 * to allow them to inspect the request (human in the loop) and decide whether to approve it.
 */
public class CreateMessageRequest {
    private final String method = "sampling/createMessage";
    private Params params;

    public String getMethod() {
        return method;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public static class Params {
        /**
         * A request to include context from one or more MCP servers (including the caller),
         * to be attached to the prompt. The client MAY ignore this request.
         */
        private String includeContext;  // Use String instead of the enum
        /**
         * The maximum number of tokens to sample, as requested by the server.
         * The client MAY choose to sample fewer tokens than requested.
         */
        private Integer maxTokens;
        private List<SamplingMessage> messages;
        /**
         * Optional metadata to pass through to the LLM provider.
         * The format of this metadata is provider-specific.
         */
        private Map<String, Object> metadata;
        /**
         * The server's preferences for which model to select.
         * The client MAY ignore these preferences.
         */
        private ModelPreferences modelPreferences;
        private List<String> stopSequences;
        /**
         * An optional system prompt the server wants to use for sampling.
         * The client MAY modify or omit this prompt.
         */
        private String systemPrompt;
        private Double temperature;

        public String getIncludeContext() {
            return includeContext;
        }

        public void setIncludeContext(String includeContext) {
            this.includeContext = includeContext;
        }

        public Integer getMaxTokens() {
            return maxTokens;
        }

        public void setMaxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
        }

        public List<SamplingMessage> getMessages() {
            return messages;
        }

        public void setMessages(List<SamplingMessage> messages) {
            this.messages = messages;
        }

        public Map<String, Object> getMetadata() {
            return metadata;
        }

        public void setMetadata(Map<String, Object> metadata) {
            this.metadata = metadata;
        }

        public ModelPreferences getModelPreferences() {
            return modelPreferences;
        }

        public void setModelPreferences(ModelPreferences modelPreferences) {
            this.modelPreferences = modelPreferences;
        }

        public List<String> getStopSequences() {
            return stopSequences;
        }

        public void setStopSequences(List<String> stopSequences) {
            this.stopSequences = stopSequences;
        }

        public String getSystemPrompt() {
            return systemPrompt;
        }

        public void setSystemPrompt(String systemPrompt) {
            this.systemPrompt = systemPrompt;
        }

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }
    }
}
