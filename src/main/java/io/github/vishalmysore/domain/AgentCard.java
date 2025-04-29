package io.github.vishalmysore.domain;

import lombok.Data;

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
    private Skill[] skills;

    // Getters and setters

}
