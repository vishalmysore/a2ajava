package io.github.vishalmysore.common;

import com.t4a.detect.ActionCallback;

public interface MCPActionCallback extends ActionCallback  {
    @Override
    default String setType(String type) {
        return CallBackType.MCP.name();
    }

    @Override
    default  String getType() {
        return CallBackType.MCP.name();
    };
}
