package io.github.vishalmysore.common;

import com.t4a.detect.ActionCallback;

/**
 * This interface is used to define the callback type for the Model Context Protocol (MCP). This will be passed
 * to the AIProcessor to handle the callback for the MCP. The AI processor will set the context and other parameters
 * and pass it to the action clsas which can use it to set real time values
 */
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
