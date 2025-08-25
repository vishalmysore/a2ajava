package io.github.vishalmysore.acp.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Data
@Getter
@Setter
@ToString
public class AgentACPDescriptor {
    
    private AgentMetadata metadata;
    
    private Map<String, Object> specs;
}
