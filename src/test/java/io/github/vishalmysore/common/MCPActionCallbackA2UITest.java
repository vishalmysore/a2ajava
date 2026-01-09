package io.github.vishalmysore.common;

import io.github.vishalmysore.mcp.domain.CallToolResult;
import io.github.vishalmysore.mcp.domain.Content;
import io.github.vishalmysore.mcp.domain.DataContent;
import io.github.vishalmysore.mcp.domain.TextContent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test A2UI support in MCPActionCallback
 */
public class MCPActionCallbackA2UITest {
    
    @Test
    void testAddA2UIContentWithNullContent() {
        MCPActionCallback callback = new MCPActionCallback();
        CallToolResult result = new CallToolResult();
        callback.setContext(result);
        
        Map<String, Object> a2uiMessage = new HashMap<>();
        Map<String, Object> beginRendering = new HashMap<>();
        beginRendering.put("surfaceId", "test_surface");
        a2uiMessage.put("beginRendering", beginRendering);
        
        callback.addA2UIContent(a2uiMessage);
        
        List<Content> content = result.getContent();
        assertNotNull(content);
        assertEquals(1, content.size());
        assertEquals("data", content.get(0).getType());
        
        DataContent dataContent = (DataContent) content.get(0);
        assertTrue(dataContent.isA2UIData());
        assertEquals("application/json+a2ui", dataContent.getMetadata().get("mimeType"));
        assertTrue(dataContent.getData().containsKey("beginRendering"));
    }
    
    @Test
    void testAddA2UIContentWithExistingContent() {
        MCPActionCallback callback = new MCPActionCallback();
        CallToolResult result = new CallToolResult();
        
        // Add existing text content
        List<Content> existingContent = new ArrayList<>();
        TextContent textContent = new TextContent();
        textContent.setType("text");
        textContent.setText("Processing started");
        existingContent.add(textContent);
        result.setContent(existingContent);
        
        callback.setContext(result);
        
        // Add A2UI content
        Map<String, Object> a2uiMessage = new HashMap<>();
        Map<String, Object> surfaceUpdate = new HashMap<>();
        surfaceUpdate.put("surfaceId", "main_content");
        Map<String, Object> component = new HashMap<>();
        component.put("componentType", "Text");
        component.put("id", "text_1");
        Map<String, Object> props = new HashMap<>();
        props.put("text", "Hello from A2UI!");
        component.put("props", props);
        surfaceUpdate.put("rootComponent", component);
        a2uiMessage.put("surfaceUpdate", surfaceUpdate);
        
        callback.addA2UIContent(a2uiMessage);
        
        List<Content> content = result.getContent();
        assertNotNull(content);
        assertEquals(2, content.size());
        
        // Check first content is text
        assertTrue(content.get(0) instanceof TextContent);
        TextContent firstContent = (TextContent) content.get(0);
        assertEquals("Processing started", firstContent.getText());
        
        // Check second content is A2UI data
        assertTrue(content.get(1) instanceof DataContent);
        DataContent dataContent = (DataContent) content.get(1);
        assertTrue(dataContent.isA2UIData());
        assertTrue(dataContent.getData().containsKey("surfaceUpdate"));
    }
    
    @Test
    void testMixedContentWithStatusAndA2UI() {
        MCPActionCallback callback = new MCPActionCallback();
        CallToolResult result = new CallToolResult();
        callback.setContext(result);
        
        // Add status
        callback.sendtStatus("Task started ", com.t4a.detect.ActionState.WORKING);
        
        // Add A2UI content
        Map<String, Object> a2uiMessage = new HashMap<>();
        Map<String, Object> beginRendering = new HashMap<>();
        beginRendering.put("surfaceId", "dashboard");
        beginRendering.put("catalogId", "https://github.com/google/A2UI/blob/main/specification/0.8/json/standard_catalog_definition.json");
        a2uiMessage.put("beginRendering", beginRendering);
        
        callback.addA2UIContent(a2uiMessage);
        
        // Add another status
        callback.sendtStatus("Task completed ", com.t4a.detect.ActionState.COMPLETED);
        
        List<Content> content = result.getContent();
        assertNotNull(content);
        assertEquals(3, content.size());
        
        // Check order and types
        assertTrue(content.get(0) instanceof TextContent);
        assertTrue(content.get(1) instanceof DataContent);
        assertTrue(content.get(2) instanceof TextContent);
        
        DataContent dataContent = (DataContent) content.get(1);
        assertTrue(dataContent.isA2UIData());
    }
    
