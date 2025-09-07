package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class TextPartTest {
    
    private TextPart textPart;
    private static final String TEST_TEXT = "Test text content";
    private static final String TEST_TYPE = "text";

    @BeforeEach
    void setUp() {
        textPart = new TextPart();
    }

    @Test
    void testBasicTextPartCreation() {
        assertNotNull(textPart, "TextPart should be created successfully");
        assertEquals(TEST_TYPE, textPart.getType(), "Default type should be 'text'");
    }

    @Test
    void testTextPartWithText() {
        textPart.setText(TEST_TEXT);
        assertEquals(TEST_TEXT, textPart.getText(), "Text should match set value");
    }

    @Test
    void testTextPartWithType() {
        String customType = "custom_text";
        textPart.setType(customType);
        assertEquals(customType, textPart.getType(), "Type should match set value");
    }

    @Test
    void testTextPartWithTextAndType() {
        textPart.setText(TEST_TEXT);
        textPart.setType(TEST_TYPE);
        assertEquals(TEST_TEXT, textPart.getText(), "Text should match set value");
        assertEquals(TEST_TYPE, textPart.getType(), "Type should match set value");
    }

    @Test
    void testNullValues() {
        textPart.setText(null);
        textPart.setType(null);
        assertNull(textPart.getText(), "Text should be null");
        assertNull(textPart.getType(), "Type should be null");
    }

    @Test
    void testEmptyValues() {
        textPart.setText("");
        textPart.setType("");
        assertEquals("", textPart.getText(), "Text should be empty string");
        assertEquals("", textPart.getType(), "Type should be empty string");
    }

    @Test
    void testMetadataOperations() {
        // Test empty metadata
        assertNotNull(textPart.getMetadata(), "Metadata should not be null by default");
        assertTrue(textPart.getMetadata().isEmpty(), "Metadata should be empty by default");

        // Test setting and getting metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key1", "value1");
        metadata.put("key2", "value2");
        textPart.setMetadata(metadata);

        assertEquals(metadata, textPart.getMetadata(), "Metadata should match set value");
        assertEquals("value1", textPart.getMetadata().get("key1"), "Should retrieve correct metadata value");
        assertEquals("value2", textPart.getMetadata().get("key2"), "Should retrieve correct metadata value");
    }

    @Test
    void testToString() {
        textPart.setText(TEST_TEXT);
        textPart.setType(TEST_TYPE);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key1", "value1");
        textPart.setMetadata(metadata);

        String toString = textPart.toString();
        assertNotNull(toString, "toString should not be null");
        assertTrue(toString.contains(TEST_TEXT), "toString should contain text");
        assertTrue(toString.contains(TEST_TYPE), "toString should contain type");
        assertTrue(toString.contains("key1"), "toString should contain metadata key");
        assertTrue(toString.contains("value1"), "toString should contain metadata value");
    }

    @Test
    void testLongText() {
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longText.append("Test text line ").append(i).append("\n");
        }
        String text = longText.toString();
        textPart.setText(text);
        assertEquals(text, textPart.getText(), "Long text should be handled correctly");
    }

    @Test
    void testEquality() {
        TextPart part1 = new TextPart();
        TextPart part2 = new TextPart();

        part1.setText(TEST_TEXT);
        part2.setText(TEST_TEXT);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("key", "value");
        part1.setMetadata(metadata);
        part2.setMetadata(new HashMap<>(metadata));

        assertEquals(part1.getText(), part2.getText(), "Text should be equal");
        assertEquals(part1.getType(), part2.getType(), "Type should be equal");
        assertEquals(part1.getMetadata(), part2.getMetadata(), "Metadata should be equal");
    }
}
