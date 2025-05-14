package io.github.vishalmysore.a2a.domain;

import com.t4a.annotations.ListType;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@ToString
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
    public void addSkill(String name, String description) {
        if (skills == null) {
            skills = new ArrayList<>();
        }

        Skill skill = new Skill();
        skill.setId(UUID.randomUUID().toString());
        skill.setName(name);
        skill.setDescription(description);

        skills.add(skill);
    }

    public void addSkill(String name, String description, String... tags) {
        if (skills == null) {
            skills = new ArrayList<>();
        }

        Skill skill = new Skill();
        skill.setId(UUID.randomUUID().toString());
        skill.setName(name);
        skill.setDescription(description);
        skill.setTags(tags);

        skills.add(skill);
    }
}
