package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CallToolResultTest {
    
    private CallToolResult callToolResult;
    private TextContent textContent;
    private ImageContent imageContent;
    private Annotations annotations;
    
    @BeforeEach
    void setUp() {
        callToolResult = new CallToolResult();
        textContent = new TextContent();
        imageContent = new ImageContent();
        annotations = new Annotations();
    }
    
    @Test
    void testEmptyCallToolResult() {
        assertNotNull(callToolResult, "CallToolResult should be created successfully");
        assertNull(callToolResult.getContent(), "Content should be null by default");
    }
    
    @Test
    void testWithTextContent() {
        textContent.setText("Test text content");
        textContent.setType("text/plain");
        
        List<Content> contentList = new ArrayList<>();
        contentList.add(textContent);
        
        callToolResult.setContent(contentList);
        
        assertNotNull(callToolResult.getContent(), "Content should not be null");
        assertEquals(1, callToolResult.getContent().size(), "Content list should have one item");
        assertTrue(callToolResult.getContent().get(0) instanceof TextContent, "First content item should be TextContent");
        
        TextContent retrievedContent = (TextContent) callToolResult.getContent().get(0);
        assertEquals("Test text content", retrievedContent.getText(), "Text content should match");
        assertEquals("text/plain", retrievedContent.getType(), "Type should match");
    }
    
    @Test
    void testWithMultipleContentTypes() {
        textContent.setText("Test text content");
        textContent.setType("text/plain");
        
        imageContent.setData("base64EncodedData");
        imageContent.setMimeType("image/png");
        
        List<Content> contentList = new ArrayList<>();
        contentList.add(textContent);
        contentList.add(imageContent);
        
        callToolResult.setContent(contentList);
        
        assertNotNull(callToolResult.getContent(), "Content should not be null");
        assertEquals(2, callToolResult.getContent().size(), "Content list should have two items");
        
        assertTrue(callToolResult.getContent().get(0) instanceof TextContent, "First content item should be TextContent");
        assertTrue(callToolResult.getContent().get(1) instanceof ImageContent, "Second content item should be ImageContent");
        
        ImageContent retrievedImageContent = (ImageContent) callToolResult.getContent().get(1);
        assertEquals("base64EncodedData", retrievedImageContent.getData(), "Image data should match");
        assertEquals("image/png", retrievedImageContent.getMimeType(), "MIME type should match");
    }
    
    @Test
    void testWithAnnotations() {
        annotations.setPriority(1.5);
        
        textContent.setText("Annotated text");
        textContent.setType("text/plain");
        textContent.setAnnotations(annotations);
        
        List<Content> contentList = new ArrayList<>();
        contentList.add(textContent);
        
        callToolResult.setContent(contentList);
        
        TextContent retrievedContent = (TextContent) callToolResult.getContent().get(0);
        assertNotNull(retrievedContent.getAnnotations(), "Annotations should not be null");
        assertEquals(1.5, retrievedContent.getAnnotations().getPriority(), "Priority should match");
    }
    
    @Test
    void testWithNullContent() {
        callToolResult.setContent(null);
        assertNull(callToolResult.getContent(), "Content should remain null");
    }
    
    @Test
    void testWithEmptyContentList() {
        callToolResult.setContent(new ArrayList<>());
        assertNotNull(callToolResult.getContent(), "Content list should not be null");
        assertEquals(0, callToolResult.getContent().size(), "Content list should be empty");
    }
    
    @Test
    void testToString() {
        textContent.setText("ToString test");
        textContent.setType("text/plain");
        
        List<Content> contentList = new ArrayList<>();
        contentList.add(textContent);
        
        callToolResult.setContent(contentList);
        
        String result = callToolResult.toString();
        
        assertNotNull(result, "ToString result should not be null");
        assertTrue(result.contains("ToString test"), "ToString should contain text content");
    }
    
    @Test
    void testEquality() {
        textContent.setText("Equality test");
        
        List<Content> contentList1 = new ArrayList<>();
        contentList1.add(textContent);
        
        List<Content> contentList2 = new ArrayList<>();
        contentList2.add(textContent);
        
        CallToolResult result1 = new CallToolResult();
        result1.setContent(contentList1);
        
        CallToolResult result2 = new CallToolResult();
        result2.setContent(contentList2);
        
        assertEquals(result1, result1, "Object should equal itself");
        assertNotEquals(result1, new Object(), "Different types should not be equal");
    }
}