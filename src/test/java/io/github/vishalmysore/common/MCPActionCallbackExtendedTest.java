package io.github.vishalmysore.common;

import com.t4a.detect.ActionState;
import io.github.vishalmysore.mcp.domain.CallToolResult;
import io.github.vishalmysore.mcp.domain.Content;
import io.github.vishalmysore.mcp.domain.TextContent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class MCPActionCallbackExtendedTest {
    
    @Test
    void testSetType() {
        MCPActionCallback callback = new MCPActionCallback();
        assertEquals(CallBackType.MCP.name(), callback.getType());
    }
    
    @Test
    void testGetType() {
        MCPActionCallback callback = new MCPActionCallback();
        assertEquals(CallBackType.MCP.name(), callback.getType());
    }
    
    @Test
    void testContextHandling() {
        MCPActionCallback callback = new MCPActionCallback();
        Object testContext = new Object();
        callback.setContext(new AtomicReference<>(testContext));
        assertEquals(testContext, callback.getContext());
    }
    
    @Test
    void testSendStatusWithNullContent() {
        MCPActionCallback callback = new MCPActionCallback();
        CallToolResult result = new CallToolResult();
        callback.setContext(new AtomicReference<>(result));
        
        callback.sendtStatus("Test status ", ActionState.WORKING);
        
        List<Content> content = result.getContent();
        assertNotNull(content);
        assertEquals(1, content.size());
        assertEquals("text", content.get(0).getType());
        TextContent textContent = (TextContent) content.get(0);
        assertEquals("Test status working", textContent.getText());
    }
    
    @Test
    void testSendStatusWithExistingContent() {
        MCPActionCallback callback = new MCPActionCallback();
        CallToolResult result = new CallToolResult();
        List<Content> existingContent = new ArrayList<>();
        TextContent existingTextContent = new TextContent();
        existingTextContent.setType("text");
        existingTextContent.setText("Existing content");
        existingContent.add(existingTextContent);
        result.setContent(existingContent);
        callback.setContext(new AtomicReference<>(result));
        
        callback.sendtStatus("Test status ", ActionState.COMPLETED);
        
        List<Content> content = result.getContent();
        assertNotNull(content);
        assertEquals(2, content.size());
        TextContent firstContent = (TextContent) content.get(0);
        assertEquals("Existing content", firstContent.getText());
        TextContent secondContent = (TextContent) content.get(1);
        assertEquals("Test status completed", secondContent.getText());
    }
}