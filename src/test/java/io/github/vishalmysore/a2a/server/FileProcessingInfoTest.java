package io.github.vishalmysore.a2a.server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FileProcessingInfoTest {

    @Test
    public void testNoArgsConstructor() {
        // Create instance using no-args constructor
        FileProcessingInfo info = new FileProcessingInfo();
        
        // Verify default values
        assertNull(info.getTypeOfProcessingRequired());
        assertFalse(info.isParallelProcessingAllowed());
    }

    @Test
    public void testAllArgsConstructor() {
        // Create instance using all-args constructor
        FileProcessingInfo info = new FileProcessingInfo("Selenium", true);
        
        // Verify values
        assertEquals("Selenium", info.getTypeOfProcessingRequired());
        assertTrue(info.isParallelProcessingAllowed());
    }

    @Test
    public void testSetters() {
        // Create instance using no-args constructor
        FileProcessingInfo info = new FileProcessingInfo();
        
        // Use setters
        info.setTypeOfProcessingRequired("Non-Selenium");
        info.setParallelProcessingAllowed(true);
        
        // Verify values
        assertEquals("Non-Selenium", info.getTypeOfProcessingRequired());
        assertTrue(info.isParallelProcessingAllowed());
    }

    @Test
    public void testToString() {
        // Create instance
        FileProcessingInfo info = new FileProcessingInfo("FileIO", false);
        
        // Verify toString contains expected values
        String toString = info.toString();
        assertTrue(toString.contains("typeOfProcessingRequired=FileIO"));
        assertTrue(toString.contains("parallelProcessingAllowed=false"));
    }

    @Test
    public void testEquals() {
        // Create two identical instances
        FileProcessingInfo info1 = new FileProcessingInfo("Selenium", true);
        FileProcessingInfo info2 = new FileProcessingInfo("Selenium", true);
        
        // Create a different instance
        FileProcessingInfo info3 = new FileProcessingInfo("Non-Selenium", false);
        
        // Test equals
        assertEquals(info1, info2);
        assertNotEquals(info1, info3);
        assertNotEquals(null, info1);
        assertNotEquals("String", info1);
    }

    @Test
    public void testHashCode() {
        // Create two identical instances
        FileProcessingInfo info1 = new FileProcessingInfo("Selenium", true);
        FileProcessingInfo info2 = new FileProcessingInfo("Selenium", true);
        
        // Test hashCode
        assertEquals(info1.hashCode(), info2.hashCode());
    }
}