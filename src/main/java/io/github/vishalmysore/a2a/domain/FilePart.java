package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.ToString;


import java.util.Map;


@ToString
public class FilePart extends Part {

    @JsonIgnore
    private String id;
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
