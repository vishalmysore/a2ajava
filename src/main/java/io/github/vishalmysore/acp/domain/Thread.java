package io.github.vishalmysore.acp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Getter
@Setter
@ToString
public class Thread {
    
    @JsonProperty("created_at")
    private Instant createdAt;
    
    private Map<String, Object> metadata;
    
    private ThreadStatus status;
    
    @JsonProperty("thread_id")
    private UUID threadId;
    
    @JsonProperty("updated_at")
    private Instant updatedAt;
    
    private List<Object> messages;
    
    private Map<String, Object> values;
    
    public enum ThreadStatus {
        IDLE,
        BUSY,
        INTERRUPTED,
        ERROR
    }
}
