package io.github.vishalmysore.a2a.domain;

import java.util.HashMap;
import java.util.Map;

public class DataPart implements Part {
    private String type = "data";
    private Map<String, Object> data;
    private Map<String, Object> metadata = new HashMap<>();

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

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
