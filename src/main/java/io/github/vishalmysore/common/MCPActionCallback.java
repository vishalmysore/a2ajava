package io.github.vishalmysore.common;

import com.t4a.detect.ActionCallback;
import com.t4a.detect.ActionState;
import io.github.vishalmysore.a2a.domain.Task;
import io.github.vishalmysore.a2a.domain.TaskState;
import io.github.vishalmysore.mcp.domain.CallToolResult;
import io.github.vishalmysore.mcp.domain.DataContent;
import io.github.vishalmysore.mcp.domain.TextContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        List list =  ((CallToolResult) getContext()).getContent();
        if (list == null) {
            list = new ArrayList<>();
            ((CallToolResult) getContext()).setContent(list);
        }
        TextContent textContent = new TextContent();
        textContent.setType("text");
        textContent.setText(status+state.getValue());
        list.add(textContent);
    }
    
    /**
     * Add A2UI content to the MCP response.
     * This allows MCP servers to return UI components to clients that support A2UI extension.
     * 
     * @param a2uiMessage A2UI message structure (surfaceUpdate, beginRendering, etc.)
     * @see <a href="https://a2ui.org/specification/v0.8-a2ui/">A2UI Specification</a>
     */
    public void addA2UIContent(Map<String, Object> a2uiMessage) {
        List list = ((CallToolResult) getContext()).getContent();
        if (list == null) {
            list = new ArrayList<>();
            ((CallToolResult) getContext()).setContent(list);
        }
        DataContent dataContent = DataContent.createA2UIContent(a2uiMessage);
        list.add(dataContent);
    }
}
