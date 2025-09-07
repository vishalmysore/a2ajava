package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class PartTest {
    
    class TestPart extends Part {
        private String type;
        private Map<String, String> metadata = new HashMap<>();

        @Override
        public String getType() {
            return type;
        }

        @Override
        public Map<String, String> getMetadata() {
            return metadata;
        }

        @Override
        public void setType(String type) {
            this.type = type;
        }
    }

    @Test
    void testPartImplementation() {
        Part part = new TestPart();

        // Test type handling
        part.setType("custom_type");
        assertEquals("custom_type", part.getType(), "Type should be updated");

        // Test metadata handling
        assertNotNull(part.getMetadata(), "Metadata should not be null by default");
        assertTrue(part.getMetadata().isEmpty(), "Metadata should be empty by default");
    }

    @Test
    void testMetadataHandling() {
        Part part = new TestPart();

        // Test metadata map operations
        part.getMetadata().put("key1", "value1");
        assertEquals("value1", part.getMetadata().get("key1"), "Should store and retrieve metadata values");

        // Test multiple entries
        part.getMetadata().put("key2", "value2");
        assertEquals(2, part.getMetadata().size(), "Should track multiple metadata entries");
    }

    @Test
    void testTypeHandling() {
        Part part = new TestPart();

        // Test different type values
        part.setType("custom_type");
        assertEquals("custom_type", part.getType(), "Type should be updated");

        part.setType(null);
        assertNull(part.getType(), "Type should allow null value");

        part.setType("");
        assertEquals("", part.getType(), "Type should allow empty string");
    }

    @Test
    void testSubclassSpecificBehavior() {
        // TextPart specific test
        TextPart textPart = new TextPart();
        assertEquals("text", textPart.getType(), "TextPart should have default type 'text'");

        // FilePart specific test
        FilePart filePart = new FilePart();
        assertEquals("file", filePart.getType(), "FilePart should have default type 'file'");

        // DataPart specific test
        DataPart dataPart = new DataPart();
        assertEquals("data", dataPart.getType(), "DataPart should have default type 'data'");
    }
}
