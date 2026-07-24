package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class VersionNotSupportedErrorTest {

    @Test
    public void testConstructorWithMessage() {
        VersionNotSupportedError error = new VersionNotSupportedError("Version 0.5 is not supported");

        assertEquals(-32009, error.getCode());
        assertEquals("Version 0.5 is not supported", error.getMessage());
        assertNull(error.getData());
    }

    @Test
    public void testConstructorWithMessageAndData() {
        Map<String, Object> data = new HashMap<>();
        data.put("requestedVersion", "0.5");
        data.put("supportedVersions", "1.0, 1.1");

        VersionNotSupportedError error = new VersionNotSupportedError("Unsupported version", data);

        assertEquals(-32009, error.getCode());
        assertEquals("Unsupported version", error.getMessage());
        assertNotNull(error.getData());
        assertEquals("0.5", error.getData().get("requestedVersion"));
        assertEquals("1.0, 1.1", error.getData().get("supportedVersions"));
    }

    @Test
    public void testInheritsFromJSONRPCError() {
        VersionNotSupportedError error = new VersionNotSupportedError("test");
        assertTrue(error instanceof JSONRPCError);
    }

    @Test
    public void testErrorCodeIsAlwaysNeg32009() {
        VersionNotSupportedError error1 = new VersionNotSupportedError("msg1");
        VersionNotSupportedError error2 = new VersionNotSupportedError("msg2", new HashMap<>());

        assertEquals(-32009, error1.getCode());
        assertEquals(-32009, error2.getCode());
    }
}
