package regression.action;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleActionTest {

    @Test
    public void testWhatFoodDoesThisPersonLike() {
        SimpleAction action = new SimpleAction();

        // Test for Vishal
        String result = action.whatFoodDoesThisPersonLike("vishal");
        assertEquals("Paneer Butter Masala", result);

        // Test for Vinod
        result = action.whatFoodDoesThisPersonLike("vinod");
        assertEquals("aloo kofta", result);

        // Test for unknown name
        result = action.whatFoodDoesThisPersonLike("unknown");
        assertEquals("something yummy", result);
    }
}