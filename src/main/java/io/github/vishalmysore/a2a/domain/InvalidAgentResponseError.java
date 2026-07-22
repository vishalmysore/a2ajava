package io.github.vishalmysore.a2a.domain;


public class InvalidAgentResponseError extends JSONRPCError {
    private static final int ERROR_CODE = -32006;
    private static final String ERROR_MESSAGE = "Invalid agent response";

    public InvalidAgentResponseError() {
        super(ERROR_CODE, ERROR_MESSAGE, null);
    }
}
