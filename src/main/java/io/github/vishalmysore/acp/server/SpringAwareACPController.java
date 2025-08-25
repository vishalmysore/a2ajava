package io.github.vishalmysore.acp.server;

import org.springframework.context.ApplicationContext;

public class SpringAwareACPController extends ACPRestController {
    
    public SpringAwareACPController(ApplicationContext applicationContext) {
        super(applicationContext);
    }
}
