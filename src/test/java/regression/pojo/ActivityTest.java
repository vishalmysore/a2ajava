package regression.pojo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ActivityTest {

    @Test
    public void testActivityGettersAndSetters() {
        Activity activity = new Activity();
        activity.setDayOfTheWeek("Monday");
        activity.setActivityName("Yoga");

        assertEquals("Monday", activity.getDayOfTheWeek());
        assertEquals("Yoga", activity.getActivityName());
    }

    @Test
    public void testToString() {
        Activity activity = new Activity();
        activity.setDayOfTheWeek("Monday");
        activity.setActivityName("Yoga");

        String expected = "Activity(dayOfTheWeek=Monday, activityName=Yoga)";
        assertEquals(expected, activity.toString());
    }
}