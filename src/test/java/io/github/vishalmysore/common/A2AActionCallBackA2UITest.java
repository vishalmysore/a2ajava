package io.github.vishalmysore.common;

import io.github.vishalmysore.a2a.domain.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test A2UI support in A2AActionCallBack
 */
public class A2AActionCallBackA2UITest {
    
    @Test
    void testAddA2UIContentWithNullStatus() {
        A2AActionCallBack callback = new A2AActionCallBack();
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));
        
        Map<String, Object> a2uiMessage = new HashMap<>();
        Map<String, Object> beginRendering = new HashMap<>();
        beginRendering.put("surfaceId", "test_surface");
        a2uiMessage.put("beginRendering", beginRendering);
        
        callback.addA2UIContent(a2uiMessage);
        
        Message statusMessage = task.getStatus().getMessage();
        assertNotNull(statusMessage);
        assertNotNull(statusMessage.getParts());
        assertEquals(1, statusMessage.getParts().size());
        
        Part part = statusMessage.getParts().get(0);
        assertTrue(part instanceof DataPart);
        
        DataPart dataPart = (DataPart) part;
        assertTrue(dataPart.isA2UIData());
        assertEquals("application/json+a2ui", dataPart.getMetadata().get("mimeType"));
        assertTrue(dataPart.getData().containsKey("beginRendering"));
    }
    
    @Test
    void testAddA2UIContentWithExistingStatus() {
        A2AActionCallBack callback = new A2AActionCallBack();
        Task task = new Task();
        
        // Set up existing status with message
        TaskStatus status = new TaskStatus();
        Message message = new Message();
        message.setRole("agent");
        List<Part> parts = new ArrayList<>();
        TextPart textPart = new TextPart();
        textPart.setText("Processing started");
        parts.add(textPart);
        message.setParts(parts);
        status.setMessage(message);
        task.setStatus(status);
        
        callback.setContext(new AtomicReference<>(task));
        
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
        
        List<Part> resultParts = task.getStatus().getMessage().getParts();
        assertNotNull(resultParts);
        assertEquals(2, resultParts.size());
        
        // Check first part is text
        assertTrue(resultParts.get(0) instanceof TextPart);
        TextPart firstPart = (TextPart) resultParts.get(0);
        assertEquals("Processing started", firstPart.getText());
        
        // Check second part is A2UI data
        assertTrue(resultParts.get(1) instanceof DataPart);
        DataPart dataPart = (DataPart) resultParts.get(1);
        assertTrue(dataPart.isA2UIData());
        assertTrue(dataPart.getData().containsKey("surfaceUpdate"));
    }
    
    @Test
    void testMixedContentWithStatusAndA2UI() {
        A2AActionCallBack callback = new A2AActionCallBack();
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));
        
        // Send status (this sets task status and message)
        callback.sendtStatus("Task started", com.t4a.detect.ActionState.WORKING);
        
        // Add A2UI content
        Map<String, Object> a2uiMessage = new HashMap<>();
        Map<String, Object> beginRendering = new HashMap<>();
        beginRendering.put("surfaceId", "dashboard");
        beginRendering.put("catalogId", "https://github.com/google/A2UI/blob/main/specification/0.8/json/standard_catalog_definition.json");
        a2uiMessage.put("beginRendering", beginRendering);
        
        callback.addA2UIContent(a2uiMessage);
        
        // Check task status was set
        assertNotNull(task.getStatus());
        
        // Check A2UI content was added to status message
        Message statusMessage = task.getStatus().getMessage();
        assertNotNull(statusMessage);
        List<Part> parts = statusMessage.getParts();
        assertNotNull(parts);
        assertTrue(parts.size() >= 1); // At least the A2UI part
        
        // Find the A2UI part
        DataPart dataPart = parts.stream()
            .filter(p -> p instanceof DataPart)
            .map(p -> (DataPart) p)
            .filter(DataPart::isA2UIData)
            .findFirst()
            .orElse(null);
        
        assertNotNull(dataPart);
        assertTrue(dataPart.isA2UIData());
    }
    
    @Test
    void testMultipleA2UIContents() {
        A2AActionCallBack callback = new A2AActionCallBack();
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));
        
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
        
        List<Part> parts = task.getStatus().getMessage().getParts();
        assertNotNull(parts);
        assertEquals(2, parts.size());
        
        // Both should be A2UI data
        assertTrue(parts.get(0) instanceof DataPart);
        assertTrue(parts.get(1) instanceof DataPart);
        
        DataPart firstData = (DataPart) parts.get(0);
        DataPart secondData = (DataPart) parts.get(1);
        
        assertTrue(firstData.isA2UIData());
        assertTrue(secondData.isA2UIData());
        assertTrue(firstData.getData().containsKey("beginRendering"));
        assertTrue(secondData.getData().containsKey("surfaceUpdate"));
    }
    
    @Test
    void testA2UIContentStructure() {
        A2AActionCallBack callback = new A2AActionCallBack();
        Task task = new Task();
        callback.setContext(new AtomicReference<>(task));
        
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
        
        List<Part> parts = task.getStatus().getMessage().getParts();
        assertEquals(1, parts.size());
        
        DataPart dataPart = (DataPart) parts.get(0);
        assertTrue(dataPart.isA2UIData());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> update = (Map<String, Object>) dataPart.getData().get("surfaceUpdate");
        assertEquals("analytics_dashboard", update.get("surfaceId"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> root = (Map<String, Object>) update.get("rootComponent");
        assertEquals("Container", root.get("componentType"));
    }
}
