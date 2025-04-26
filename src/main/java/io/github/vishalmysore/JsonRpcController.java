package io.github.vishalmysore;



import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/rpc")
@Log
class JsonRpcController {

    @Autowired
    private TaskController taskController;
    @PostMapping
    public ResponseEntity<Object> handleRpc(@RequestBody JsonRpcRequest request) {
        String method = request.getMethod();
        Object params = request.getParams();
        log.info(request.toString());
        switch (method) {
            case "tasks/send":
                TaskSendParams sendParams = new ObjectMapper().convertValue(params, TaskSendParams.class);
                return ResponseEntity.ok(taskController.sendTask(sendParams));
            case "tasks/get":
                TaskQueryParams queryParams = new ObjectMapper().convertValue(params, TaskQueryParams.class);
                return ResponseEntity.ok(taskController.getTask(queryParams.getId(), queryParams.getHistoryLength()));
            // similarly for other methods...
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Method not found");
        }
    }
}
