package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TextContentTest {
    
    private TextContent textContent;
    private static final String TEST_TEXT = "Test text content";
    private static final String TEST_TYPE = "text/plain";
    
    @BeforeEach
    void setUp() {
        textContent = new TextContent();
    }
    
    @Test
    void testBasicTextContentCreation() {
        assertNotNull(textContent, "TextContent should be created successfully");
    }
    
    @Test
    void testTextHandling() {
        textContent.setText(TEST_TEXT);
        assertEquals(TEST_TEXT, textContent.getText(), "Text should match set value");
    }
    
    @Test
    void testTypeHandling() {
        textContent.setType(TEST_TYPE);
        assertEquals(TEST_TYPE, textContent.getType(), "Type should match set value");
    }
    
    @Test
    void testTextContentWithAnnotations() {
        Annotations annotations = new Annotations();
        annotations.setPriority(1.5);
        
        textContent.setAnnotations(annotations);
        textContent.setText(TEST_TEXT);
        textContent.setType(TEST_TYPE);
        
        assertEquals(TEST_TEXT, textContent.getText(), "Text should match set value");
        assertEquals(TEST_TYPE, textContent.getType(), "Type should match set value");
        assertEquals(1.5, textContent.getAnnotations().getPriority(), "Annotations priority should match");
    }
    
    @Test
    void testNullValues() {
        textContent.setText(null);
        textContent.setType(null);
        textContent.setAnnotations(null);
        
        assertNull(textContent.getText(), "Text should be null");
        assertNull(textContent.getType(), "Type should be null");
        assertNull(textContent.getAnnotations(), "Annotations should be null");
    }
    
    @Test
    void testEmptyValues() {
        textContent.setText("");
        textContent.setType("");
        
        assertEquals("", textContent.getText(), "Text should be empty string");
        assertEquals("", textContent.getType(), "Type should be empty string");
    }
    
    @Test
    void testToString() {
        textContent.setText(TEST_TEXT);
        textContent.setType(TEST_TYPE);
        
        String result = textContent.toString();
        assertTrue(result.contains(TEST_TEXT), "ToString should contain the text value");
        assertTrue(result.contains(TEST_TYPE), "ToString should contain the type value");
    }
    
    @Test
    void testLongText() {
        String longText = "This is a very long text that should be handled properly by the TextContent class. " +
                "It contains multiple sentences and should be stored and retrieved correctly without any truncation " +
                "or modification. This tests the ability to handle larger text inputs which is common in real-world usage.";
        
        textContent.setText(longText);
        assertEquals(longText, textContent.getText(), "Long text should be stored and retrieved without modification");
    }
    
    @Test
    void testSpecialCharacters() {
        String specialText = "Special characters: !@#$%^&*()_+{}|:<>?~`-=[]\\;',./\n\t\"";
        textContent.setText(specialText);
        assertEquals(specialText, textContent.getText(), "Special characters should be preserved");
    }
    
    @Test
    void testHtmlContent() {
        String htmlContent = "<html><body><h1>Test</h1><p>This is HTML content</p></body></html>";
        textContent.setText(htmlContent);
        textContent.setType("text/html");
        
        assertEquals(htmlContent, textContent.getText(), "HTML content should be preserved");
        assertEquals("text/html", textContent.getType(), "Type should be text/html");
    }
}