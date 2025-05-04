package io.github.vishalmysore.common;

import com.t4a.detect.ActionState;

public class MCPResultsCallBack implements MCPActionCallback{
    private String status;
    private Object context;

    @Override
    public void setContext(Object obj) {
        this.context = obj;
    }

    @Override
    public Object getContext() {
        return context;
    }

    @Override
    public void sendtStatus(String status, ActionState state) {

    }
}
