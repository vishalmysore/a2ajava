package io.github.vishalmysore.common.server;

import org.springframework.context.ApplicationContext;

/**
 * SpringAwareJSONRpcController is a subclass of JsonRpcController that is aware of the Spring application context.
 * It allows for dependency injection and other Spring features to be used within the JSON-RPC controller.
 */
public class SpringAwareJSONRpcController extends JsonRpcController{

    public SpringAwareJSONRpcController(ApplicationContext applicationContext) {
     super(applicationContext);

    }

}
