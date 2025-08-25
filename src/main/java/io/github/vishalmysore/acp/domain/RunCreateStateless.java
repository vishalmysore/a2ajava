package io.github.vishalmysore.acp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

@Data
@Getter
@Setter
@ToString
public class RunCreateStateless {
    
    @JsonProperty("agent_id")
    private UUID agentId;
    
    private Map<String, Object> input;
    
    private Map<String, Object> metadata;
}
