package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * A2A v1.0: TextPart serializes as {"text": "..."}
 * No "kind" or "type" field in JSON.
 * However, accepts "kind" during deserialization for compatibility with a2a-js SDK.
 */
@ToString
public class TextPart extends Part {

    @JsonIgnore
    private String id;
    @JsonIgnore
    private String type = "text";
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String kind; // Accept during deserialization, ignore during serialization
    private String text;


    private Map<String, String> metadata = new HashMap<>();

    // Update getter and setter to use String instead of Object
    @Override
    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    // Other getters and setters remain the same
    @Override
    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }
}