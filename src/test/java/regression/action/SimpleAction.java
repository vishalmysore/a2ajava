package regression.action;

import com.t4a.detect.ActionCallback;
import com.t4a.detect.ActionState;


//@Agent(groupName = "food preference", groupDescription = "actions related to food preference")
public class SimpleAction  {

    /**
     * This will be used for streaming the status of the action
     * it will be populated if its part of the streaming request else not
     */
    private ActionCallback actionCallback;

  //  @Action( description = "what is the food preference of this person")
    public String whatFoodDoesThisPersonLike(String name) {
        if(actionCallback!= null) {
            actionCallback.sendtStatus("success", ActionState.WORKING);
        }
        if("vishal".equalsIgnoreCase(name)) {
            if(actionCallback!= null) {
                actionCallback.sendtStatus("Paneer butter masala", ActionState.WORKING);
            }
            return "Paneer Butter Masala";
        }
        else if ("vinod".equalsIgnoreCase(name)) {
            if(actionCallback!= null) {
                actionCallback.sendtStatus("aloo kofta", ActionState.WORKING);
            }
            return "aloo kofta";
        }else {
            if(actionCallback!= null) {
                actionCallback.sendtStatus("Raw Onions", ActionState.WORKING);
            }
            return "something yummy";
        }
    }

}
