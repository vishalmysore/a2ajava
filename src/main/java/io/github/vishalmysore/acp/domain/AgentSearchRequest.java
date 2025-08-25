package io.github.vishalmysore.acp.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
@Getter
@Setter
@ToString
public class AgentSearchRequest {
    
    private String query;
    
    private List<String> tags;
    
    private Integer limit;
    
    private Integer offset;
}
