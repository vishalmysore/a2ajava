package io.github.vishalmysore.server;

import com.t4a.detect.ActionCallback;
import io.github.vishalmysore.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface A2ATaskController {


    public SseEmitter sendSubscribeTask(TaskSendSubscribeParams params);

    Object setTaskPushNotification(TaskSetPushNotificationParams setPushParams);

    public ResponseEntity<Task> getTask(@RequestParam String id, @RequestParam(defaultValue = "0") int historyLength) ;

    Object cancelTask(String id);

    Object getTaskPushNotification(TaskGetPushNotificationParams getPushParams);

    Object resubscribeToTask(TaskResubscriptionParams resubParams);

    public  SendTaskResponse sendTask(TaskSendParams taskSendParams, ActionCallback callback);

    public default SendTaskResponse sendTask(TaskSendParams taskSendParams) {
        return sendTask(taskSendParams, null);
    }

}
