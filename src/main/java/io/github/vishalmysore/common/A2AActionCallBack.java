package io.github.vishalmysore.common;

import com.t4a.detect.ActionCallback;

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
