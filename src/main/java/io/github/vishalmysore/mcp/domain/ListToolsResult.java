package io.github.vishalmysore.mcp.domain;


import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * The server's response to a tools/list request from the client.
 */
@Component
public class ListToolsResult {
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
}