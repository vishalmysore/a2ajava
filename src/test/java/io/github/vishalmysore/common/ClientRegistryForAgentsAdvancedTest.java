package io.github.vishalmysore.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Advanced unit tests for ClientRegistryForAgents covering concurrency,
 * agent replacement, large-scale stress, and boundary conditions.
 */
public class ClientRegistryForAgentsAdvancedTest {

    private ClientRegistryForAgents registry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        registry = new ClientRegistryForAgents();
    }

    // ── Agent replacement (same key) ─────────────────────────────────

    @Test
    void addAgent_sameInfoKey_shouldReplaceOldAgent() {
        AgentInfo sharedInfo = new MockAgentInfo("SharedKey");

        Agent agent1 = mock(Agent.class);
        when(agent1.getInfo()).thenReturn(sharedInfo);

        Agent agent2 = mock(Agent.class);
        when(agent2.getInfo()).thenReturn(sharedInfo);

        registry.addAgent(agent1);
        assertEquals(1, registry.getAgentCount());
        assertSame(agent1, registry.retrieveAgent(sharedInfo));

        registry.addAgent(agent2);
        assertEquals(1, registry.getAgentCount());  // count should stay 1
        assertSame(agent2, registry.retrieveAgent(sharedInfo));  // replaced
    }

    // ── Large-scale stress ───────────────────────────────────────────

    @Test
    void addAndRetrieve_largeNumberOfAgents() {
        int count = 500;
        List<AgentInfo> infos = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            MockAgentInfo info = new MockAgentInfo("Agent-" + i);
            Agent agent = mock(Agent.class);
            when(agent.getInfo()).thenReturn(info);
            infos.add(info);
            assertTrue(registry.addAgent(agent));
        }

        assertEquals(count, registry.getAgentCount());

        for (AgentInfo info : infos) {
            assertTrue(registry.hasAgent(info));
            assertNotNull(registry.retrieveAgent(info));
        }
    }

    // ── getAgentsInfo formatting ─────────────────────────────────────

    @Test
    void getAgentsInfo_singleAgent_shouldContainAgentName() {
        Agent agent = mock(Agent.class);
        AgentInfo info = new MockAgentInfo("Solo");
        when(agent.getInfo()).thenReturn(info);
        registry.addAgent(agent);

        String result = registry.getAgentsInfo();
        assertTrue(result.contains("Solo"));
        // Single agent: the delimiter ", " used by Collectors.joining should not appear
        // between separate agent entries (though it may appear inside toString)
        assertEquals(info.toString(), result);
    }

    @Test
    void getAgentsInfo_manyAgents_shouldContainAllNames() {
        for (int i = 0; i < 5; i++) {
            Agent agent = mock(Agent.class);
            AgentInfo info = new MockAgentInfo("Agent-" + i);
            when(agent.getInfo()).thenReturn(info);
            registry.addAgent(agent);
        }

        String result = registry.getAgentsInfo();
        // All agent names should appear in the output
        for (int i = 0; i < 5; i++) {
            assertTrue(result.contains("Agent-" + i),
                    "getAgentsInfo should contain Agent-" + i);
        }
        // The joining delimiter ", " should separate entries
        // With 5 agents, the result length must be greater than a single agent's toString
        assertTrue(result.length() > new MockAgentInfo("Agent-0").toString().length());
    }

    // ── Null/edge-case retrieval ─────────────────────────────────────

    @Test
    void retrieveAgent_afterRemovalByReplacement_shouldReturnNewAgent() {
        MockAgentInfo info = new MockAgentInfo("Key");
        Agent old = mock(Agent.class);
        when(old.getInfo()).thenReturn(info);
        Agent replacement = mock(Agent.class);
        when(replacement.getInfo()).thenReturn(info);

        registry.addAgent(old);
        registry.addAgent(replacement);

        assertSame(replacement, registry.retrieveAgent(info));
        assertNotSame(old, registry.retrieveAgent(info));
    }

    @Test
    void hasAgent_withDifferentInfoInstances_sameContent() {
        // MockAgentInfo does not override equals, so two instances are different keys
        MockAgentInfo info1 = new MockAgentInfo("Agent");
        MockAgentInfo info2 = new MockAgentInfo("Agent");

        Agent agent = mock(Agent.class);
        when(agent.getInfo()).thenReturn(info1);
        registry.addAgent(agent);

        assertTrue(registry.hasAgent(info1));
        // info2 is a different object, won't match unless equals/hashCode overridden
        assertFalse(registry.hasAgent(info2));
    }

    // ── Concurrent add ───────────────────────────────────────────────

    @Test
    void concurrentAdds_shouldNotThrow() throws Exception {
        int threads = 30;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            final int idx = i;
            pool.submit(() -> {
                try {
                    MockAgentInfo info = new MockAgentInfo("Agent-" + idx);
                    Agent agent = mock(Agent.class);
                    when(agent.getInfo()).thenReturn(info);
                    registry.addAgent(agent);
                } finally {
                    latch.countDown();
                }
            });
        }

        assertTrue(latch.await(10, TimeUnit.SECONDS));
        pool.shutdown();

        // All agents should be present (unique keys)
        assertTrue(registry.getAgentCount() > 0);
        assertTrue(registry.getAgentCount() <= threads);
    }

    @Test
    void concurrentAddAndRetrieve_shouldNotThrow() throws Exception {
        int threads = 20;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads * 2);

        // First add some agents
        List<MockAgentInfo> infos = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            MockAgentInfo info = new MockAgentInfo("Agent-" + i);
            infos.add(info);
            Agent agent = mock(Agent.class);
            when(agent.getInfo()).thenReturn(info);
            registry.addAgent(agent);
        }

        // Concurrently add new agents and retrieve existing ones
        for (int i = 0; i < threads; i++) {
            final int idx = i;
            pool.submit(() -> {
                try {
                    MockAgentInfo info = new MockAgentInfo("New-Agent-" + idx);
                    Agent agent = mock(Agent.class);
                    when(agent.getInfo()).thenReturn(info);
                    registry.addAgent(agent);
                } finally {
                    latch.countDown();
                }
            });
            pool.submit(() -> {
                try {
                    registry.retrieveAgent(infos.get(idx));
                    registry.hasAgent(infos.get(idx));
                    registry.getAgentCount();
                } finally {
                    latch.countDown();
                }
            });
        }

        assertTrue(latch.await(10, TimeUnit.SECONDS));
        pool.shutdown();
    }

    // ── Boundary conditions ──────────────────────────────────────────

    @Test
    void addAgent_agentReturningNullInfo_shouldReturnFalse() {
        Agent agent = mock(Agent.class);
        when(agent.getInfo()).thenReturn(null);

        assertFalse(registry.addAgent(agent));
        assertEquals(0, registry.getAgentCount());
    }

    @Test
    void emptyRegistry_allOperationsShouldBeConsistent() {
        assertEquals(0, registry.getAgentCount());
        assertEquals("", registry.getAgentsInfo());
        assertNull(registry.retrieveAgent(new MockAgentInfo("x")));
        assertFalse(registry.hasAgent(new MockAgentInfo("x")));
        assertFalse(registry.hasAgent(null));
        assertNull(registry.retrieveAgent(null));
        assertFalse(registry.addAgent(null));
    }

    // ── Agent info ordering ──────────────────────────────────────────

    @Test
    void getAgentsInfo_shouldContainAllAgentNames() {
        Set<String> expectedNames = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            String name = "Agent-" + i;
            expectedNames.add(name);
            Agent agent = mock(Agent.class);
            MockAgentInfo info = new MockAgentInfo(name);
            when(agent.getInfo()).thenReturn(info);
            registry.addAgent(agent);
        }

        String infoStr = registry.getAgentsInfo();
        for (String name : expectedNames) {
            assertTrue(infoStr.contains(name),
                    "getAgentsInfo should contain " + name);
        }
    }

    // ── Repeated add of same agent ───────────────────────────────────

    @Test
    void addSameAgentMultipleTimes_shouldNotIncrementCount() {
        Agent agent = mock(Agent.class);
        MockAgentInfo info = new MockAgentInfo("Repeated");
        when(agent.getInfo()).thenReturn(info);

        for (int i = 0; i < 50; i++) {
            assertTrue(registry.addAgent(agent));
        }

        assertEquals(1, registry.getAgentCount());
        assertSame(agent, registry.retrieveAgent(info));
    }
}
