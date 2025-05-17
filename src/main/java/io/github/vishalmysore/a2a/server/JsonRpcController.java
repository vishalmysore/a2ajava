package io.github.vishalmysore.a2a.server;



import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vishalmysore.a2a.domain.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
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

@Log
public class JsonRpcController implements A2ARPCController {



    /**
     * The DynamicTaskController is responsible for handling dynamic task-related operations.
     * IT can handle any task wheter ticket or food prefernce etc
     */
    private DyanamicTaskContoller dynamicTaskController = new DyanamicTaskContoller();

    public A2ATaskController getTaskController() {
        return dynamicTaskController;
    }


    /**
     * This method handles JSON-RPC requests. It is the main entry point for the JSON-RPC API.
     * Will optimize this method later
     * @param request
     * @return
     */
    public Object handleRpc(@RequestBody JsonRpcRequest request) {
        String method = request.getMethod();
        Object params = request.getParams();
        log.info(request.toString());
        preProcessing(method,params);
        Object result = null;
        switch (method) {
            case "tasks/send":
                TaskSendParams sendParams = new ObjectMapper().convertValue(params, TaskSendParams.class);
                result = getTaskController().sendTask(sendParams);
                postProcessing(method,result);
                return result;
            case "tasks/get":
                TaskQueryParams queryParams = new ObjectMapper().convertValue(params, TaskQueryParams.class);
                result = getTaskController().getTask(queryParams.getId(), queryParams.getHistoryLength());
                postProcessing(method,result);
                return result;
            case "tasks/sendSubscribe":
                TaskSendSubscribeParams sendSubscribeParams = new ObjectMapper().convertValue(params, TaskSendSubscribeParams.class);

                 result = getTaskController().sendSubscribeTask(sendSubscribeParams);
                postProcessing(method,result);
                return result;
            case "tasks/cancel":
                TaskCancelParams cancelParams = new ObjectMapper().convertValue(params, TaskCancelParams.class);
                result = getTaskController().cancelTask(cancelParams.getId());
                postProcessing(method,result);
                return result;
            case "tasks/setPushNotification":
                TaskSetPushNotificationParams setPushParams = new ObjectMapper().convertValue(params, TaskSetPushNotificationParams.class);

                result = getTaskController().setTaskPushNotification(setPushParams);
                postProcessing(method,result);
                return result;
            case "tasks/getPushNotification":
                TaskGetPushNotificationParams getPushParams = new ObjectMapper().convertValue(params, TaskGetPushNotificationParams.class);

                result = getTaskController().getTaskPushNotification(getPushParams);
                postProcessing(method,result);
                return result;
            case "tasks/resubscribe":
                TaskResubscriptionParams resubParams = new ObjectMapper().convertValue(params, TaskResubscriptionParams.class);
                result = getTaskController().resubscribeToTask(resubParams);
                postProcessing(method,result);
                return result;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Method not found: " + method);
        }
    }
}