package io.github.vishalmysore.a2a.server;

import com.t4a.detect.ActionCallback;
import com.t4a.processor.AIProcessor;
import io.github.vishalmysore.a2a.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface A2ATaskController {


    public SseEmitter sendSubscribeTask(TaskSendSubscribeParams params);

    Object setTaskPushNotification(TaskSetPushNotificationParams setPushParams);

    public ResponseEntity<Task> getTask(@RequestParam String id, @RequestParam(defaultValue = "0") int historyLength) ;

    Object cancelTask(String id);

    Object getTaskPushNotification(TaskGetPushNotificationParams getPushParams);

    Object resubscribeToTask(TaskResubscriptionParams resubParams);

    public SendTaskResponse sendTask(TaskSendParams taskSendParams, ActionCallback callback);

    public default SendTaskResponse sendTask(TaskSendParams taskSendParams) {
        return sendTask(taskSendParams, null);
    }

    public AIProcessor getBaseProcessor ();
}
