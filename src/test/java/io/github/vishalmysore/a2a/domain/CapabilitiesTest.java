package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Capabilities domain object
 */
public class CapabilitiesTest {

    @Test
    void testDefaultConstructor() {
        Capabilities capabilities = new Capabilities();
        
        assertFalse(capabilities.isStreaming());
        assertFalse(capabilities.isPushNotifications());
        assertFalse(capabilities.isStateTransitionHistory());
    }

    @Test
    void testAllArgsConstructor() {
        Capabilities capabilities = new Capabilities(true, true, true);
        
        assertTrue(capabilities.isStreaming());
        assertTrue(capabilities.isPushNotifications());
        assertTrue(capabilities.isStateTransitionHistory());
    }

    @Test
    void testMixedCapabilitiesConstructor() {
        Capabilities capabilities = new Capabilities(true, false, true);
        
        assertTrue(capabilities.isStreaming());
        assertFalse(capabilities.isPushNotifications());
        assertTrue(capabilities.isStateTransitionHistory());
    }

    @Test
    void testSetters() {
        Capabilities capabilities = new Capabilities();
        
        capabilities.setStreaming(true);
        assertTrue(capabilities.isStreaming());
        assertFalse(capabilities.isPushNotifications());
        assertFalse(capabilities.isStateTransitionHistory());
        
        capabilities.setPushNotifications(true);
        assertTrue(capabilities.isStreaming());
        assertTrue(capabilities.isPushNotifications());
        assertFalse(capabilities.isStateTransitionHistory());
        
        capabilities.setStateTransitionHistory(true);
        assertTrue(capabilities.isStreaming());
        assertTrue(capabilities.isPushNotifications());
        assertTrue(capabilities.isStateTransitionHistory());
    }

    @Test
    void testToString() {
        Capabilities capabilities = new Capabilities(true, false, true);
        String toStringResult = capabilities.toString();
        
        assertTrue(toStringResult.contains("streaming=true"));
        assertTrue(toStringResult.contains("pushNotifications=false"));
        assertTrue(toStringResult.contains("stateTransitionHistory=true"));
    }

    @Test
    void testEquals() {
        Capabilities capabilities1 = new Capabilities(true, false, true);
        Capabilities capabilities2 = new Capabilities(true, false, true);
        Capabilities capabilities3 = new Capabilities(false, false, true);
        
        assertEquals(capabilities1, capabilities2);
        assertNotEquals(capabilities1, capabilities3);
    }

    @Test
    void testHashCode() {
        Capabilities capabilities1 = new Capabilities(true, false, true);
        Capabilities capabilities2 = new Capabilities(true, false, true);
        
        assertEquals(capabilities1.hashCode(), capabilities2.hashCode());
    }
}