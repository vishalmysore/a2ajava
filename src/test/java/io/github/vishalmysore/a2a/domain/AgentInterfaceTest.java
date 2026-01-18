package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AgentInterfaceTest {

    @Test
    public void testNoArgsConstructor() {
        AgentInterface agentInterface = new AgentInterface();
        assertNotNull(agentInterface);
        assertNull(agentInterface.getUrl());
        assertNull(agentInterface.getProtocolBinding());
        assertNull(agentInterface.getTenant());
    }

    @Test
    public void testAllArgsConstructor() {
        AgentInterface agentInterface = new AgentInterface("https://example.com", "JSONRPC", "tenant1");
        assertEquals("https://example.com", agentInterface.getUrl());
        assertEquals("JSONRPC", agentInterface.getProtocolBinding());
        assertEquals("tenant1", agentInterface.getTenant());
    }

    @Test
    public void testSettersAndGetters() {
        AgentInterface agentInterface = new AgentInterface();
        agentInterface.setUrl("https://test.com");
        agentInterface.setProtocolBinding("GRPC");
        agentInterface.setTenant("testTenant");

        assertEquals("https://test.com", agentInterface.getUrl());
        assertEquals("GRPC", agentInterface.getProtocolBinding());
        assertEquals("testTenant", agentInterface.getTenant());
    }

    @Test
    public void testToString() {
        AgentInterface agentInterface = new AgentInterface("url", "binding", "tenant");
        String toString = agentInterface.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("url"));
        assertTrue(toString.contains("binding"));
        assertTrue(toString.contains("tenant"));
    }

    @Test
    public void testEqualsAndHashCode() {
        AgentInterface ai1 = new AgentInterface("url", "binding", "tenant");
        AgentInterface ai2 = new AgentInterface("url", "binding", "tenant");
        AgentInterface ai3 = new AgentInterface("different", "binding", "tenant");

        assertEquals(ai1, ai2);
        assertNotEquals(ai1, ai3);
        assertEquals(ai1.hashCode(), ai2.hashCode());
        assertNotEquals(ai1.hashCode(), ai3.hashCode());
    }
}