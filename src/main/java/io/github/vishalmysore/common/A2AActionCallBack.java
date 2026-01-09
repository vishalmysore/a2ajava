package io.github.vishalmysore.common;

import com.t4a.detect.ActionCallback;
import com.t4a.detect.ActionState;
import io.github.vishalmysore.a2a.domain.DataPart;
import io.github.vishalmysore.a2a.domain.Message;
import io.github.vishalmysore.a2a.domain.Task;
import io.github.vishalmysore.a2a.domain.TaskState;
import io.github.vishalmysore.a2a.domain.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    
    /**
     * Add A2UI content to the A2A Task response.
     * This allows A2A agents to return UI components to clients that support A2UI extension.
     * The A2UI content is added to the task's status message as a DataPart.
     * 
     * @param a2uiMessage A2UI message structure (surfaceUpdate, beginRendering, etc.)
     * @see <a href="https://a2ui.org/specification/v0.8-a2ui/">A2UI Specification</a>
     */
    public void addA2UIContent(Map<String, Object> a2uiMessage) {
        Task task = (Task) getContext();
        TaskStatus taskStatus = task.getStatus();
        
        // Get or create the status message
        Message message;
        if (taskStatus != null && taskStatus.getMessage() != null) {
            message = taskStatus.getMessage();
        } else {
            message = new Message();
            message.setRole("agent");
            if (taskStatus == null) {
                taskStatus = new TaskStatus();
                task.setStatus(taskStatus);
            }
            taskStatus.setMessage(message);
        }
        
        // Get or create parts list
        List parts = message.getParts();
        if (parts == null) {
            parts = new ArrayList<>();
            message.setParts(parts);
        }
        
        // Create and add A2UI DataPart
        DataPart dataPart = DataPart.createA2UIPart(a2uiMessage);
        parts.add(dataPart);
    }
}
