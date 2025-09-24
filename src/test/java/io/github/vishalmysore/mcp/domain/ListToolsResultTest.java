package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListToolsResultTest {

    @Test
    public void testGetSetTools() {
        ListToolsResult result = new ListToolsResult();
        List<Tool> tools = new ArrayList<>();
        
        Tool tool = new Tool();
        tool.setName("testTool");
        tool.setDescription("Test Description");
        tools.add(tool);
        
        result.setTools(tools);
        
        assertEquals(tools, result.getTools());
        assertEquals(1, result.getTools().size());
        assertEquals("testTool", result.getTools().get(0).getName());
    }

    @Test
    public void testGetSetMeta() {
        ListToolsResult result = new ListToolsResult();
        Map<String, Object> meta = new HashMap<>();
        meta.put("key", "value");
        
        result.setMeta(meta);
        
        assertEquals(meta, result.getMeta());
        assertEquals("value", result.getMeta().get("key"));
    }

    @Test
    public void testRetrieveToolList_EmptyList() {
        ListToolsResult result = new ListToolsResult();
        result.setTools(Collections.emptyList());
        
        String toolList = result.retrieveToolList();
        
        assertEquals("[]", toolList);
    }

    @Test
    public void testRetrieveToolList_NullList() {
        ListToolsResult result = new ListToolsResult();
        result.setTools(null);
        
        String toolList = result.retrieveToolList();
        
        assertEquals("[]", toolList);
    }

    @Test
    public void testRetrieveToolList_SingleTool() {
        ListToolsResult result = new ListToolsResult();
        List<Tool> tools = new ArrayList<>();
        
        Tool tool = new Tool();
        tool.setName("testTool");
        tool.setDescription("Test Description");
        tools.add(tool);
        
        result.setTools(tools);
        
        String toolList = result.retrieveToolList();
        
        assertTrue(toolList.contains("\"toolName\": \"testTool\""));
        assertTrue(toolList.contains("\"toolDescription\": \"Test Description\""));
    }

    @Test
    public void testRetrieveToolList_MultipleTool() {
        ListToolsResult result = new ListToolsResult();
        List<Tool> tools = new ArrayList<>();
        
        Tool tool1 = new Tool();
        tool1.setName("testTool1");
        tool1.setDescription("Test Description 1");
        tools.add(tool1);
        
        Tool tool2 = new Tool();
        tool2.setName("testTool2");
        tool2.setDescription("Test Description 2");
        tools.add(tool2);
        
        result.setTools(tools);
        
        String toolList = result.retrieveToolList();
        
        assertTrue(toolList.contains("\"toolName\": \"testTool1\""));
        assertTrue(toolList.contains("\"toolDescription\": \"Test Description 1\""));
        assertTrue(toolList.contains("\"toolName\": \"testTool2\""));
        assertTrue(toolList.contains("\"toolDescription\": \"Test Description 2\""));
        assertTrue(toolList.contains(",\n"));
    }
    
    @Test
    public void testRetrieveToolList_EscapeQuotes() {
        ListToolsResult result = new ListToolsResult();
        List<Tool> tools = new ArrayList<>();
        
        Tool tool = new Tool();
        tool.setName("test\"Tool");
        tool.setDescription("Test \"Description\"");
        tools.add(tool);
        
        result.setTools(tools);
        
        String toolList = result.retrieveToolList();
        
        assertTrue(toolList.contains("\"toolName\": \"test\\\"Tool\""));
        assertTrue(toolList.contains("\"toolDescription\": \"Test \\\"Description\\\"\""));
    }
    
    @Test
    public void testEquals() {
        ListToolsResult result1 = new ListToolsResult();
        List<Tool> tools1 = new ArrayList<>();
        Tool tool1 = new Tool();
        tool1.setName("testTool");
        tools1.add(tool1);
        result1.setTools(tools1);

        ListToolsResult result2 = new ListToolsResult();
        List<Tool> tools2 = new ArrayList<>();
        Tool tool2 = new Tool();
        tool2.setName("testTool");
        tools2.add(tool2);
        result2.setTools(tools2);

        ListToolsResult result3 = new ListToolsResult();
        List<Tool> tools3 = new ArrayList<>();
        Tool tool3 = new Tool();
        tool3.setName("differentTool");
        tools3.add(tool3);
        result3.setTools(tools3);

        assertEquals(result1, result2);
        assertNotEquals(result1, result3);
        assertEquals(result1.hashCode(), result2.hashCode());
    }

    @Test
    public void testToString() {
        ListToolsResult result = new ListToolsResult();
        List<Tool> tools = new ArrayList<>();
        
        Tool tool = new Tool();
        tool.setName("testTool");
        tool.setDescription("Test Description");
        tools.add(tool);
        
        result.setTools(tools);
        
        String toString = result.toString();
        assertTrue(toString.contains("tools="));
        assertTrue(toString.contains("testTool"));
    }
}