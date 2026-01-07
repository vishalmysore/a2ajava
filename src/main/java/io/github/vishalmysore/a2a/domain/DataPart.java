package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * A2A v1.0: DataPart serializes as {"data": {...}}
 * No "kind" or "type" field in JSON.
 * For A2UI support, set metadata mimeType to "application/json+a2ui"
 */
@ToString
public class DataPart extends Part {

    @JsonIgnore
    private String id;
    @JsonIgnore
    private String type = "data";


    private Map<String, Object> data;

    private Map<String, String> metadata;

    public String getType() {
        return type;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setType(String type) {
        this.type = type;
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
     * Check if this DataPart contains A2UI data
     */
    @JsonIgnore
    public boolean isA2UIData() {
        return metadata != null && "application/json+a2ui".equals(metadata.get("mimeType"));
    }
    
    /**
     * Create a DataPart for A2UI messages
     */
    public static DataPart createA2UIPart(Map<String, Object> a2uiMessage) {
        DataPart part = new DataPart();
        part.setData(a2uiMessage);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("mimeType", "application/json+a2ui");
        part.setMetadata(metadata);
        return part;
    }
}
