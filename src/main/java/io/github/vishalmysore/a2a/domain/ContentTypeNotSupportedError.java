package io.github.vishalmysore.a2a.domain;


public class ContentTypeNotSupportedError extends JSONRPCError {
    private static final int ERROR_CODE = -32005;
    private static final String ERROR_MESSAGE = "Incompatible content types";

    public ContentTypeNotSupportedError() {
        super(ERROR_CODE, ERROR_MESSAGE, null);
    }
}
