package io.github.vishalmysore.common;

import io.github.vishalmysore.a2a.domain.SendTaskResponse;
import io.github.vishalmysore.a2a.domain.Task;
import io.github.vishalmysore.a2a.domain.TextPart;
import io.github.vishalmysore.mcp.domain.CallToolResult;
import io.github.vishalmysore.mcp.domain.TextContent;

public interface CommonClientResponse {

    default String getTextResult() {
        if (this instanceof CallToolResult) {
            CallToolResult toolResult = (CallToolResult) this;
            Object content = toolResult.getContent();

            if (content instanceof TextContent) {
                return ((TextContent) content).getText();
            }

            if (content instanceof java.util.List<?>) {
                java.util.List<?> list = (java.util.List<?>) content;
                if (!list.isEmpty() && list.get(0) instanceof TextContent) {
                    return ((TextContent) list.get(0)).getText();
                }
            }

            return "";
        } else if (this instanceof SendTaskResponse) {
            SendTaskResponse taskResponse = (SendTaskResponse) this;
            var status = taskResponse.getResult().getStatus();
            if (status != null && status.getMessage() != null) {
                var parts = status.getMessage().getParts();
                if (parts != null && !parts.isEmpty()) {
                    Object lastPart = parts.get(parts.size() - 1);
                    if (lastPart instanceof TextPart) {
                        return ((TextPart) lastPart).getText();
                    }
                }
            }

            return "";
        } else if (this instanceof Task) {
            Task task = (Task) this;
            var status = task.getStatus();
            if (status != null && status.getMessage() != null) {
                var parts = status.getMessage().getParts();
                if (parts != null && !parts.isEmpty()) {
                    Object lastPart = parts.get(parts.size() - 1);
                    if (lastPart instanceof TextPart) {
                        return ((TextPart) lastPart).getText();
                    }
                }
            }
            return "";
        } else {
            throw new IllegalStateException("Unexpected response type: " + this.getClass());
        }
    }


}
