package io.github.vishalmysore.a2a.domain;

import com.t4a.annotations.ListType;
import lombok.Data;

import java.util.List;

@Data
public class AgentCard {
    private String name;
    private String description;
    private String url;
    private Provider provider;
    private String version;
    private String documentationUrl;
    private Capabilities capabilities;
    private Authentication authentication;
    private String[] defaultInputModes;
    private String[] defaultOutputModes;
    //private Skill[] skills;
    @ListType(Skill.class)
    private List<Skill> skills;

    // Getters and setters

}
