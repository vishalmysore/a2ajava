package io.github.vishalmysore.server;



import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vishalmysore.domain.JsonRpcRequest;
import io.github.vishalmysore.domain.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * JsonRpcController handles JSON-RPC requests and routes them to the appropriate methods in the TaskController.
 * This is the main entry point for the JSON-RPC API for Google A2A clients
 */
@RestController
@RequestMapping("/")
@Log
class JsonRpcController {

    /**
     * The TaskController is responsible for handling ticket-related operations.
     */
    @Autowired
    private TicketTaskController taskController;

    /**
     * The DynamicTaskController is responsible for handling dynamic task-related operations.
     * IT can handle any task wheter ticket or food prefernce etc
     */
    @Autowired
    private DyanamicTaskContoller dynamicTaskController;

    @PostMapping
    public Object handleRpc(@RequestBody JsonRpcRequest request) {
        String method = request.getMethod();
        Object params = request.getParams();
        log.info(request.toString());

        switch (method) {
            case "tasks/send":
                TaskSendParams sendParams = new ObjectMapper().convertValue(params, TaskSendParams.class);
                return dynamicTaskController.sendTask(sendParams);
            case "tasks/get":
                TaskQueryParams queryParams = new ObjectMapper().convertValue(params, TaskQueryParams.class);
                return taskController.getTask(queryParams.getId(), queryParams.getHistoryLength());
            case "tasks/sendSubscribe":
                TaskSendSubscribeParams sendSubscribeParams = new ObjectMapper().convertValue(params, TaskSendSubscribeParams.class);

                return dynamicTaskController.sendSubscribeTask(sendSubscribeParams);
            case "tasks/cancel":
                TaskCancelParams cancelParams = new ObjectMapper().convertValue(params, TaskCancelParams.class);
                return taskController.cancelTask(cancelParams.getId());
            case "tasks/setPushNotification":
                TaskSetPushNotificationParams setPushParams = new ObjectMapper().convertValue(params, TaskSetPushNotificationParams.class);
                return taskController.setTaskPushNotification(setPushParams);
            case "tasks/getPushNotification":
                TaskGetPushNotificationParams getPushParams = new ObjectMapper().convertValue(params, TaskGetPushNotificationParams.class);
                return taskController.getTaskPushNotification(getPushParams);
            case "tasks/resubscribe":
                TaskResubscriptionParams resubParams = new ObjectMapper().convertValue(params, TaskResubscriptionParams.class);
                return taskController.resubscribeToTask(resubParams);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Method not found: " + method);
        }
    }
}