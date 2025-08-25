package io.github.vishalmysore.acp.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class AgentReference {
    
    private String name;
    
    private String version;
    
    private String url;
}
