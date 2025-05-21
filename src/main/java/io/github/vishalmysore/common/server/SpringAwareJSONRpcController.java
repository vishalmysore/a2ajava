package io.github.vishalmysore.common.server;

import com.t4a.predict.PredictionLoader;
import io.github.vishalmysore.a2a.server.DyanamicTaskContoller;
import io.github.vishalmysore.mcp.server.MCPToolsController;
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
