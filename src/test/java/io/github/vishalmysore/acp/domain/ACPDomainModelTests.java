package io.github.vishalmysore.acp.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ACPDomainModelTests {

    @Test
    void testAgentModel() {
        Agent agent = new Agent();
        UUID agentId = UUID.randomUUID();
        agent.setAgentId(agentId);
        
        AgentMetadata metadata = new AgentMetadata();
        metadata.setDescription("Test agent");
        agent.setMetadata(metadata);
        
        assertEquals(agentId, agent.getAgentId());
        assertEquals("Test agent", agent.getMetadata().getDescription());
    }

    @Test
    void testAgentMetadataModel() {
        AgentMetadata metadata = new AgentMetadata();
        metadata.setDescription("Test description");
        
        AgentReference ref = new AgentReference();
        ref.setName("test-agent");
        ref.setVersion("1.0.0");
        ref.setUrl("http://localhost:8080");
        metadata.setRef(ref);
        
        assertEquals("Test description", metadata.getDescription());
        assertEquals("test-agent", metadata.getRef().getName());
        assertEquals("1.0.0", metadata.getRef().getVersion());
        assertEquals("http://localhost:8080", metadata.getRef().getUrl());
    }

    @Test
    void testAgentRunModel() {
        AgentRun run = new AgentRun();
        UUID runId = UUID.randomUUID();
        UUID agentId = UUID.randomUUID();
        UUID threadId = UUID.randomUUID();
        Instant now = Instant.now();
        
        run.setRunId(runId);
        run.setAgentId(agentId);
        run.setThreadId(threadId);
        run.setStatus(AgentRun.RunStatus.pending);
        run.setCreatedAt(now);
        run.setUpdatedAt(now);
        
        assertEquals(runId, run.getRunId());
        assertEquals(agentId, run.getAgentId());
        assertEquals(threadId, run.getThreadId());
        assertEquals(AgentRun.RunStatus.pending, run.getStatus());
        assertEquals(now, run.getCreatedAt());
        assertEquals(now, run.getUpdatedAt());
    }

    @Test
    void testThreadModel() {
        Thread thread = new Thread();
        UUID threadId = UUID.randomUUID();
        Instant now = Instant.now();
        Map<String, Object> metadata = Map.of("key", "value");
        List<Object> messages = new ArrayList<>();
        Map<String, Object> values = new HashMap<>();
        
        thread.setThreadId(threadId);
        thread.setStatus(Thread.ThreadStatus.idle);
        thread.setCreatedAt(now);
        thread.setUpdatedAt(now);
        thread.setMetadata(metadata);
        thread.setMessages(messages);
        thread.setValues(values);
        
        assertEquals(threadId, thread.getThreadId());
        assertEquals(Thread.ThreadStatus.idle, thread.getStatus());
        assertEquals(now, thread.getCreatedAt());
        assertEquals(now, thread.getUpdatedAt());
        assertEquals(metadata, thread.getMetadata());
        assertEquals(messages, thread.getMessages());
        assertEquals(values, thread.getValues());
    }

    @Test
    void testRunCreateStatelessModel() {
        RunCreateStateless request = new RunCreateStateless();
        UUID agentId = UUID.randomUUID();
        Map<String, Object> input = Map.of("param", "value");
        Map<String, Object> metadata = Map.of("key", "value");
        
        request.setAgentId(agentId);
        request.setInput(input);
        request.setMetadata(metadata);
        
        assertEquals(agentId, request.getAgentId());
        assertEquals(input, request.getInput());
        assertEquals(metadata, request.getMetadata());
    }

    @Test
    void testRunCreateStatefulModel() {
        RunCreateStateful request = new RunCreateStateful();
        UUID agentId = UUID.randomUUID();
        UUID threadId = UUID.randomUUID();
        Map<String, Object> input = Map.of("param", "value");
        Map<String, Object> metadata = Map.of("key", "value");
        
        request.setAgentId(agentId);
        request.setThreadId(threadId);
        request.setInput(input);
        request.setMetadata(metadata);
        
        assertEquals(agentId, request.getAgentId());
        assertEquals(threadId, request.getThreadId());
        assertEquals(input, request.getInput());
        assertEquals(metadata, request.getMetadata());
    }

    @Test
    void testAgentSearchRequestModel() {
        AgentSearchRequest request = new AgentSearchRequest();
        request.setQuery("test query");
        request.setTags(Arrays.asList("tag1", "tag2"));
        request.setLimit(10);
        request.setOffset(0);
        
        assertEquals("test query", request.getQuery());
        assertEquals(Arrays.asList("tag1", "tag2"), request.getTags());
        assertEquals(10, request.getLimit());
        assertEquals(0, request.getOffset());
    }

    @Test
    void testAgentACPDescriptorModel() {
        AgentACPDescriptor descriptor = new AgentACPDescriptor();
        
        AgentMetadata metadata = new AgentMetadata();
        metadata.setDescription("Test descriptor");
        descriptor.setMetadata(metadata);
        
        Map<String, Object> specs = Map.of("spec1", "value1");
        descriptor.setSpecs(specs);
        
        assertEquals("Test descriptor", descriptor.getMetadata().getDescription());
        assertEquals(specs, descriptor.getSpecs());
    }
}
