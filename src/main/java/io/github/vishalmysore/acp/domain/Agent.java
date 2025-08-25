package io.github.vishalmysore.acp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Data
@Getter
@Setter
@ToString
public class Agent {
    
    @JsonProperty("agent_id")
    private UUID agentId;
    
    private AgentMetadata metadata;
}
