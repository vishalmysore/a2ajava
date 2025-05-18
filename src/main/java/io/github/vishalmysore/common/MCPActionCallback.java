package io.github.vishalmysore.common;

import com.t4a.detect.ActionCallback;
import com.t4a.detect.ActionState;
import io.github.vishalmysore.a2a.domain.Task;
import io.github.vishalmysore.a2a.domain.TaskState;
import io.github.vishalmysore.mcp.domain.CallToolResult;

/**
 * This interface is used to define the callback type for the Model Context Protocol (MCP). This will be passed
 * to the AIProcessor to handle the callback for the MCP. The AI processor will set the context and other parameters
 * and pass it to the action clsas which can use it to set real time values
 */
public class MCPActionCallback implements ActionCallback  {

    private String status;
    private Object context;
    @Override
    public String setType(String type) {
        return CallBackType.MCP.name();
    }

    @Override
    public void setContext(Object context) {
     this.context= context;
    }

    @Override
    public Object getContext() {
        return context;
    }

    @Override
    public  String getType() {
        return CallBackType.MCP.name();
    };

    @Override
    public void sendtStatus(String status, ActionState state) {
        ((CallToolResult) getContext()).getContent().get(0);
    }
}
