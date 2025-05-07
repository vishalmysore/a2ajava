package io.github.vishalmysore.common;

import com.t4a.detect.ActionCallback;
import com.t4a.detect.ActionState;
import io.github.vishalmysore.a2a.domain.Task;
import io.github.vishalmysore.a2a.domain.TaskState;

/**
 * A2AActionCallBack is an interface that extends ActionCallback.
 * It provides default implementations for the setType and getType methods,
 * setting the type to "A2A". The AI processor will pass this to the action class which can use
 * this to set the Task detaails .
 */
public class A2AActionCallBack implements ActionCallback {

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
    public String setType(String type) {
        return CallBackType.A2A.name();
    }



    @Override
    public  String getType() {
        return CallBackType.A2A.name();
    };

    @Override
    public void sendtStatus(String status, ActionState state) {
        ((Task) getContext()).setDetailedAndMessage(TaskState.forValue(state.getValue()), status );
    }
}
