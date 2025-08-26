package io.github.vishalmysore.acp.server;

import io.github.vishalmysore.acp.domain.*;
import io.github.vishalmysore.acp.util.ACPMapper;
import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.a2a.domain.Task;
import io.github.vishalmysore.a2a.domain.TaskStatus;
import io.github.vishalmysore.a2a.domain.TaskState;
import io.github.vishalmysore.a2a.server.RealTimeAgentCardController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/acp")
@Slf4j
public class ACPRestController implements ACPController {
    
    private final ApplicationContext applicationContext;
    private final RealTimeAgentCardController agentCardController;
    private final ACPMapper acpMapper;
    private final Map<UUID, io.github.vishalmysore.acp.domain.Thread> threads = new HashMap<>();
    private final Map<UUID, AgentRun> runs = new HashMap<>();
    
    public ACPRestController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.agentCardController = new RealTimeAgentCardController(applicationContext);
        this.acpMapper = new ACPMapper();
    }
    
    @Override
    public ResponseEntity<List<Agent>> searchAgents(@RequestBody AgentSearchRequest request) {
        log.info("Searching agents with request: {}", request);
        
        try {
            AgentCard agentCard = agentCardController.getAgentCard().getBody();
            List<AgentCard> agentCards = agentCard != null ? List.of(agentCard) : List.of();
            List<Agent> agents = agentCards.stream()
                .map(acpMapper::toAgent)
                .toList();
            
            if (request.getQuery() != null && !request.getQuery().isEmpty()) {
                agents = agents.stream()
                    .filter(agent -> agent.getMetadata().getDescription()
                        .toLowerCase().contains(request.getQuery().toLowerCase()))
                    .toList();
            }
            
            if (request.getLimit() != null) {
                int offset = request.getOffset() != null ? request.getOffset() : 0;
                int limit = Math.min(request.getLimit(), agents.size() - offset);
                agents = agents.stream()
                    .skip(offset)
                    .limit(limit)
                    .toList();
            }
            
            return ResponseEntity.ok(agents);
        } catch (Exception e) {
            log.error("Error searching agents", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @Override
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<Agent> getAgent(@PathVariable UUID agentId) {
        log.info("Getting agent with ID: {}", agentId);
        
        try {
            AgentCard singleAgentCard = agentCardController.getAgentCard().getBody();
            List<AgentCard> agentCards = singleAgentCard != null ? List.of(singleAgentCard) : List.of();
            Optional<AgentCard> agentCard = agentCards.stream()
                .filter(card -> agentId.equals(acpMapper.generateAgentId(card)))
                .findFirst();
            
            if (agentCard.isPresent()) {
                Agent agent = acpMapper.toAgent(agentCard.get());
                return ResponseEntity.ok(agent);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error getting agent", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @Override
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<AgentACPDescriptor> getAgentDescriptor(@PathVariable UUID agentId) {
        log.info("Getting agent descriptor for ID: {}", agentId);
        
        try {
            AgentCard singleAgentCard = agentCardController.getAgentCard().getBody();
            List<AgentCard> agentCards = singleAgentCard != null ? List.of(singleAgentCard) : List.of();
            Optional<AgentCard> agentCard = agentCards.stream()
                .filter(card -> agentId.equals(acpMapper.generateAgentId(card)))
                .findFirst();
            
            if (agentCard.isPresent()) {
                AgentACPDescriptor descriptor = acpMapper.toAgentDescriptor(agentCard.get());
                return ResponseEntity.ok(descriptor);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error getting agent descriptor", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @Override
    public ResponseEntity<AgentRun> createStatelessRun(@RequestBody RunCreateStateless request) {
        log.info("Creating stateless run: {}", request);
        
        try {
            AgentRun run = new AgentRun();
            run.setRunId(UUID.randomUUID());
            run.setAgentId(request.getAgentId());
            run.setStatus(AgentRun.RunStatus.PENDING);
            run.setCreatedAt(Instant.now());
            run.setUpdatedAt(Instant.now());
            
            runs.put(run.getRunId(), run);
            
            Task task = acpMapper.toTask(request);
            task.setId(run.getRunId().toString());
            task.setStatus(new TaskStatus(TaskState.SUBMITTED));
            
            run.setStatus(AgentRun.RunStatus.SUCCESS);
            run.setUpdatedAt(Instant.now());
            
            return ResponseEntity.ok(run);
        } catch (Exception e) {
            log.error("Error creating stateless run", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @Override
    public ResponseEntity<io.github.vishalmysore.acp.domain.Thread> createThread(@RequestBody Map<String, Object> metadata) {
        log.info("Creating thread with metadata: {}", metadata);
        
        try {
            io.github.vishalmysore.acp.domain.Thread thread = new io.github.vishalmysore.acp.domain.Thread();
            thread.setThreadId(UUID.randomUUID());
            thread.setStatus(io.github.vishalmysore.acp.domain.Thread.ThreadStatus.IDLE);
            thread.setCreatedAt(Instant.now());
            thread.setUpdatedAt(Instant.now());
            thread.setMetadata(metadata);
            thread.setMessages(new ArrayList<>());
            thread.setValues(new HashMap<>());
            
            threads.put(thread.getThreadId(), thread);
            
            return ResponseEntity.ok(thread);
        } catch (Exception e) {
            log.error("Error creating thread", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @Override
    @GetMapping("/agent/{threadId}")
    public ResponseEntity<AgentRun> createStatefulRun(@PathVariable UUID threadId, @RequestBody RunCreateStateful request) {
        log.info("Creating stateful run for thread {}: {}", threadId, request);
        
        try {
            io.github.vishalmysore.acp.domain.Thread thread = threads.get(threadId);
            if (thread == null) {
                return ResponseEntity.notFound().build();
            }
            
            AgentRun run = new AgentRun();
            run.setRunId(UUID.randomUUID());
            run.setAgentId(request.getAgentId());
            run.setThreadId(threadId);
            run.setStatus(AgentRun.RunStatus.PENDING);
            run.setCreatedAt(Instant.now());
            run.setUpdatedAt(Instant.now());
            
            runs.put(run.getRunId(), run);
            
            thread.setStatus(io.github.vishalmysore.acp.domain.Thread.ThreadStatus.BUSY);
            thread.setUpdatedAt(Instant.now());
            
            Task task = acpMapper.toTask(request);
            task.setId(run.getRunId().toString());
            task.setStatus(new TaskStatus(TaskState.SUBMITTED));
            
            run.setStatus(AgentRun.RunStatus.SUCCESS);
            run.setUpdatedAt(Instant.now());
            
            thread.setStatus(io.github.vishalmysore.acp.domain.Thread.ThreadStatus.IDLE);
            thread.setUpdatedAt(Instant.now());
            
            return ResponseEntity.ok(run);
        } catch (Exception e) {
            log.error("Error creating stateful run", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
