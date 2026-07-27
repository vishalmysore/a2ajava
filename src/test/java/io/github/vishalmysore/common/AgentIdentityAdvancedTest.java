package io.github.vishalmysore.common;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Advanced unit tests for AgentIdentity covering equality contract,
 * hash consistency, thread-safe UUID generation, builder usage, and edge cases.
 */
public class AgentIdentityAdvancedTest {

    // ── UUID uniqueness across many instances ────────────────────────

    @Test
    void builderConstructor_shouldGenerateUniqueUUIDs() {
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            AgentIdentity identity = AgentIdentity.builder()
                    .info(new MockAgentInfo("Agent-" + i))
                    .url("http://localhost:" + i)
                    .build();
            ids.add(identity.getAgentUniqueIDTobeUsedToIdentifyTheAgent());
        }
        assertEquals(1000, ids.size(), "All generated UUIDs should be unique");
    }

    @Test
    void twoArgConstructor_shouldGenerateUniqueUUIDs() {
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < 500; i++) {
            AgentIdentity identity = new AgentIdentity(
                    new MockAgentInfo("Agent-" + i),
                    "http://localhost:" + i);
            ids.add(identity.getAgentUniqueIDTobeUsedToIdentifyTheAgent());
        }
        assertEquals(500, ids.size());
    }

    // ── Concurrent UUID generation ───────────────────────────────────

    @Test
    void concurrentConstruction_shouldGenerateUniqueUUIDs() throws Exception {
        int threads = 50;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            final int idx = i;
            futures.add(pool.submit(() -> {
                AgentIdentity identity = new AgentIdentity(
                        new MockAgentInfo("Agent-" + idx),
                        "http://localhost:" + idx);
                return identity.getAgentUniqueIDTobeUsedToIdentifyTheAgent();
            }));
        }

        Set<String> ids = new HashSet<>();
        for (Future<String> f : futures) {
            ids.add(f.get(5, TimeUnit.SECONDS));
        }
        pool.shutdown();

        assertEquals(threads, ids.size(), "Concurrent UUID generation should still produce unique IDs");
    }

    // ── Equality contract (based on @EqualsAndHashCode.Include) ──────

    @Test
    void equals_sameUniqueId_differentInfoAndUrl_shouldBeEqual() {
        AgentIdentity a = new AgentIdentity("id-1", new MockAgentInfo("A"), "http://a");
        AgentIdentity b = new AgentIdentity("id-1", new MockAgentInfo("B"), "http://b");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_differentUniqueId_sameInfoAndUrl_shouldNotBeEqual() {
        MockAgentInfo info = new MockAgentInfo("Same");
        AgentIdentity a = new AgentIdentity("id-1", info, "http://same");
        AgentIdentity b = new AgentIdentity("id-2", info, "http://same");

        assertNotEquals(a, b);
    }

    @Test
    void equals_reflexive() {
        AgentIdentity a = new AgentIdentity("id-1", new MockAgentInfo("X"), "http://x");
        assertEquals(a, a);
    }

    @Test
    void equals_symmetric() {
        AgentIdentity a = new AgentIdentity("id-1", new MockAgentInfo("X"), "http://x");
        AgentIdentity b = new AgentIdentity("id-1", new MockAgentInfo("Y"), "http://y");
        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void equals_transitive() {
        AgentIdentity a = new AgentIdentity("id-1", new MockAgentInfo("A"), "http://a");
        AgentIdentity b = new AgentIdentity("id-1", new MockAgentInfo("B"), "http://b");
        AgentIdentity c = new AgentIdentity("id-1", new MockAgentInfo("C"), "http://c");

        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c);
    }

    @Test
    void equals_nullAndOtherType() {
        AgentIdentity a = new AgentIdentity("id-1", new MockAgentInfo("X"), "http://x");
        assertNotEquals(null, a);
        assertNotEquals("a string", a);
        assertNotEquals(42, a);
    }

    // ── hashCode consistency ─────────────────────────────────────────

    @Test
    void hashCode_shouldBeConsistentAcrossMultipleCalls() {
        AgentIdentity a = new AgentIdentity("id-1", new MockAgentInfo("X"), "http://x");
        int hash = a.hashCode();
        for (int i = 0; i < 1000; i++) {
            assertEquals(hash, a.hashCode());
        }
    }

    @Test
    void hashCode_equalObjectsShouldHaveSameHash() {
        AgentIdentity a = new AgentIdentity("id-1", new MockAgentInfo("A"), "http://a");
        AgentIdentity b = new AgentIdentity("id-1", new MockAgentInfo("B"), "http://b");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    // ── HashMap / HashSet behavior ───────────────────────────────────

    @Test
    void identityAsHashMapKey_shouldFindByEqualKey() {
        AgentIdentity key1 = new AgentIdentity("id-1", new MockAgentInfo("A"), "http://a");
        AgentIdentity key2 = new AgentIdentity("id-1", new MockAgentInfo("B"), "http://b");

        Map<AgentIdentity, String> map = new HashMap<>();
        map.put(key1, "value1");

        // key2 has same uniqueId, should find the entry
        assertTrue(map.containsKey(key2));
        assertEquals("value1", map.get(key2));
    }

    @Test
    void identityInHashSet_shouldDeduplicateByUniqueId() {
        Set<AgentIdentity> set = new HashSet<>();
        set.add(new AgentIdentity("id-1", new MockAgentInfo("A"), "http://a"));
        set.add(new AgentIdentity("id-1", new MockAgentInfo("B"), "http://b"));
        set.add(new AgentIdentity("id-2", new MockAgentInfo("C"), "http://c"));

        assertEquals(2, set.size());
    }

    // ── toString ─────────────────────────────────────────────────────

    @Test
    void toString_shouldContainUniqueIdAndAgentInfo_butNotUrl() {
        AgentIdentity identity = new AgentIdentity("agent-xyz", new MockAgentInfo("TestAgent"), "http://secret-url");
        String str = identity.toString();

        assertTrue(str.contains("agent-xyz"));
        assertTrue(str.contains("allTheCapabilitiesOfTheAgent"));
        assertFalse(str.contains("http://secret-url"),
                "URL should be excluded from toString per @ToString annotation");
    }

    // ── Setter mutations ─────────────────────────────────────────────

    @Test
    void setters_shouldOverrideConstructorValues() {
        AgentIdentity identity = new AgentIdentity("old-id", new MockAgentInfo("Old"), "http://old");

        identity.setAgentUniqueIDTobeUsedToIdentifyTheAgent("new-id");
        identity.setAllTheCapabilitiesOfTheAgent(new MockAgentInfo("New"));
        identity.setUrl("http://new");

        assertEquals("new-id", identity.getAgentUniqueIDTobeUsedToIdentifyTheAgent());
        assertEquals("New", ((MockAgentInfo) identity.getAllTheCapabilitiesOfTheAgent()).getAgentName());
        assertEquals("http://new", identity.getUrl());
    }

    @Test
    void setUniqueId_shouldChangeEqualityBehavior() {
        AgentIdentity a = new AgentIdentity("id-1", new MockAgentInfo("A"), "http://a");
        AgentIdentity b = new AgentIdentity("id-1", new MockAgentInfo("B"), "http://b");

        assertEquals(a, b);

        b.setAgentUniqueIDTobeUsedToIdentifyTheAgent("id-2");
        assertNotEquals(a, b);
    }

    // ── Null fields ──────────────────────────────────────────────────

    @Test
    void constructorWithNullInfo_shouldNotThrow() {
        AgentIdentity identity = new AgentIdentity(null, null);
        assertNotNull(identity.getAgentUniqueIDTobeUsedToIdentifyTheAgent());
        assertNull(identity.getAllTheCapabilitiesOfTheAgent());
        assertNull(identity.getUrl());
    }

    @Test
    void threeArgConstructorWithNulls_shouldNotThrow() {
        AgentIdentity identity = new AgentIdentity(null, null, null);
        assertNull(identity.getAgentUniqueIDTobeUsedToIdentifyTheAgent());
        assertNull(identity.getAllTheCapabilitiesOfTheAgent());
        assertNull(identity.getUrl());
    }

    // ── Builder with nulls ───────────────────────────────────────────

    @Test
    void builder_withNullInfoAndUrl_shouldStillGenerateUUID() {
        AgentIdentity identity = AgentIdentity.builder()
                .info(null)
                .url(null)
                .build();

        assertNotNull(identity.getAgentUniqueIDTobeUsedToIdentifyTheAgent());
        assertNull(identity.getAllTheCapabilitiesOfTheAgent());
        assertNull(identity.getUrl());
    }
}
