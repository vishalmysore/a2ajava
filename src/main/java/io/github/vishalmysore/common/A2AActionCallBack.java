package io.github.vishalmysore.common;

import com.t4a.detect.ActionCallback;

/**
 * A2AActionCallBack is an interface that extends ActionCallback.
 * It provides default implementations for the setType and getType methods,
 * setting the type to "A2A". The AI processor will pass this to the action class which can use
 * this to set the Task detaails .
 */
public interface A2AActionCallBack extends ActionCallback {
    @Override
    default String setType(String type) {
        return CallBackType.A2A.name();
    }

    @Override
    default  String getType() {
        return CallBackType.A2A.name();
    };
}
