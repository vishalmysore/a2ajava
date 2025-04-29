package io.github.vishalmysore.server;

import com.t4a.processor.GeminiV2ActionProcessor;
import io.github.vishalmysore.domain.*;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log
@RestController
@RequestMapping("/dynamictasks")
public class DyanamicTaskContoller implements A2ATaskController {
    private final Map<String, Task> tasks = new ConcurrentHashMap<>();
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ExecutorService nonBlockingService = Executors.newCachedThreadPool();

    @Override
    public ResponseEntity<Task> sendTask(TaskSendParams taskSendParams) {
        return null;
    }



    @Override
    public SseEmitter sendSubscribeTask(TaskSendSubscribeParams params) {
        String id = params.getId(); // assuming your params object has an id field
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); //timeout
        emitters.put(id, emitter);
        nonBlockingService.execute(() -> {
            try {
                List<Part> parts = params.getMessage().getParts();
                if (parts != null && !parts.isEmpty()) {
                    Part part = parts.get(0);
                    if (part instanceof TextPart textPart && "text".equals(textPart.getType())) {
                        // Process text part
                        String text = textPart.getText();
                        // Use text for GeminiV2ActionProcessor
                        GeminiV2ActionProcessor processor = new GeminiV2ActionProcessor();
                        SSEEmitterCallback sseEmitterCallback = new SSEEmitterCallback(id,emitter);
                        processor.processSingleAction(text, sseEmitterCallback);
                    }
                }

            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        //handle client disconnects
        emitter.onCompletion(() -> {
            emitters.remove(id);
            log.info("Client disconnected for task: " + id);
        });
        emitter.onError((throwable) -> {
            emitters.remove(id);
            log.info("Error occurred for task " + id + ": " + throwable.getMessage());
        });
        emitter.onTimeout(() -> {
            emitters.remove(id);
            emitter.complete();
            log.info("Timeout occurred for task: " + id);
        });
        return emitter;

    }
}
