package io.github.vishalmysore.domain;

import org.apache.tomcat.jni.FileInfo;

import java.util.HashMap;
import java.util.Map;

public class FilePart implements Part {
    private String type = "file";
    private FileInfo file;
    private Map<String, Object> metadata = new HashMap<>();

    public String getType() {
        return type;
    }

    public FileInfo getFile() {
        return file;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void setFile(FileInfo file) {
        this.file = file;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
