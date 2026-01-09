package io.github.vishalmysore.mcp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * DataContent represents structured data in MCP responses.
 * Can be used for A2UI data by setting appropriate metadata.
 */
@Data
@ToString
public class DataContent implements Content {
    
    private String type = "data";
    private Map<String, Object> data;
    private Map<String, String> metadata;

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
    
    /**
     * Check if this DataContent contains A2UI data
     */
    @JsonIgnore
    public boolean isA2UIData() {
        return metadata != null && "application/json+a2ui".equals(metadata.get("mimeType"));
    }
    
    /**
     * Create a DataContent for A2UI messages
     * @param a2uiMessage The A2UI message structure (surfaceUpdate, beginRendering, etc.)
     * @return DataContent with A2UI metadata
     */
    public static DataContent createA2UIContent(Map<String, Object> a2uiMessage) {
        DataContent content = new DataContent();
        content.setData(a2uiMessage);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("mimeType", "application/json+a2ui");
        content.setMetadata(metadata);
        return content;
    }
}
