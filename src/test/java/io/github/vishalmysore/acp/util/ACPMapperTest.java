package io.github.vishalmysore.acp.util;

import io.github.vishalmysore.acp.domain.*;
import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.a2a.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ACPMapperTest {

    private ACPMapper mapper;
    private AgentCard testAgentCard;

    @BeforeEach
    void setUp() {
        mapper = new ACPMapper();
        
        testAgentCard = new AgentCard();
        testAgentCard.setName("Test Agent");
        testAgentCard.setDescription("Test agent description");
    }

    @Test
    void testToAgent() {
        Agent agent = mapper.toAgent(testAgentCard);
        
        assertNotNull(agent);
        assertNotNull(agent.getAgentId());
        assertNotNull(agent.getMetadata());
        assertEquals("Test agent description", agent.getMetadata().getDescription());
        assertEquals("Test Agent", agent.getMetadata().getRef().getName());
        assertEquals("1.0.0", agent.getMetadata().getRef().getVersion());
        assertEquals("http://localhost:8080", agent.getMetadata().getRef().getUrl());
    }

    @Test
    void testToAgentDescriptor() {
        AgentACPDescriptor descriptor = mapper.toAgentDescriptor(testAgentCard);
        
        assertNotNull(descriptor);
        assertNotNull(descriptor.getMetadata());
        assertNotNull(descriptor.getSpecs());
        assertEquals("Test agent description", descriptor.getMetadata().getDescription());
        assertEquals("Test Agent", descriptor.getMetadata().getRef().getName());
    }

    @Test
    void testToTaskFromStateless() {
        RunCreateStateless runCreate = new RunCreateStateless();
        runCreate.setAgentId(UUID.randomUUID());
        runCreate.setMetadata(Map.of("key", "value"));
        
        Task task = mapper.toTask(runCreate);
        
        assertNotNull(task);
        assertNotNull(task.getStatus());
        assertEquals("value", task.getMetadata().get("key"));
    }

    @Test
    void testToTaskFromStateful() {
        RunCreateStateful runCreate = new RunCreateStateful();
        runCreate.setAgentId(UUID.randomUUID());
        runCreate.setThreadId(UUID.randomUUID());
        runCreate.setMetadata(Map.of("key", "value"));
        
        Task task = mapper.toTask(runCreate);
        
        assertNotNull(task);
        assertNotNull(task.getStatus());
        assertEquals("value", task.getMetadata().get("key"));
    }

    @Test
    void testGenerateAgentId() {
        UUID agentId1 = mapper.generateAgentId(testAgentCard);
        UUID agentId2 = mapper.generateAgentId(testAgentCard);
        
        assertNotNull(agentId1);
        assertNotNull(agentId2);
        assertEquals(agentId1, agentId2);
        
        AgentCard differentCard = new AgentCard();
        differentCard.setName("Different Agent");
        differentCard.setDescription("Different description");
        
        UUID differentId = mapper.generateAgentId(differentCard);
        assertNotEquals(agentId1, differentId);
    }

    @Test
    void testConvertToStringMapWithNullValues() {
        RunCreateStateless runCreate = new RunCreateStateless();
        runCreate.setAgentId(UUID.randomUUID());
        runCreate.setMetadata(Map.of("key1", "value1", "key2", 123, "key3", true));
        
        Task task = mapper.toTask(runCreate);
        
        assertEquals("value1", task.getMetadata().get("key1"));
        assertEquals("123", task.getMetadata().get("key2"));
        assertEquals("true", task.getMetadata().get("key3"));
    }
}
