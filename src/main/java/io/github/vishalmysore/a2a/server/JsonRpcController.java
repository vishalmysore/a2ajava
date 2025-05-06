package io.github.vishalmysore.a2a.server;



import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vishalmysore.a2a.domain.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

/**
 * JsonRpcController handles JSON-RPC requests and routes them to the appropriate methods in the TaskController.
 * This is the main entry point for the JSON-RPC API for Google A2A clients
 * You need to look at the sample to see how it works
 */
@Service
@Log
public class JsonRpcController {



    /**
     * The DynamicTaskController is responsible for handling dynamic task-related operations.
     * IT can handle any task wheter ticket or food prefernce etc
     */
    @Autowired
    @Qualifier(TaskControllerQualifiers.DYNAMIC_TASK_CONTROLLER)
    private DyanamicTaskContoller dynamicTaskController;

    protected A2ATaskController getTaskController() {
        return dynamicTaskController;
    }


    public Object handleRpc(@RequestBody JsonRpcRequest request) {
        String method = request.getMethod();
        Object params = request.getParams();
        log.info(request.toString());

        switch (method) {
            case "tasks/send":
                TaskSendParams sendParams = new ObjectMapper().convertValue(params, TaskSendParams.class);
                return getTaskController().sendTask(sendParams);
            case "tasks/get":
                TaskQueryParams queryParams = new ObjectMapper().convertValue(params, TaskQueryParams.class);
                return getTaskController().getTask(queryParams.getId(), queryParams.getHistoryLength());
            case "tasks/sendSubscribe":
                TaskSendSubscribeParams sendSubscribeParams = new ObjectMapper().convertValue(params, TaskSendSubscribeParams.class);

                return getTaskController().sendSubscribeTask(sendSubscribeParams);
            case "tasks/cancel":
                TaskCancelParams cancelParams = new ObjectMapper().convertValue(params, TaskCancelParams.class);
                return getTaskController().cancelTask(cancelParams.getId());
            case "tasks/setPushNotification":
                TaskSetPushNotificationParams setPushParams = new ObjectMapper().convertValue(params, TaskSetPushNotificationParams.class);
                return getTaskController().setTaskPushNotification(setPushParams);
            case "tasks/getPushNotification":
                TaskGetPushNotificationParams getPushParams = new ObjectMapper().convertValue(params, TaskGetPushNotificationParams.class);
                return getTaskController().getTaskPushNotification(getPushParams);
            case "tasks/resubscribe":
                TaskResubscriptionParams resubParams = new ObjectMapper().convertValue(params, TaskResubscriptionParams.class);
                return getTaskController().resubscribeToTask(resubParams);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Method not found: " + method);
        }
    }
}