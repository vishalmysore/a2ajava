package io.github.vishalmysore.mcp.domain;


import io.github.vishalmysore.common.AgentInfo;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * The server's response to a tools/list request from the client.
 */
@Component
@ToString
@EqualsAndHashCode
public class ListToolsResult implements AgentInfo {
    /**
     * This result property is reserved by the protocol to allow clients and servers to attach additional metadata to their responses.
     */
    private Map<String, Object> _meta;
    private List<Tool> tools;

    public Map<String, Object> getMeta() {
        return _meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this._meta = meta;
    }

    public List<Tool> getTools() {
        return tools;
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }

    public String retrieveToolList() {
        if (tools == null || tools.isEmpty()) {
            return "[]";
        }
        StringBuilder jsonBuilder = new StringBuilder("[\n");
        for (int i = 0; i < tools.size(); i++) {
            Tool tool = tools.get(i);
            jsonBuilder.append("  {\n")
                    .append("    \"toolName\": \"").append(tool.getName().replace("\"", "\\\"")).append("\",\n")
                    .append("    \"toolDescription\": \"").append(tool.getDescription().replace("\"", "\\\"")).append("\"\n")
                    .append("  }");
            if (i < tools.size() - 1) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append("\n");
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
}