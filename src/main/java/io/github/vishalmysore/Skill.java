package io.github.vishalmysore;

import lombok.Data;

@Data
class Skill {
    private String id;
    private String name;
    private String description;
    private String[] tags;
    private String[] examples;
    private String[] inputModes;
    private String[] outputModes;


}

