package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FileContentTest {
    
    private FileContent fileContent;
    private static final String TEST_NAME = "test.txt";
    private static final String TEST_MIME_TYPE = "text/plain";
    private static final String TEST_BYTES = "VGVzdCBjb250ZW50"; // base64 encoded "Test content"
    private static final String TEST_URI = "http://example.com/test.txt";

    @BeforeEach
    void setUp() {
        fileContent = new FileContent();
    }

    @Test
    void testBasicFileContentCreation() {
        assertNotNull(fileContent, "FileContent should be created successfully");
    }

    @Test
    void testNameHandling() {
        fileContent.setName(TEST_NAME);
        assertEquals(TEST_NAME, fileContent.getName(), "Name should match set value");
    }

    @Test
    void testMimeTypeHandling() {
        fileContent.setMimeType(TEST_MIME_TYPE);
        assertEquals(TEST_MIME_TYPE, fileContent.getMimeType(), "MIME type should match set value");
    }

    @Test
    void testBytesHandling() {
        fileContent.setBytes(TEST_BYTES);
        assertEquals(TEST_BYTES, fileContent.getBytes(), "Bytes should match set value");
    }

    @Test
    void testUriHandling() {
        fileContent.setUri(TEST_URI);
        assertEquals(TEST_URI, fileContent.getUri(), "URI should match set value");
    }

    @Test
    void testNullValues() {
        fileContent.setName(null);
        fileContent.setMimeType(null);
        fileContent.setBytes(null);
        fileContent.setUri(null);
        
        assertNull(fileContent.getName(), "Name should allow null value");
        assertNull(fileContent.getMimeType(), "MIME type should allow null value");
        assertNull(fileContent.getBytes(), "Bytes should allow null value");
        assertNull(fileContent.getUri(), "URI should allow null value");
    }

    @Test
    void testEmptyValues() {
        fileContent.setName("");
        fileContent.setMimeType("");
        fileContent.setBytes("");
        fileContent.setUri("");
        
        assertEquals("", fileContent.getName(), "Name should allow empty string");
        assertEquals("", fileContent.getMimeType(), "MIME type should allow empty string");
        assertEquals("", fileContent.getBytes(), "Bytes should allow empty string");
        assertEquals("", fileContent.getUri(), "URI should allow empty string");
    }

    @Test
    void testToString() {
        fileContent.setName(TEST_NAME);
        fileContent.setMimeType(TEST_MIME_TYPE);
        fileContent.setBytes(TEST_BYTES);
        
        String toString = fileContent.toString();
        assertNotNull(toString, "toString should not be null");
        assertTrue(toString.contains(TEST_NAME), "toString should contain name");
        assertTrue(toString.contains(TEST_MIME_TYPE), "toString should contain MIME type");
        assertTrue(toString.contains(TEST_BYTES), "toString should contain bytes");
    }

    @Test
    void testCompleteFileContent() {
        fileContent.setName(TEST_NAME);
        fileContent.setMimeType(TEST_MIME_TYPE);
        fileContent.setBytes(TEST_BYTES);
        fileContent.setUri(TEST_URI);
        
        assertEquals(TEST_NAME, fileContent.getName(), "Name should match");
        assertEquals(TEST_MIME_TYPE, fileContent.getMimeType(), "MIME type should match");
        assertEquals(TEST_BYTES, fileContent.getBytes(), "Bytes should match");
        assertEquals(TEST_URI, fileContent.getUri(), "URI should match");
    }
    
    @Test
    void testId() {
        String testId = "test-id-123";
        fileContent.setId(testId);
        assertEquals(testId, fileContent.getId(), "ID should match set value");
    }
}
