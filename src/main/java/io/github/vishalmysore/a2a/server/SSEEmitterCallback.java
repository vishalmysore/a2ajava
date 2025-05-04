package io.github.vishalmysore.a2a.server;

import com.t4a.detect.ActionState;
import io.github.vishalmysore.a2a.domain.*;
import io.github.vishalmysore.common.A2AActionCallBack;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;


public class SSEEmitterCallback implements A2AActionCallBack {

    private SseEmitter sseEmitter;
    private Object context ;
    private String status;
    private String taskId;



    public SSEEmitterCallback(String taskId, SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
        this.taskId = taskId;

    }


    @Override
    public void setContext(Object context) {
        this.context = context;
    }

    @Override
    public Object getContext() {
        return context;
    }



    @Override
    public void sendtStatus(String status, ActionState state){
        this.status = status;
        TextPart textPart = new TextPart();
        textPart.setType("text");
        textPart.setText(status);

        Message message = new Message();
        message.setRole("agent");
        message.setParts(List.of(textPart));

        TaskState taskState = TaskState.valueOf(state.name());
        TaskStatus processingStatus = new TaskStatus(taskState);
        processingStatus.setMessage(message);
        SendTaskStreamingResponse response = new SendTaskStreamingResponse();
        response.setId(taskId);
        response.setResult((new TaskStatusUpdateEvent(taskId, processingStatus, false)));
        try {
            sseEmitter.send(SseEmitter.event().name("message").data(response));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
