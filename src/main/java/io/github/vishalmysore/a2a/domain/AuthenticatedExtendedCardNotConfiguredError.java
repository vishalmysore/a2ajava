package io.github.vishalmysore.a2a.domain;


public class AuthenticatedExtendedCardNotConfiguredError extends JSONRPCError {
    private static final int ERROR_CODE = -32007;
    private static final String ERROR_MESSAGE = "Authenticated Extended Card is not configured";

    public AuthenticatedExtendedCardNotConfiguredError() {
        super(ERROR_CODE, ERROR_MESSAGE, null);
    }
}
