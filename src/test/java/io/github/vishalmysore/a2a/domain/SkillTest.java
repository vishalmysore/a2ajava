package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SkillTest {

    @Test
    public void testDefaultConstructor() {
        Skill skill = new Skill();
        
        assertNull(skill.getId());
        assertNull(skill.getName());
        assertNull(skill.getDescription());
        assertNull(skill.getTags());
        assertNull(skill.getExamples());
        assertNull(skill.getInputModes());
        assertNull(skill.getOutputModes());
    }
    
    @Test
    public void testSettersAndGetters() {
        Skill skill = new Skill();
        
        String id = "skill-123";
        String name = "Test Skill";
        String description = "This is a test skill";
        String[] tags = {"tag1", "tag2", "tag3"};
        String[] examples = {"example1", "example2"};
        String[] inputModes = {"text", "json"};
        String[] outputModes = {"text", "html", "markdown"};
        
        skill.setId(id);
        skill.setName(name);
        skill.setDescription(description);
        skill.setTags(tags);
        skill.setExamples(examples);
        skill.setInputModes(inputModes);
        skill.setOutputModes(outputModes);
        
        assertEquals(id, skill.getId());
        assertEquals(name, skill.getName());
        assertEquals(description, skill.getDescription());
        assertArrayEquals(tags, skill.getTags());
        assertArrayEquals(examples, skill.getExamples());
        assertArrayEquals(inputModes, skill.getInputModes());
        assertArrayEquals(outputModes, skill.getOutputModes());
    }
    
    @Test
    public void testEqualsAndHashCode() {
        Skill skill1 = new Skill();
        skill1.setId("skill-123");
        skill1.setName("Test Skill");
        
        Skill skill2 = new Skill();
        skill2.setId("skill-123");
        skill2.setName("Test Skill");
        
        Skill skill3 = new Skill();
        skill3.setId("skill-456");
        skill3.setName("Another Skill");
        
        // Test equals
        assertEquals(skill1, skill2);
        assertNotEquals(skill1, skill3);
        
        // Test hashCode
        assertEquals(skill1.hashCode(), skill2.hashCode());
        assertNotEquals(skill1.hashCode(), skill3.hashCode());
    }
    
    @Test
    public void testToString() {
        Skill skill = new Skill();
        skill.setId("skill-123");
        skill.setName("Test Skill");
        skill.setDescription("This is a test skill");
        
        String toString = skill.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("skill-123"));
        assertTrue(toString.contains("Test Skill"));
        assertTrue(toString.contains("This is a test skill"));
    }
}