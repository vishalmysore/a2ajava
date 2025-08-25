package io.github.vishalmysore.acp.server;

import io.github.vishalmysore.acp.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ACPController {
    
    @PostMapping("/agents/search")
    ResponseEntity<List<Agent>> searchAgents(@RequestBody AgentSearchRequest request);
    
    @GetMapping("/agents/{agentId}")
    ResponseEntity<Agent> getAgent(@PathVariable("agentId") UUID agentId);
    
    @GetMapping("/agents/{agentId}/descriptor")
    ResponseEntity<AgentACPDescriptor> getAgentDescriptor(@PathVariable("agentId") UUID agentId);
    
    @PostMapping("/runs")
    ResponseEntity<AgentRun> createStatelessRun(@RequestBody RunCreateStateless request);
    
    @PostMapping("/threads")
    ResponseEntity<io.github.vishalmysore.acp.domain.Thread> createThread(@RequestBody Map<String, Object> metadata);
    
    @PostMapping("/threads/{threadId}/runs")
    ResponseEntity<AgentRun> createStatefulRun(@PathVariable("threadId") UUID threadId, @RequestBody RunCreateStateful request);
}
