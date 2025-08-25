package regression.action;

import com.t4a.annotations.Action;
import com.t4a.annotations.Agent;

@Agent(groupName = "acp test", groupDescription = "actions for testing ACP integration")
public class ACPTestAction {

    @Action(description = "Test action for ACP protocol integration")
    public String testACPAction(String input) {
        return "ACP Test Result: " + input;
    }

    @Action(description = "Another test action for ACP with multiple parameters")
    public String multiParamACPAction(String param1, int param2, boolean param3) {
        return String.format("ACP Multi-param result: %s, %d, %b", param1, param2, param3);
    }
}
