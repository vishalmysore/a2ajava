package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProviderTest {

    @Test
    public void testNoArgsConstructor() {
        Provider provider = new Provider();
        assertNotNull(provider);
        assertNull(provider.getOrganization());
        assertNull(provider.getUrl());
    }

    @Test
    public void testAllArgsConstructor() {
        Provider provider = new Provider("TestOrg", "https://test.com");
        assertEquals("TestOrg", provider.getOrganization());
        assertEquals("https://test.com", provider.getUrl());
    }

    @Test
    public void testSettersAndGetters() {
        Provider provider = new Provider();
        provider.setOrganization("Org");
        provider.setUrl("http://url.com");

        assertEquals("Org", provider.getOrganization());
        assertEquals("http://url.com", provider.getUrl());
    }

    @Test
    public void testToString() {
        Provider provider = new Provider("Org", "url");
        String toString = provider.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Org"));
        assertTrue(toString.contains("url"));
    }

    @Test
    public void testEqualsAndHashCode() {
        Provider p1 = new Provider("Org", "url");
        Provider p2 = new Provider("Org", "url");
        Provider p3 = new Provider("Different", "url");

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());
    }
}