package io.github.vishalmysore.a2a.server;

import io.github.vishalmysore.a2a.domain.JsonRpcRequest;
import org.springframework.web.bind.annotation.RequestBody;

public interface A2ARPCController {
    default void preProcessing(String method, Object params) {};
    default void postProcessing(String method, Object params) {};
    public Object handleRpc(JsonRpcRequest request);
    public A2ATaskController getTaskController();
}
