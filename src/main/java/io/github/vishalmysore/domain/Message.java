package io.github.vishalmysore.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Message {
    private String role;
    private List<Part> parts;
    private Map<String, Object> metadata;


}
