package io.github.vishalmysore.a2a.server;


import org.springframework.context.ApplicationContext;

/**
 * SpringAwareAgentCardController is a controller that handles agent card-related operations.
 * It extends the RealTimeAgentCardController and is aware of the Spring application context.
 */
public class SpringAwareAgentCardController extends RealTimeAgentCardController{
    public SpringAwareAgentCardController(ApplicationContext context) {
        super(context);
    }

}
