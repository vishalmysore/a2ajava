package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class A2UIClientCapabilitiesTest {

    @Test
    public void testNoArgsConstructor() {
        A2UIClientCapabilities capabilities = new A2UIClientCapabilities();
        assertNotNull(capabilities);
        assertNull(capabilities.getSupportedCatalogIds());
        assertNull(capabilities.getInlineCatalogs());
    }

    @Test
    public void testAllArgsConstructor() {
        List<String> catalogIds = Arrays.asList("catalog1", "catalog2");
        List<Map<String, Object>> inlineCatalogs = Arrays.asList(new HashMap<>());
        A2UIClientCapabilities capabilities = new A2UIClientCapabilities(catalogIds, inlineCatalogs);
        assertEquals(catalogIds, capabilities.getSupportedCatalogIds());
        assertEquals(inlineCatalogs, capabilities.getInlineCatalogs());
    }

    @Test
    public void testSettersAndGetters() {
        A2UIClientCapabilities capabilities = new A2UIClientCapabilities();
        List<String> catalogIds = Arrays.asList("test1", "test2");
        capabilities.setSupportedCatalogIds(catalogIds);
        List<Map<String, Object>> inlineCatalogs = Arrays.asList(new HashMap<>());
        capabilities.setInlineCatalogs(inlineCatalogs);

        assertEquals(catalogIds, capabilities.getSupportedCatalogIds());
        assertEquals(inlineCatalogs, capabilities.getInlineCatalogs());
    }

    @Test
    public void testToString() {
        List<String> catalogIds = Arrays.asList("catalog");
        A2UIClientCapabilities capabilities = new A2UIClientCapabilities(catalogIds, null);
        String toString = capabilities.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("catalog"));
    }

    @Test
    public void testEqualsAndHashCode() {
        List<String> catalogIds = Arrays.asList("catalog");
        A2UIClientCapabilities cap1 = new A2UIClientCapabilities(catalogIds, null);
        A2UIClientCapabilities cap2 = new A2UIClientCapabilities(catalogIds, null);
        A2UIClientCapabilities cap3 = new A2UIClientCapabilities(Arrays.asList("different"), null);

        assertEquals(cap1, cap2);
        assertNotEquals(cap1, cap3);
        assertEquals(cap1.hashCode(), cap2.hashCode());
        assertNotEquals(cap1.hashCode(), cap3.hashCode());
    }
}