    @Test
    void testMultipleA2UIContents() {
        MCPActionCallback callback = new MCPActionCallback();
        CallToolResult result = new CallToolResult();
        callback.setContext(result);
        
        // Add first A2UI content - beginRendering
        Map<String, Object> beginMessage = new HashMap<>();
        Map<String, Object> beginRendering = new HashMap<>();
        beginRendering.put("surfaceId", "main_surface");
        beginMessage.put("beginRendering", beginRendering);
        callback.addA2UIContent(beginMessage);
        
        // Add second A2UI content - surfaceUpdate
        Map<String, Object> updateMessage = new HashMap<>();
        Map<String, Object> surfaceUpdate = new HashMap<>();
        surfaceUpdate.put("surfaceId", "main_surface");
        Map<String, Object> component = new HashMap<>();
        component.put("componentType", "Chart");
        component.put("id", "chart_1");
        surfaceUpdate.put("rootComponent", component);
        updateMessage.put("surfaceUpdate", surfaceUpdate);
        callback.addA2UIContent(updateMessage);
        
        List<Content> content = result.getContent();
        assertNotNull(content);
        assertEquals(2, content.size());
        
        // Both should be A2UI data
        assertTrue(content.get(0) instanceof DataContent);
        assertTrue(content.get(1) instanceof DataContent);
        
        DataContent firstData = (DataContent) content.get(0);
        DataContent secondData = (DataContent) content.get(1);
        
        assertTrue(firstData.isA2UIData());
        assertTrue(secondData.isA2UIData());
        assertTrue(firstData.getData().containsKey("beginRendering"));
        assertTrue(secondData.getData().containsKey("surfaceUpdate"));
    }
    
    @Test
    void testA2UIContentStructure() {
        MCPActionCallback callback = new MCPActionCallback();
        CallToolResult result = new CallToolResult();
        callback.setContext(result);
        
        // Create complex A2UI structure
        Map<String, Object> a2uiMessage = new HashMap<>();
        Map<String, Object> surfaceUpdate = new HashMap<>();
        surfaceUpdate.put("surfaceId", "analytics_dashboard");
        
        // Create component tree
        Map<String, Object> rootComponent = new HashMap<>();
        rootComponent.put("componentType", "Container");
        rootComponent.put("id", "container_1");
        
        Map<String, Object> containerProps = new HashMap<>();
        containerProps.put("layout", "vertical");
        rootComponent.put("props", containerProps);
        
        List<Map<String, Object>> children = new ArrayList<>();
        Map<String, Object> textComponent = new HashMap<>();
        textComponent.put("componentType", "Text");
        textComponent.put("id", "text_1");
        Map<String, Object> textProps = new HashMap<>();
        textProps.put("text", "Dashboard Title");
        textProps.put("style", "heading");
        textComponent.put("props", textProps);
        children.add(textComponent);
        
        rootComponent.put("children", children);
        surfaceUpdate.put("rootComponent", rootComponent);
        a2uiMessage.put("surfaceUpdate", surfaceUpdate);
        
        callback.addA2UIContent(a2uiMessage);
        
        List<Content> content = result.getContent();
        assertEquals(1, content.size());
        
        DataContent dataContent = (DataContent) content.get(0);
        assertTrue(dataContent.isA2UIData());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> update = (Map<String, Object>) dataContent.getData().get("surfaceUpdate");
        assertEquals("analytics_dashboard", update.get("surfaceId"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> root = (Map<String, Object>) update.get("rootComponent");
        assertEquals("Container", root.get("componentType"));
    }
}
