package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class AgentExtensionTest {

    @Test
    public void testNoArgsConstructor() {
        AgentExtension extension = new AgentExtension();
        assertNotNull(extension);
        assertNull(extension.getUri());
        assertNull(extension.getDescription());
        assertFalse(extension.isRequired());
        assertNull(extension.getParams());
    }

    @Test
    public void testAllArgsConstructor() {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        AgentExtension extension = new AgentExtension("uri", "description", true, params);
        assertEquals("uri", extension.getUri());
        assertEquals("description", extension.getDescription());
        assertTrue(extension.isRequired());
        assertEquals(params, extension.getParams());
    }

    @Test
    public void testSettersAndGetters() {
        AgentExtension extension = new AgentExtension();
        extension.setUri("testUri");
        extension.setDescription("testDescription");
        extension.setRequired(true);
        Map<String, Object> params = new HashMap<>();
        params.put("test", "value");
        extension.setParams(params);

        assertEquals("testUri", extension.getUri());
        assertEquals("testDescription", extension.getDescription());
        assertTrue(extension.isRequired());
        assertEquals(params, extension.getParams());
    }

    @Test
    public void testToString() {
        AgentExtension extension = new AgentExtension("uri", "desc", false, null);
        String toString = extension.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("uri"));
        assertTrue(toString.contains("desc"));
    }

    @Test
    public void testEqualsAndHashCode() {
        AgentExtension ext1 = new AgentExtension("uri", "desc", true, null);
        AgentExtension ext2 = new AgentExtension("uri", "desc", true, null);
        AgentExtension ext3 = new AgentExtension("different", "desc", true, null);

        assertEquals(ext1, ext2);
        assertNotEquals(ext1, ext3);
        assertEquals(ext1.hashCode(), ext2.hashCode());
        assertNotEquals(ext1.hashCode(), ext3.hashCode());
    }
}