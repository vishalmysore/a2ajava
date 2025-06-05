package io.github.vishalmysore.common;

import com.t4a.annotations.Prompt;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString(of = {"agentUniqueIDTobeUsedToIdentifyTheAgent", "allTheCapabilitiesOfTheAgent"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AgentIdentity {
    @EqualsAndHashCode.Include
    @Prompt(describe = "Unique ID to identify the agent to be used for the query")
    private String agentUniqueIDTobeUsedToIdentifyTheAgent;
    @Prompt(ignore = true)
    private AgentInfo allTheCapabilitiesOfTheAgent;
    @Prompt(ignore = true)
    private String url;
    @Builder
    public AgentIdentity(AgentInfo info, String url) {
        this.agentUniqueIDTobeUsedToIdentifyTheAgent = UUID.randomUUID().toString();
        this.allTheCapabilitiesOfTheAgent = info;
        this.url = url;
    }

    // Keep existing all-args constructor
    public AgentIdentity(String uniqueId, AgentInfo info, String url) {
        this.agentUniqueIDTobeUsedToIdentifyTheAgent = uniqueId;
        this.allTheCapabilitiesOfTheAgent = info;
        this.url = url;
    }
}
