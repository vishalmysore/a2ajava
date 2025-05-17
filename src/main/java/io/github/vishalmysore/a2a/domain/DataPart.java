package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
public class DataPart extends Part {


    @JsonIgnore
    private String id;
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
}
