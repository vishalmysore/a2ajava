package io.github.vishalmysore.server;

import io.github.vishalmysore.domain.Task;
import io.github.vishalmysore.domain.TaskSendParams;
import io.github.vishalmysore.domain.TaskSendSubscribeParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface A2ATaskController {
    public ResponseEntity<Task> sendTask(@RequestBody TaskSendParams taskSendParams);

    public SseEmitter sendSubscribeTask(TaskSendSubscribeParams params);
}
