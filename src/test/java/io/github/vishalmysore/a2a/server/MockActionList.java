package io.github.vishalmysore.a2a.server;

import com.t4a.api.GroupInfo;
import java.util.Map;

/**
 * Mock implementation of ActionList for testing purposes
 */
public class MockActionList {
    private final Map<GroupInfo, String> groupActions;
    
    public MockActionList(Map<GroupInfo, String> groupActions) {
        this.groupActions = groupActions;
    }
    
    public Map<GroupInfo, String> getGroupActions() {
        return groupActions;
    }
}