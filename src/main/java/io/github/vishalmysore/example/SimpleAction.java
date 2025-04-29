package io.github.vishalmysore.example;

import com.t4a.annotations.Action;
import com.t4a.annotations.Agent;
import com.t4a.detect.ActionCallback;
import com.t4a.detect.ActionState;
import com.t4a.detect.HumanInLoop;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Agent(groupName = "food preference", groupDescription = "actions related to food preference")
public class SimpleAction  {

    private ActionCallback actionCallback;

    @Action( description = "what is the food preference of this person")
    public String whatFoodDoesThisPersonLike(String name) {
        actionCallback.sendtStatus("success", ActionState.WORKING);
        if("vishal".equalsIgnoreCase(name)) {
            actionCallback.sendtStatus("Paneer butter masala", ActionState.WORKING);
            return "Paneer Butter Masala";
        }
        else if ("vinod".equalsIgnoreCase(name)) {
            actionCallback.sendtStatus("aloo kofta", ActionState.WORKING);
            return "aloo kofta";
        }else {
            actionCallback.sendtStatus("Raw Onions", ActionState.WORKING);
            return "something yummy";
        }
    }

}
