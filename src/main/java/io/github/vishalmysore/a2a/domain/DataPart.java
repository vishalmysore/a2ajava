package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * A2A v1.0: DataPart serializes as {"data": {...}}
 * No "kind" or "type" field in JSON.
 * However, accepts "kind" during deserialization for compatibility with a2a-js SDK.
 * For A2UI support, set metadata mimeType to "application/json+a2ui"
 */
@ToString
public class DataPart extends Part {

    @JsonIgnore
    private String id;
    @JsonIgnore
    private String type = "data";
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String kind; // Accept during deserialization, ignore during serialization


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

    @JsonIgnore
    public String dataToText() {
        StringBuilder sb = new StringBuilder();

        sb.append("Type: ").append(type).append("\n");

        if (metadata != null && !metadata.isEmpty()) {
            sb.append("Metadata:\n");
            metadata.forEach((key, value) ->
                    sb.append("  ").append(key).append(": ").append(value).append("\n")
            );
        }

        if (data != null && !data.isEmpty()) {
            sb.append("Data:\n");
            data.forEach((key, value) ->
                    sb.append("  ").append(key).append(": ").append(value).append("\n")
            );
        }

        return sb.toString().trim();
    }
}
