package io.github.vishalmysore.mcp.domain;

import io.github.vishalmysore.mcp.domain.ClientRequest;

public class CompleteRequest extends ClientRequest {
    private final String method = "completion/complete";
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
         * The argument's information
         */
        private Argument argument;
        private Object ref; // Can be PromptReference or ResourceReference.  Better to use Object

        public Argument getArgument() {
            return argument;
        }

        public void setArgument(Argument argument) {
            this.argument = argument;
        }

        public Object getRef() {
            return ref;
        }

        public void setRef(Object ref) {
            this.ref = ref;
        }
    }

    public static class Argument {
        /**
         * The name of the argument
         */
        private String name;
        /**
         * The value of the argument to use for completion matching.
         */
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}