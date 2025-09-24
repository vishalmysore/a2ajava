package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class PromptTest {
    
    private Prompt prompt;
    private static final String TEST_NAME = "TestPrompt";
    private static final String TEST_DESCRIPTION = "A test prompt description";
    
    @BeforeEach
    void setUp() {
        prompt = new Prompt();
    }
    
    @Test
    void testBasicCreation() {
        assertNotNull(prompt, "Prompt should be created successfully");
    }
    
    @Test
    void testNameHandling() {
        prompt.setName(TEST_NAME);
        assertEquals(TEST_NAME, prompt.getName(), "Name should match set value");
    }
    
    @Test
    void testDescriptionHandling() {
        prompt.setDescription(TEST_DESCRIPTION);
        assertEquals(TEST_DESCRIPTION, prompt.getDescription(), "Description should match set value");
    }
    
    @Test
    void testAnnotationsHandling() {
        Annotations annotations = new Annotations();
        annotations.setPriority(2.0);
        
        prompt.setAnnotations(annotations);
        assertEquals(annotations, prompt.getAnnotations(), "Annotations should match set value");
        assertEquals(2.0, prompt.getAnnotations().getPriority(), "Annotations priority should match");
    }
    
    @Test
    void testMessagesHandling() {
        List<PromptMessage> messages = new ArrayList<>();
        
        PromptMessage message1 = new PromptMessage();
        message1.setRole(Role.SYSTEM);
        TextContent textContent1 = new TextContent();
        textContent1.setText("You are a helpful assistant");
        message1.setContent(textContent1);
        
        PromptMessage message2 = new PromptMessage();
        message2.setRole(Role.USER);
        TextContent textContent2 = new TextContent();
        textContent2.setText("Tell me about Java testing");
        message2.setContent(textContent2);
        
        messages.add(message1);
        messages.add(message2);
        
        prompt.setMessages(messages);
        
        assertEquals(messages, prompt.getMessages(), "Messages should match set value");
        assertEquals(2, prompt.getMessages().size(), "Should have 2 messages");
        assertEquals(Role.SYSTEM, prompt.getMessages().get(0).getRole(), "First message role should match");
        assertEquals("You are a helpful assistant", ((TextContent)prompt.getMessages().get(0).getContent()).getText(), 
            "First message content should match");
        assertEquals(Role.USER, prompt.getMessages().get(1).getRole(), "Second message role should match");
        assertEquals("Tell me about Java testing", ((TextContent)prompt.getMessages().get(1).getContent()).getText(), 
            "Second message content should match");
    }
    
    @Test
    void testAllPropertiesSet() {
        Annotations annotations = new Annotations();
        annotations.setPriority(1.5);
        
        List<PromptMessage> messages = new ArrayList<>();
        PromptMessage message = new PromptMessage();
        message.setRole(Role.USER);
        TextContent textContent = new TextContent();
        textContent.setText("Hello, world!");
        message.setContent(textContent);
        messages.add(message);
        
        prompt.setName(TEST_NAME);
        prompt.setDescription(TEST_DESCRIPTION);
        prompt.setAnnotations(annotations);
        prompt.setMessages(messages);
        
        assertEquals(TEST_NAME, prompt.getName(), "Name should match set value");
        assertEquals(TEST_DESCRIPTION, prompt.getDescription(), "Description should match set value");
        assertEquals(annotations, prompt.getAnnotations(), "Annotations should match set value");
        assertEquals(messages, prompt.getMessages(), "Messages should match set value");
    }
    
    @Test
    void testNullValues() {
        prompt.setName(null);
        prompt.setDescription(null);
        prompt.setAnnotations(null);
        prompt.setMessages(null);
        
        assertNull(prompt.getName(), "Name should be null");
        assertNull(prompt.getDescription(), "Description should be null");
        assertNull(prompt.getAnnotations(), "Annotations should be null");
        assertNull(prompt.getMessages(), "Messages should be null");
    }
    
    @Test
    void testEmptyMessages() {
        prompt.setMessages(new ArrayList<>());
        
        assertNotNull(prompt.getMessages(), "Messages should not be null");
        assertTrue(prompt.getMessages().isEmpty(), "Messages list should be empty");
    }
}