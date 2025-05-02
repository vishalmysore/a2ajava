package io.github.vishalmysore.mcp.domain;

import java.util.Map;

public class GetPromptRequest extends ClientRequest {
    private final String method = "prompts/get";
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
         * Arguments to use for templating the prompt.
         */
        private Map<String, String> arguments;
        /**
         * The name of the prompt or prompt template.
         */
        private String name;

        public Map<String, String> getArguments() {
            return arguments;
        }

        public void setArguments(Map<String, String> arguments) {
            this.arguments = arguments;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
