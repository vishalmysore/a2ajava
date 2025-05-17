package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data

@ToString
public class Artifact {

    @JsonIgnore
    private String id;
    private String name;
    private String description;

    private List<Part> parts;

    private Map<String, String> metadata = new HashMap<>();
    private int index;
    private boolean append;
    private boolean lastChunk;


}
