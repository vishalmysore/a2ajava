package io.github.vishalmysore;

import java.util.HashMap;
import java.util.Map;

public class TextPart implements Part {
    private String type = "text";
    private String text;
    private Map<String, Object> metadata = new HashMap<>();

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void setText(String text) {
        this.text = text;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}