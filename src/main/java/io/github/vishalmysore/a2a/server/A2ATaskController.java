package io.github.vishalmysore.a2a.server;

import com.t4a.detect.ActionCallback;
import com.t4a.processor.AIProcessor;
import io.github.vishalmysore.a2a.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.util.List;

public interface A2ATaskController {


    public SseEmitter sendSubscribeTask(TaskSendSubscribeParams params);

    Object setTaskPushNotification(TaskSetPushNotificationParams setPushParams);

    public ResponseEntity<Task> getTask(@RequestParam String id, @RequestParam(defaultValue = "0") int historyLength) ;

    Object cancelTask(String id);

    Object getTaskPushNotification(TaskGetPushNotificationParams getPushParams);

    /**
     * Lists the push notification configurations for a task
     * (A2A tasks/pushNotificationConfig/list). The default implementation wraps the
     * single configuration returned by getTaskPushNotification in a list.
     */
    default Object listTaskPushNotificationConfig(ListTaskPushNotificationConfigParams listParams) {
        TaskGetPushNotificationParams getParams = new TaskGetPushNotificationParams();
        getParams.setTaskId(listParams.getId());
        Object config = getTaskPushNotification(getParams);
        List<Object> configs = new java.util.ArrayList<>();
        if (config != null) {
            configs.add(config);
        }
        return configs;
    }

    /**
     * Deletes a push notification configuration for a task
     * (A2A tasks/pushNotificationConfig/delete). The default implementation clears the
     * configuration by setting it to null; controllers with real storage should override.
     */
    default Object deleteTaskPushNotificationConfig(DeleteTaskPushNotificationConfigParams deleteParams) {
        TaskSetPushNotificationParams setParams = new TaskSetPushNotificationParams();
        setParams.setTaskId(deleteParams.getId());
        return setTaskPushNotification(setParams);
    }

    Object resubscribeToTask(TaskResubscriptionParams resubParams);

    default SendTaskResponse sendTask(TaskSendParams taskSendParams, ActionCallback callback) {
        return sendTask(taskSendParams, callback, true);
    }

    public SendTaskResponse sendTask(TaskSendParams taskSendParams, ActionCallback callback,boolean isAsync);

    public default SendTaskResponse sendTask(TaskSendParams taskSendParams) {
        return sendTask(taskSendParams, null);
    }

    public AIProcessor getBaseProcessor ();

    default List<Task> getTasks() {
        throw new UnsupportedOperationException();
    }


}
