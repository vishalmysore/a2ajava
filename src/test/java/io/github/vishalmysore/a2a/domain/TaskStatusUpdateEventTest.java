package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class TaskStatusUpdateEventTest {

    @Test
    public void testDefaultConstructor() {
        TaskStatusUpdateEvent event = new TaskStatusUpdateEvent();
        
        assertNull(event.getId());
        assertNull(event.getStatus());
        assertFalse(event.isFinalValue());
        assertNull(event.getMetadata());
    }
    
    @Test
    public void testParameterizedConstructor() {
        String id = "task-123";
        TaskStatus status = new TaskStatus();
        status.setState(TaskState.WORKING);
        
        TaskStatusUpdateEvent event = new TaskStatusUpdateEvent(id, status, true);
        
        assertEquals(id, event.getId());
        assertEquals(status, event.getStatus());
        assertTrue(event.isFinalValue());
    }
    
    @Test
    public void testSettersAndGetters() {
        TaskStatusUpdateEvent event = new TaskStatusUpdateEvent();
        
        String id = "task-456";
        TaskStatus status = new TaskStatus();
        status.setState(TaskState.COMPLETED);
        boolean finalValue = true;
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key1", "value1");
        metadata.put("key2", 123);
        
        event.setId(id);
        event.setStatus(status);
        event.setFinalValue(finalValue);
        event.setMetadata(metadata);
        
        assertEquals(id, event.getId());
        assertEquals(status, event.getStatus());
        assertEquals(finalValue, event.isFinalValue());
        assertEquals(metadata, event.getMetadata());
    }
    
    @Test
    public void testEqualsAndHashCode() {
        String id = "task-789";
        TaskStatus status = new TaskStatus();
        status.setState(TaskState.SUBMITTED);
        
        TaskStatusUpdateEvent event1 = new TaskStatusUpdateEvent(id, status, false);
        TaskStatusUpdateEvent event2 = new TaskStatusUpdateEvent(id, status, false);
        TaskStatusUpdateEvent event3 = new TaskStatusUpdateEvent("different-id", status, false);
        
        // Test equals
        assertEquals(event1, event2);
        assertNotEquals(event1, event3);
        
        // Test hashCode
        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1.hashCode(), event3.hashCode());
    }
    
    @Test
    public void testToString() {
        String id = "task-abc";
        TaskStatus status = new TaskStatus();
        status.setState(TaskState.CANCELED);
        
        TaskStatusUpdateEvent event = new TaskStatusUpdateEvent(id, status, true);
        
        String toString = event.toString();
        assertNotNull(toString);
        assertTrue(toString.contains(id));
        assertTrue(toString.contains(TaskState.CANCELED.name()));
    }
}