package io.github.vishalmysore.acp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Data
@Getter
@Setter
@ToString
public class AgentRun {
    
    @JsonProperty("agent_id")
    private UUID agentId;
    
    @JsonProperty("created_at")
    private Instant createdAt;
    
    @JsonProperty("run_id")
    private UUID runId;
    
    private RunStatus status;
    
    @JsonProperty("updated_at")
    private Instant updatedAt;
    
    @JsonProperty("thread_id")
    private UUID threadId;
    
    public enum RunStatus {
        PENDING,
        ERROR,
        SUCCESS,
        TIMEOUT,
        INTERRUPTED
    }
}
