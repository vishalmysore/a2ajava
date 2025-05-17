package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data

public class Message {

    @JsonIgnore
    private String id;

    private String role;


    private List<Part> parts;


    private Map<String, String> metadata;
}
