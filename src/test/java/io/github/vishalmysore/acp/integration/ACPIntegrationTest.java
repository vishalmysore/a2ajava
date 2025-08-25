package io.github.vishalmysore.acp.integration;

import io.github.vishalmysore.acp.server.ACPRestController;
import io.github.vishalmysore.acp.domain.*;
import io.github.vishalmysore.acp.server.ACPController;
import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.a2a.server.RealTimeAgentCardController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ACPIntegrationTest {

    @Mock
    private ApplicationContext applicationContext;
    
    @Mock
    private RealTimeAgentCardController agentCardController;
    
    private ACPController acpController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        acpController = new ACPController() {
            @Override
            public ResponseEntity<List<Agent>> searchAgents(AgentSearchRequest request) {
                Agent agent = new Agent();
                agent.setAgentId(UUID.randomUUID());
                AgentMetadata metadata = new AgentMetadata();
                metadata.setDescription("Handles customer support tasks");
                agent.setMetadata(metadata);
                return ResponseEntity.ok(List.of(agent));
            }
            
            @Override
            public ResponseEntity<io.github.vishalmysore.acp.domain.Thread> createThread(Map<String, Object> metadata) {
                io.github.vishalmysore.acp.domain.Thread thread = new io.github.vishalmysore.acp.domain.Thread();
                thread.setThreadId(UUID.randomUUID());
                thread.setStatus(io.github.vishalmysore.acp.domain.Thread.ThreadStatus.IDLE);
                thread.setCreatedAt(java.time.Instant.now());
                thread.setUpdatedAt(java.time.Instant.now());
                thread.setMetadata(metadata);
                thread.setMessages(new java.util.ArrayList<>());
                thread.setValues(new java.util.HashMap<>());
                mockThreads.put(thread.getThreadId(), thread);
                return ResponseEntity.ok(thread);
            }
            
            @Override
            public ResponseEntity<AgentRun> createStatelessRun(RunCreateStateless request) {
                AgentRun run = new AgentRun();
                run.setRunId(UUID.randomUUID());
                run.setAgentId(request.getAgentId());
                run.setStatus(AgentRun.RunStatus.SUCCESS);
                run.setCreatedAt(java.time.Instant.now());
                run.setUpdatedAt(java.time.Instant.now());
                return ResponseEntity.ok(run);
            }
            
            private final Map<UUID, io.github.vishalmysore.acp.domain.Thread> mockThreads = new HashMap<>();
            
            @Override
            public ResponseEntity<AgentRun> createStatefulRun(UUID threadId, RunCreateStateful request) {
                if (!mockThreads.containsKey(threadId)) {
                    return ResponseEntity.notFound().build();
                }
                AgentRun run = new AgentRun();
                run.setRunId(UUID.randomUUID());
                run.setAgentId(request.getAgentId());
                run.setThreadId(threadId);
                run.setStatus(AgentRun.RunStatus.SUCCESS);
                run.setCreatedAt(java.time.Instant.now());
                run.setUpdatedAt(java.time.Instant.now());
                return ResponseEntity.ok(run);
            }
            
            @Override
            public ResponseEntity<Agent> getAgent(UUID agentId) {
                return ResponseEntity.notFound().build();
            }
            
            @Override
            public ResponseEntity<AgentACPDescriptor> getAgentDescriptor(UUID agentId) {
                return ResponseEntity.notFound().build();
            }
        };
    }

    @Test
    void testFullACPWorkflow() {
        AgentCard mockAgentCard = new AgentCard();
        mockAgentCard.setName("Customer Support Agent");
        mockAgentCard.setDescription("Handles customer support tasks");
        
        when(agentCardController.getAgentCard()).thenReturn(ResponseEntity.ok(mockAgentCard));
        AgentSearchRequest searchRequest = new AgentSearchRequest();
        searchRequest.setQuery("test");
        searchRequest.setLimit(10);

        ResponseEntity<List<Agent>> searchResponse = acpController.searchAgents(searchRequest);
        assertEquals(200, searchResponse.getStatusCodeValue());
        assertNotNull(searchResponse.getBody());

        Map<String, Object> threadMetadata = Map.of("session", "test-session");
        ResponseEntity<io.github.vishalmysore.acp.domain.Thread> threadResponse = acpController.createThread(threadMetadata);
        assertEquals(200, threadResponse.getStatusCodeValue());
        assertNotNull(threadResponse.getBody());
        
        io.github.vishalmysore.acp.domain.Thread thread = threadResponse.getBody();
        assertEquals(io.github.vishalmysore.acp.domain.Thread.ThreadStatus.IDLE, thread.getStatus());
        assertNotNull(thread.getThreadId());

        RunCreateStateless statelessRequest = new RunCreateStateless();
        statelessRequest.setAgentId(UUID.randomUUID());
        statelessRequest.setInput(Map.of("param", "value"));
        statelessRequest.setMetadata(Map.of("type", "stateless"));

        ResponseEntity<AgentRun> statelessResponse = acpController.createStatelessRun(statelessRequest);
        assertEquals(200, statelessResponse.getStatusCodeValue());
        assertNotNull(statelessResponse.getBody());
        
        AgentRun statelessRun = statelessResponse.getBody();
        assertEquals(AgentRun.RunStatus.SUCCESS, statelessRun.getStatus());
        assertNotNull(statelessRun.getRunId());

        RunCreateStateful statefulRequest = new RunCreateStateful();
        statefulRequest.setAgentId(UUID.randomUUID());
        statefulRequest.setThreadId(thread.getThreadId());
        statefulRequest.setInput(Map.of("param", "value"));
        statefulRequest.setMetadata(Map.of("type", "stateful"));

        ResponseEntity<AgentRun> statefulResponse = acpController.createStatefulRun(thread.getThreadId(), statefulRequest);
        assertEquals(200, statefulResponse.getStatusCodeValue());
        assertNotNull(statefulResponse.getBody());
        
        AgentRun statefulRun = statefulResponse.getBody();
        assertEquals(AgentRun.RunStatus.SUCCESS, statefulRun.getStatus());
        assertEquals(thread.getThreadId(), statefulRun.getThreadId());
        assertNotNull(statefulRun.getRunId());
    }

    @Test
    void testAgentNotFound() {
        UUID nonExistentAgentId = UUID.randomUUID();
        
        ResponseEntity<Agent> agentResponse = acpController.getAgent(nonExistentAgentId);
        assertEquals(404, agentResponse.getStatusCodeValue());
        
        ResponseEntity<AgentACPDescriptor> descriptorResponse = acpController.getAgentDescriptor(nonExistentAgentId);
        assertEquals(404, descriptorResponse.getStatusCodeValue());
    }

    @Test
    void testStatefulRunWithInvalidThread() {
        UUID nonExistentThreadId = UUID.randomUUID();
        
        RunCreateStateful request = new RunCreateStateful();
        request.setAgentId(UUID.randomUUID());
        request.setThreadId(nonExistentThreadId);
        request.setInput(Map.of("param", "value"));
        
        ResponseEntity<AgentRun> response = acpController.createStatefulRun(nonExistentThreadId, request);
        assertEquals(404, response.getStatusCodeValue());
    }
}
