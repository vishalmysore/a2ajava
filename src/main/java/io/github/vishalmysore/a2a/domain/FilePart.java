package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import java.util.Map;

/**
 * A2A v1.0: FilePart serializes as {"file": {...}}
 * No "kind" or "type" field in JSON.
 */
@ToString
public class FilePart extends Part {

    @JsonIgnore
    private String id;
    @JsonIgnore
    private String type = "file";

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
