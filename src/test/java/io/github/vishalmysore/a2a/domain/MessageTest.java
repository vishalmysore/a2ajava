package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageTest {
    private Message message;

    @BeforeEach
    public void setUp() {
        message = new Message();
    }

    @Test
    public void testMessageCreation() {
        assertNotNull(message);
        assertNull(message.getRole());
        assertNull(message.getParts());
    }

    @Test
    public void testRoleAssignment() {
        message.setRole("user");
        assertEquals("user", message.getRole());

        message.setRole("agent");
        assertEquals("agent", message.getRole());
    }

    @Test
    public void testPartManagement() {
        List<Part> parts = new ArrayList<>();
        
        TextPart textPart = new TextPart();
        textPart.setType("text");
        textPart.setText("Hello world");
        parts.add(textPart);

        FilePart filePart = new FilePart();
        filePart.setType("file");
        FileContent fileContent = new FileContent();
        fileContent.setName("test.txt");
        fileContent.setBytes("file content");
        filePart.setFile(fileContent);
        parts.add(filePart);

        message.setParts(parts);
        
        assertEquals(2, message.getParts().size());
        assertTrue(message.getParts().get(0) instanceof TextPart);
        assertTrue(message.getParts().get(1) instanceof FilePart);
        
        TextPart retrievedTextPart = (TextPart) message.getParts().get(0);
        assertEquals("text", retrievedTextPart.getType());
        assertEquals("Hello world", retrievedTextPart.getText());
        
        FilePart retrievedFilePart = (FilePart) message.getParts().get(1);
        assertEquals("file", retrievedFilePart.getType());
        assertEquals("test.txt", retrievedFilePart.getFile().getName());
        assertEquals("file content", retrievedFilePart.getFile().getBytes());
    }

    @Test
    public void testMultipleTextParts() {
        TextPart part1 = new TextPart();
        part1.setType("text");
        part1.setText("First message");

        TextPart part2 = new TextPart();
        part2.setType("text");
        part2.setText("Second message");

        message.setParts(Arrays.asList(part1, part2));
        message.setRole("user");

        assertEquals(2, message.getParts().size());
        assertEquals("First message", ((TextPart)message.getParts().get(0)).getText());
        assertEquals("Second message", ((TextPart)message.getParts().get(1)).getText());
        assertEquals("user", message.getRole());
    }

    @Test
    public void testEmptyParts() {
        message.setParts(new ArrayList<>());
        assertNotNull(message.getParts());
        assertTrue(message.getParts().isEmpty());
    }

    @Test
    public void testPartTypeConsistency() {
        TextPart textPart = new TextPart();
        textPart.setType("text");
        textPart.setText("Test message");

        FilePart filePart = new FilePart();
        filePart.setType("file");
        FileContent fileContent = new FileContent();
        fileContent.setName("test.txt");
        filePart.setFile(fileContent);

        message.setParts(Arrays.asList(textPart, filePart));

        for (Part part : message.getParts()) {
            if (part instanceof TextPart) {
                assertEquals("text", part.getType());
            } else if (part instanceof FilePart) {
                assertEquals("file", part.getType());
            }
        }
    }



    @Test
    public void testToString() {
        message.setRole("user");
        TextPart part = new TextPart();
        part.setType("text");
        part.setText("Test message");
        message.setParts(Arrays.asList(part));

        String toString = message.toString();
        assertTrue(toString.contains("role=user"));
        assertTrue(toString.contains("parts="));
        assertTrue(toString.contains("Test message"));
    }
}
