package io.github.vishalmysore.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Artifact {
    private String name;
    private String description;
    private List<Part> parts;
    private Map<String, Object> metadata;
    private int index;
    private boolean append;
    private boolean lastChunk;


}
