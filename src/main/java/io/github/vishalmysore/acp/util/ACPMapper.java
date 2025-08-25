package io.github.vishalmysore.acp.util;

import io.github.vishalmysore.acp.domain.*;
import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.a2a.domain.Task;
import io.github.vishalmysore.a2a.domain.TaskStatus;
import io.github.vishalmysore.a2a.domain.TaskState;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ACPMapper {
    
    public Agent toAgent(AgentCard agentCard) {
        Agent agent = new Agent();
        agent.setAgentId(generateAgentId(agentCard));
        
        AgentMetadata metadata = new AgentMetadata();
        metadata.setDescription(agentCard.getDescription());
        
        AgentReference ref = new AgentReference();
        ref.setName(agentCard.getName());
        ref.setVersion("1.0.0");
        ref.setUrl("http://localhost:8080");
        metadata.setRef(ref);
        
        agent.setMetadata(metadata);
        return agent;
    }
    
    public AgentACPDescriptor toAgentDescriptor(AgentCard agentCard) {
        AgentACPDescriptor descriptor = new AgentACPDescriptor();
        
        AgentMetadata metadata = new AgentMetadata();
        metadata.setDescription(agentCard.getDescription());
        
        AgentReference ref = new AgentReference();
        ref.setName(agentCard.getName());
        ref.setVersion("1.0.0");
        ref.setUrl("http://localhost:8080");
        metadata.setRef(ref);
        
        descriptor.setMetadata(metadata);
        
        Map<String, Object> specs = new HashMap<>();
        specs.put("actions", agentCard.getSkills() != null ? 
            agentCard.getSkills().stream().map(skill -> skill.getName()).toList() : List.of());
        specs.put("groups", List.of("default"));
        descriptor.setSpecs(specs);
        
        return descriptor;
    }
    
    public Task toTask(RunCreateStateless runCreate) {
        Task task = new Task();
        task.setMetadata(convertToStringMap(runCreate.getMetadata()));
        task.setStatus(new TaskStatus(TaskState.SUBMITTED));
        return task;
    }
    
    public Task toTask(RunCreateStateful runCreate) {
        Task task = new Task();
        task.setMetadata(convertToStringMap(runCreate.getMetadata()));
        task.setStatus(new TaskStatus(TaskState.SUBMITTED));
        return task;
    }
    
    public UUID generateAgentId(AgentCard agentCard) {
        try {
            String input = agentCard.getName() + agentCard.getDescription();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            
            long mostSigBits = 0;
            long leastSigBits = 0;
            for (int i = 0; i < 8; i++) {
                mostSigBits = (mostSigBits << 8) | (hash[i] & 0xff);
            }
            for (int i = 8; i < 16; i++) {
                leastSigBits = (leastSigBits << 8) | (hash[i] & 0xff);
            }
            
            return new UUID(mostSigBits, leastSigBits);
        } catch (NoSuchAlgorithmException e) {
            return UUID.randomUUID();
        }
    }
    
    private Map<String, String> convertToStringMap(Map<String, Object> objectMap) {
        if (objectMap == null) {
            return new HashMap<>();
        }
        
        Map<String, String> stringMap = new HashMap<>();
        objectMap.forEach((key, value) -> 
            stringMap.put(key, value != null ? value.toString() : null));
        return stringMap;
    }
}
