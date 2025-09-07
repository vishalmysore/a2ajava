package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class FilePartTest {
    
    private FilePart filePart;
    private static final String TEST_TYPE = "file";

    @BeforeEach
    void setUp() {
        filePart = new FilePart();
    }

    @Test
    void testBasicFilePartCreation() {
        assertNotNull(filePart, "FilePart should be created successfully");
        assertEquals(TEST_TYPE, filePart.getType(), "Default type should be 'file'");
    }

    @Test
    void testFileContentOperations() {
        FileContent fileContent = new FileContent();
        fileContent.setName("test.txt");
        fileContent.setBytes("VGVzdCBjb250ZW50"); // base64 encoded "Test content"
        fileContent.setMimeType("text/plain");
        
        filePart.setFile(fileContent);
        assertNotNull(filePart.getFile(), "File content should not be null");
        assertEquals("test.txt", filePart.getFile().getName(), "File name should match");
        assertEquals("VGVzdCBjb250ZW50", filePart.getFile().getBytes(), "File bytes should match");
        assertEquals("text/plain", filePart.getFile().getMimeType(), "MIME type should match");
    }

    @Test
    void testFileContentWithUri() {
        FileContent fileContent = new FileContent();
        fileContent.setName("test.txt");
        fileContent.setUri("http://example.com/test.txt");
        fileContent.setMimeType("text/plain");
        
        filePart.setFile(fileContent);
        assertNotNull(filePart.getFile(), "File content should not be null");
        assertEquals("test.txt", filePart.getFile().getName(), "File name should match");
        assertEquals("http://example.com/test.txt", filePart.getFile().getUri(), "File URI should match");
        assertEquals("text/plain", filePart.getFile().getMimeType(), "MIME type should match");
    }

    @Test
    void testMetadataOperations() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key1", "value1");
        metadata.put("key2", "value2");
        
        filePart.setMetadata(metadata);
        assertEquals(metadata, filePart.getMetadata(), "Metadata should match set value");
        assertEquals("value1", filePart.getMetadata().get("key1"), "Should retrieve correct metadata value");
    }

    @Test
    void testTypeOperations() {
        assertEquals(TEST_TYPE, filePart.getType(), "Initial type should be 'file'");
        
        filePart.setType("custom_file");
        assertEquals("custom_file", filePart.getType(), "Type should be updated");
    }

    @Test
    void testNullValues() {
        filePart.setFile(null);
        assertNull(filePart.getFile(), "File content should allow null value");
        
        filePart.setMetadata(null);
        assertNull(filePart.getMetadata(), "Metadata should allow null value");
        
        filePart.setType(null);
        assertNull(filePart.getType(), "Type should allow null value");
    }

    @Test
    void testFileContentWithEmptyValues() {
        FileContent fileContent = new FileContent();
        fileContent.setName("");
        fileContent.setBytes("");
        fileContent.setUri("");
        fileContent.setMimeType("");
        
        filePart.setFile(fileContent);
        assertNotNull(filePart.getFile(), "File content should not be null with empty values");
        assertEquals("", filePart.getFile().getName(), "Empty file name should be preserved");
        assertEquals("", filePart.getFile().getBytes(), "Empty bytes should be preserved");
        assertEquals("", filePart.getFile().getUri(), "Empty URI should be preserved");
        assertEquals("", filePart.getFile().getMimeType(), "Empty MIME type should be preserved");
    }



    @Test
    void testWithVariousMimeTypes() {
        FileContent fileContent = new FileContent();
        
        // Test common MIME types
        String[][] mimeTests = {
            {"text.txt", "text/plain"},
            {"doc.pdf", "application/pdf"},
            {"image.jpg", "image/jpeg"},
            {"doc.json", "application/json"},
            {"file.xml", "application/xml"}
        };
        
        for (String[] test : mimeTests) {
            fileContent.setName(test[0]);
            fileContent.setMimeType(test[1]);
            filePart.setFile(fileContent);
            
            assertEquals(test[1], filePart.getFile().getMimeType(), 
                "Should handle " + test[1] + " MIME type");
        }
    }
}
