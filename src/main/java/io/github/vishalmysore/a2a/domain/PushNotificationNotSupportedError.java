package io.github.vishalmysore.a2a.domain;


public class PushNotificationNotSupportedError extends JSONRPCError {
    private static final int ERROR_CODE = -32003;
    private static final String ERROR_MESSAGE = "Push Notification is not supported";

    public PushNotificationNotSupportedError() {
        super(ERROR_CODE, ERROR_MESSAGE, null);
    }
}