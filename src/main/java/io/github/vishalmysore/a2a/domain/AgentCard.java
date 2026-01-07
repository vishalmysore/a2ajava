package io.github.vishalmysore.a2a.domain;

import com.t4a.annotations.ListType;
import io.github.vishalmysore.common.AgentInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@ToString
@EqualsAndHashCode
public class AgentCard implements AgentInfo {
    private String protocolVersion = "1.0"; // A2A protocol version (Major.Minor format)
    private String name;
    private String description;
    private String url; // Deprecated - use supportedInterfaces instead
    private Provider provider;
    private String version; // Agent version
    private String documentationUrl;
    private Capabilities capabilities;
    private Authentication authentication;
    private String[] defaultInputModes;
    private String[] defaultOutputModes;
    //private Skill[] skills;
    @ListType(Skill.class)
    private List<Skill> skills;
    // A2A v1.0: Ordered list of supported interfaces. First entry is preferred.
    @ListType(AgentInterface.class)
    private List<AgentInterface> supportedInterfaces;

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
    
    /**
     * Add A2UI v0.8 extension support to this agent.
     * 
     * @param supportedCatalogIds List of catalog URIs this agent can generate
     * @param acceptsInlineCatalogs Whether agent accepts inline catalog definitions
     */
    public void addA2UISupport(List<String> supportedCatalogIds, boolean acceptsInlineCatalogs) {
        if (capabilities == null) {
            capabilities = new Capabilities();
        }
        if (capabilities.getExtensions() == null) {
            capabilities.setExtensions(new ArrayList<>());
        }
        
        AgentExtension a2uiExtension = new AgentExtension();
        a2uiExtension.setUri("https://a2ui.org/a2a-extension/a2ui/v0.8");
        a2uiExtension.setDescription("Ability to render A2UI");
        a2uiExtension.setRequired(false);
        
        Map<String, Object> params = new HashMap<>();
        params.put("supportedCatalogIds", supportedCatalogIds);
        params.put("acceptsInlineCatalogs", acceptsInlineCatalogs);
        a2uiExtension.setParams(params);
        
        capabilities.getExtensions().add(a2uiExtension);
    }
}
