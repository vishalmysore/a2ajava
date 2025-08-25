package io.github.vishalmysore.common;

import com.t4a.detect.ActionState;

public interface ACPActionCallback {
    
    void sendStatus(String message, ActionState state);
    
    void sendResult(Object result);
    
    void sendError(String error);
}
