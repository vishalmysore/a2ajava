package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.Map;

/**
 * A2A v1.0: FilePart serializes as {"file": {...}}
 * No "kind" or "type" field in JSON.
 * However, accepts "kind" during deserialization for compatibility with a2a-js SDK.
 */
@ToString
public class FilePart extends Part {

    @JsonIgnore
    private String id;
    @JsonIgnore
    private String type = "file";
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String kind; // Accept during deserialization, ignore during serialization

    private FileContent file;

    private Map<String, String> metadata;

    public String getType() {
        return type;
    }

    public FileContent getFile() {
        return file;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void setFile(FileContent file) {
        this.file = file;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
