package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Base64;

/**
 * Test class for Authentication domain object
 */
public class AuthenticationTest {

    @Test
    void testDefaultConstructor() {
        Authentication auth = new Authentication();
        assertNull(auth.getSchemes());
        assertNull(auth.getCredentials());
    }

    @Test
    void testConstructorWithSchemes() {
        String[] schemes = new String[]{"Basic", "Bearer"};
        Authentication auth = new Authentication(schemes);
        assertArrayEquals(schemes, auth.getSchemes());
        assertNull(auth.getCredentials());
    }

    @Test
    void testSetBasicAuth() {
        Authentication auth = new Authentication();
        auth.setBasicAuth("username", "password");
        
        assertNotNull(auth.getSchemes());
        assertEquals(1, auth.getSchemes().length);
        assertEquals("Basic", auth.getSchemes()[0]);
        
        String expectedCredentials = Base64.getEncoder().encodeToString("username:password".getBytes());
        assertEquals(expectedCredentials, auth.getCredentials());
        assertTrue(auth.isBasicAuth());
        assertFalse(auth.isBearerAuth());
    }

    @Test
    void testSetBearerAuth() {
        Authentication auth = new Authentication();
        auth.setBearerAuth("my-token");
        
        assertNotNull(auth.getSchemes());
        assertEquals(1, auth.getSchemes().length);
        assertEquals("Bearer", auth.getSchemes()[0]);
        
        assertEquals("my-token", auth.getCredentials());
        assertTrue(auth.isBearerAuth());
        assertFalse(auth.isBasicAuth());
    }

    @Test
    void testSetApiKeyAuth() {
        Authentication auth = new Authentication();
        auth.setApiKeyAuth("api-key-12345");
        
        assertNotNull(auth.getSchemes());
        assertEquals(1, auth.getSchemes().length);
        assertEquals("ApiKey", auth.getSchemes()[0]);
        
        assertEquals("api-key-12345", auth.getCredentials());
        assertFalse(auth.isBasicAuth());
        assertFalse(auth.isBearerAuth());
    }

    @Test
    void testIsBasicAuth() {
        Authentication auth = new Authentication();
        
        // Null schemes
        assertFalse(auth.isBasicAuth());
        
        // Empty schemes
        auth.setSchemes(new String[]{});
        assertFalse(auth.isBasicAuth());
        
        // Non-Basic scheme
        auth.setSchemes(new String[]{"Bearer"});
        assertFalse(auth.isBasicAuth());
        
        // Basic scheme
        auth.setSchemes(new String[]{"Basic"});
        assertTrue(auth.isBasicAuth());
    }

    @Test
    void testIsBearerAuth() {
        Authentication auth = new Authentication();
        
        // Null schemes
        assertFalse(auth.isBearerAuth());
        
        // Empty schemes
        auth.setSchemes(new String[]{});
        assertFalse(auth.isBearerAuth());
        
        // Non-Bearer scheme
        auth.setSchemes(new String[]{"Basic"});
        assertFalse(auth.isBearerAuth());
        
        // Bearer scheme
        auth.setSchemes(new String[]{"Bearer"});
        assertTrue(auth.isBearerAuth());
    }

    @Test
    void testToStringExcludesCredentials() {
        Authentication auth = new Authentication();
        auth.setBasicAuth("username", "password");
        
        String toStringResult = auth.toString();
        
        // Should include schemes
        assertTrue(toStringResult.contains("schemes"));
        
        // Should not include credential values
        assertFalse(toStringResult.contains(auth.getCredentials()));
    }
}