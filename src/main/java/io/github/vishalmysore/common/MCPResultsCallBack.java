package io.github.vishalmysore.common;

import com.t4a.detect.ActionState;

import java.util.concurrent.atomic.AtomicReference;

public class MCPResultsCallBack extends MCPActionCallback{
    private String status;
    private AtomicReference<Object> context;

    @Override
    public void setContext(AtomicReference<Object>  context) {
        this.context = context;
    }

    @Override
    public AtomicReference<Object> getContext() {
        return context;
    }

    @Override
    public void sendtStatus(String status, ActionState state) {

    }
}
