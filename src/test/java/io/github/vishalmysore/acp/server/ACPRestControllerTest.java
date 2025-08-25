package io.github.vishalmysore.acp.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vishalmysore.acp.domain.*;
import io.github.vishalmysore.acp.server.ACPController;
import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.a2a.server.RealTimeAgentCardController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ACPRestControllerTest {

    @Mock
    private ApplicationContext applicationContext;
    
    @Mock
    private RealTimeAgentCardController agentCardController;
    
    private ACPController acpController;
    private ObjectMapper objectMapper;

    private UUID testAgentId;
    private UUID testThreadId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        
        acpController = new ACPController() {
            @Override
            public ResponseEntity<List<Agent>> searchAgents(AgentSearchRequest request) {
                AgentCard mockAgentCard = new AgentCard();
                mockAgentCard.setName("Test Agent");
                mockAgentCard.setDescription("Test Description");
                
                Agent agent = new Agent();
                agent.setAgentId(UUID.randomUUID());
                AgentMetadata metadata = new AgentMetadata();
                metadata.setDescription("Test Description");
                agent.setMetadata(metadata);
                
                return ResponseEntity.ok(List.of(agent));
            }
            
            @Override
            public ResponseEntity<Agent> getAgent(UUID agentId) {
                return ResponseEntity.notFound().build();
            }
            
            @Override
            public ResponseEntity<AgentACPDescriptor> getAgentDescriptor(UUID agentId) {
                return ResponseEntity.notFound().build();
            }
            
            @Override
            public ResponseEntity<AgentRun> createStatelessRun(RunCreateStateless request) {
                AgentRun run = new AgentRun();
                run.setRunId(UUID.randomUUID());
                run.setAgentId(request.getAgentId());
                run.setStatus(AgentRun.RunStatus.SUCCESS);
                return ResponseEntity.ok(run);
            }
            
            @Override
            public ResponseEntity<io.github.vishalmysore.acp.domain.Thread> createThread(Map<String, Object> metadata) {
                io.github.vishalmysore.acp.domain.Thread thread = new io.github.vishalmysore.acp.domain.Thread();
                thread.setThreadId(UUID.randomUUID());
                thread.setStatus(io.github.vishalmysore.acp.domain.Thread.ThreadStatus.IDLE);
                return ResponseEntity.ok(thread);
            }
            
            @Override
            public ResponseEntity<AgentRun> createStatefulRun(UUID threadId, RunCreateStateful request) {
                return ResponseEntity.notFound().build();
            }
        };
        
        testAgentId = UUID.randomUUID();
        testThreadId = UUID.randomUUID();
    }

    @Test
    void testSearchAgents() {
        AgentCard mockAgentCard = new AgentCard();
        mockAgentCard.setName("Test Agent");
        mockAgentCard.setDescription("Test Description");
        
        when(agentCardController.getAgentCard()).thenReturn(ResponseEntity.ok(mockAgentCard));
        
        AgentSearchRequest request = new AgentSearchRequest();
        request.setQuery("test");
        request.setLimit(10);

        ResponseEntity<List<Agent>> response = acpController.searchAgents(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAgentNotFound() {
        when(agentCardController.getAgentCard()).thenReturn(ResponseEntity.ok(null));
        
        ResponseEntity<Agent> response = acpController.getAgent(testAgentId);
        
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetAgentDescriptorNotFound() {
        when(agentCardController.getAgentCard()).thenReturn(ResponseEntity.ok(null));
        
        ResponseEntity<AgentACPDescriptor> response = acpController.getAgentDescriptor(testAgentId);
        
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testCreateStatelessRun() {
        RunCreateStateless request = new RunCreateStateless();
        request.setAgentId(testAgentId);
        request.setInput(Map.of("test", "value"));
        request.setMetadata(Map.of("key", "value"));

        ResponseEntity<AgentRun> response = acpController.createStatelessRun(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(testAgentId, response.getBody().getAgentId());
        assertEquals(AgentRun.RunStatus.SUCCESS, response.getBody().getStatus());
    }

    @Test
    void testCreateThread() {
        Map<String, Object> metadata = Map.of("key", "value");

        ResponseEntity<io.github.vishalmysore.acp.domain.Thread> response = acpController.createThread(metadata);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(io.github.vishalmysore.acp.domain.Thread.ThreadStatus.IDLE, response.getBody().getStatus());
    }

    @Test
    void testCreateStatefulRunWithInvalidThread() {
        RunCreateStateful request = new RunCreateStateful();
        request.setAgentId(testAgentId);
        request.setThreadId(testThreadId);
        request.setInput(Map.of("test", "value"));

        ResponseEntity<AgentRun> response = acpController.createStatefulRun(testThreadId, request);

        assertEquals(404, response.getStatusCodeValue());
    }
}
