# Complex Multi-Agent Workflow: House Buying System

*** Please note this tutorial is still work in progres, I will update it shortly ***

## Overview
This tutorial demonstrates how to build a sophisticated multi-agent system using A2AJava and Tools4AI. We'll create a house buying workflow that showcases advanced features like:
- Dynamic task processing
- Real-time agent communication
- Risk management
- Human-in-loop approvals
- Server-Sent Events (SSE) for real-time updates

## 1. Configuration Setup

First, configure your Tools4AI properties:

```properties
# tools4ai.properties
agent.provider=gemini
agent.risk.level.default=LOW
agent.human.approval.required=true
```

## 2. Task Processing Infrastructure

### Base Task Controller
```java
@Service
public class HouseBuyingTaskController extends DyanamicTaskContoller {
    
    @Autowired
    private RiskManager riskManager;
    
    @Override
    public SendTaskResponse sendTask(TaskSendParams taskSendParams, ActionCallback callback) {
        String taskId = taskSendParams.getId();
        Task task = new Task();
        task.setId(taskId);
        task.setSessionId(UUID.randomUUID().toString());
        
        // Configure real-time updates
        task.setPushNotificationConfig(new TaskPushNotificationConfig());
        
        // Process task asynchronously
        nonBlockingService.execute(() -> {
            try {
                TextPart textPart = (TextPart) taskSendParams.getMessage().getParts().get(0);
                String prompt = textPart.getText();
                
                SSEEmitterCallback sseCallback = new SSEEmitterCallback(taskId, getEmitter(taskId));
                sseCallback.setContext(task);
                
                // Process with risk awareness
                if (riskManager.requiresApproval(task, getCurrentAgent())) {
                    processHighRiskTask(task, prompt, sseCallback);
                } else {
                    getBaseProcessor().processSingleAction(prompt, sseCallback);
                }
            } catch (Exception e) {
                handleTaskError(task, e);
            }
        });
        
        return createResponse(task);
    }
}
```

## 3. Real-Time Updates Implementation

### SSE Callback Handler
```java
public class SSEEmitterCallback implements ActionCallback {
    private final String taskId;
    private final SseEmitter emitter;
    private Task context;

    @Override
    public void sendtStatus(String status, ActionState state) {
        try {
            TaskStatusUpdateEvent event = new TaskStatusUpdateEvent(
                taskId, 
                new TaskStatus(state.name(), status),
                false
            );
            emitter.send(SseEmitter.event()
                .name("status_update")
                .data(event));
        } catch (IOException e) {
            log.severe("Failed to send SSE update: " + e.getMessage());
        }
    }

    @Override
    public void setContext(Object context) {
        if (context instanceof Task) {
            this.context = (Task) context;
        }
    }
}
```

## 4. Risk Management Integration

### Risk-Aware Action Processing
```java
@Component
public class HouseBuyingRiskManager extends RiskManager {
    
    @Override
    public boolean requiresApproval(Task task, Agent agent) {
        if (task.getType() == TaskType.MAKE_OFFER) {
            Map<String, Object> params = task.getParameters();
            double offerAmount = Double.parseDouble(params.get("amount").toString());
            
            // High-risk conditions
            if (offerAmount > 1000000) {
                return true;
            }
            
            // Medium-risk conditions requiring additional checks
            if (offerAmount > 500000) {
                return validateMediumRiskOffer(params);
            }
        }
        
        return false;
    }
    
    private boolean validateMediumRiskOffer(Map<String, Object> params) {
        // Implement medium-risk validation logic
        // For example, check buyer's credit score, down payment, etc.
        return false;
    }
}
```

## 5. Human-in-Loop Integration

### Approval Workflow
```java
@Service
public class HouseOfferApprovalService {
    
    @Autowired
    private StateMachine<ApprovalStates, ApprovalEvents> stateMachine;
    
    @Autowired
    private HumanInLoop humanInLoop;
    
    public void processOffer(Task offerTask) {
        FeedbackLoop feedback = humanInLoop.allow(
            createApprovalPrompt(offerTask),
            "validateOffer",
            offerTask.getParameters()
        );
        
        feedback.setCallback(new ActionCallback() {
            @Override
            public void onComplete(ActionState state) {
                if (state == ActionState.APPROVED) {
                    stateMachine.sendEvent(ApprovalEvents.APPROVE);
                    proceedWithOffer(offerTask);
                } else {
                    stateMachine.sendEvent(ApprovalEvents.REJECT);
                    handleRejectedOffer(offerTask);
                }
            }
        });
    }
    
    private String createApprovalPrompt(Task task) {
        return String.format(
            "High-value offer requiring approval:\n" +
            "Property: %s\n" +
            "Offer Amount: $%s\n" +
            "Buyer: %s\n" +
            "Please review and approve/reject.",
            task.getParameters().get("propertyId"),
            task.getParameters().get("amount"),
            task.getParameters().get("buyerId")
        );
    }
}
```

## 6. Event Handling and Monitoring

### Event Listener Implementation
```java
@Component
public class HouseBuyingEventListener {
    
    @EventListener
    public void onStateChange(StateMachineEvent event) {
        logStateTransition(event);
        notifyRelevantParties(event);
        updateTaskStatus(event);
    }
    
    @EventListener
    public void onRiskDetected(RiskDetectedEvent event) {
        // Handle risk events
        escalateIfNeeded(event);
        updateRiskMetrics(event);
    }
    
    @Async
    public void notifyRelevantParties(StateMachineEvent event) {
        // Implement notification logic
        // For example, send emails, push notifications, etc.
    }
}
```

## 7. Complete Usage Example

```java
@RestController
@RequestMapping("/api/house-buying")
public class HouseBuyingController {
    
    @Autowired
    private HouseBuyingWorkflowService workflowService;
    
    @Autowired
    private HouseOfferApprovalService approvalService;
    
    @PostMapping("/make-offer")
    public ResponseEntity<String> makeOffer(@RequestBody OfferRequest request) {
        // Create and validate the task
        Task offerTask = createOfferTask(request);
        
        // Start the workflow
        if (workflowService.requiresHumanApproval(offerTask)) {
            approvalService.processOffer(offerTask);
            return ResponseEntity.ok("Offer submitted for approval");
        } else {
            workflowService.processOffer(offerTask);
            return ResponseEntity.ok("Offer processed automatically");
        }
    }
    
    @GetMapping("/updates/{taskId}")
    public SseEmitter subscribeToUpdates(@PathVariable String taskId) {
        return workflowService.createUpdateEmitter(taskId);
    }
}
```

## 8. MCP (Model Context Protocol) Integration

### MCP Tools Controller
```java
@Service
public class HouseBuyingMCPController extends MCPToolsController {
    
    @Override
    public CallToolResult callTool(ToolCallRequest request, ActionCallback callback) {
        // Configure AI processing based on tools4ai.properties
        AIProcessor processor = getBaseProcessor();
        
        Map<String, AIAction> predictions = PredictionLoader.getInstance().getPredictions();
        AIAction action = predictions.get(request.getName());
        
        try {
            // Process with human verification and explanation
            Object result = processor.processSingleAction(
                request.toString(),
                action,
                new LoggingHumanDecision(),
                new LogginggExplainDecision(),
                callback
            );
            
            return createToolResponse(result, callback);
        } catch (AIProcessingException e) {
            return handleAIError(e);
        }
    }
    
    private CallToolResult createToolResponse(Object result, ActionCallback callback) {
        CallToolResult callToolResult = new CallToolResult();
        List<Content> content = new ArrayList<>();
        TextContent textContent = new TextContent();
        textContent.setText(result.toString());
        textContent.setType("text");
        content.add(textContent);
        callToolResult.setContent(content);
        callback.setContext(callToolResult);
        return callToolResult;
    }
}
```

### AI Action Annotations
```java
@Agent(groupName = "house-valuation", groupDescription = "Property valuation and analysis")
public class PropertyValuationAgent {

    @Action(name = "estimatePropertyValue", 
            description = "Estimates property value based on features and market data",
            risk = ActionRisk.MEDIUM)
    public ValuationResult estimatePropertyValue(
            @ActionParameter(name = "propertyFeatures", description = "Property details including location, size, etc.") 
            PropertyFeatures features,
            @ActionParameter(name = "marketData", description = "Current market conditions and comparable sales") 
            MarketData marketData) {
        // Implementation
    }
}
```

### MCP Callback Implementation
```java
public class MCPHouseBuyingCallback implements MCPActionCallback {
    private final Task task;
    
    @Override
    public void setContext(Object obj) {
        if (obj instanceof CallToolResult) {
            updateTaskWithToolResult((CallToolResult) obj);
        }
    }
    
    private void updateTaskWithToolResult(CallToolResult result) {
        TaskStatus status = new TaskStatus(TaskState.COMPLETED);
        Message message = new Message();
        message.setParts(convertContentToParts(result.getContent()));
        status.setMessage(message);
        task.setStatus(status);
    }
}
```

## 9. AI Model Integration Examples

### Property Value Estimation
```java
@Service
public class PropertyAIService {
    
    @Autowired
    private AIProcessor aiProcessor;
    
    public ValuationEstimate getPropertyValuation(PropertyDetails details) {
        String prompt = String.format(
            "Analyze the following property details and provide a valuation estimate:\n" +
            "Location: %s\n" +
            "Square Footage: %d\n" +
            "Bedrooms: %d\n" +
            "Year Built: %d\n" +
            "Recent Renovations: %s",
            details.getLocation(),
            details.getSquareFootage(),
            details.getBedrooms(),
            details.getYearBuilt(),
            details.getRenovations()
        );
        
        try {
            AIAction action = PredictionLoader.getInstance()
                .getPredictedAction(prompt, AIPlatform.GEMINI);
            
            return (ValuationEstimate) aiProcessor.processSingleAction(
                prompt, 
                action,
                new PropertyValuationHumanVerification(),
                new ValuationExplanationProvider()
            );
        } catch (AIProcessingException e) {
            log.severe("AI Valuation failed: " + e.getMessage());
            throw new ValuationException("Failed to estimate property value", e);
        }
    }
}
```

### Market Analysis Integration
```java
@Service
public class MarketAnalysisService {
    
    @Autowired
    private AIProcessor aiProcessor;
    
    public MarketReport analyzeMarketConditions(String location) {
        String prompt = createMarketAnalysisPrompt(location);
        
        try {
            return (MarketReport) aiProcessor.processSingleAction(
                prompt,
                new MarketAnalysisCallback()
            );
        } catch (AIProcessingException e) {
            handleAnalysisError(e);
            return null;
        }
    }
    
    private String createMarketAnalysisPrompt(String location) {
        return String.format(
            "Analyze current market conditions for %s including:\n" +
            "1. Average home prices\n" +
            "2. Price trends over last 6 months\n" +
            "3. Average days on market\n" +
            "4. Inventory levels\n" +
            "5. Buyer/seller market indicators",
            location
        );
    }
}
```

This MCP integration enables:
- AI-powered property valuation
- Market analysis and predictions
- Risk assessment automation
- Decision support for agents
- Automated document analysis
- Smart contract recommendations

The combination of A2AJava's task management and Tools4AI's AI capabilities creates a powerful platform for intelligent real estate operations.

## Best Practices and Advanced Features

1. **Error Handling and Recovery**
   - Implement compensating transactions
   - Use reliable messaging patterns
   - Maintain audit logs

2. **Scalability Considerations**
   - Use asynchronous processing
   - Implement proper task queuing
   - Consider distributed state management

3. **Security Best Practices**
   - Implement proper authentication
   - Use role-based access control
   - Validate all inputs

4. **Monitoring and Maintenance**
   - Track task processing metrics
   - Monitor state transitions
   - Set up alerting for failures

## Integration Tips

1. Configure proper timeouts for long-running tasks:
```java
@Configuration
public class TaskConfig {
    @Bean
    public SseEmitter createEmitter() {
        return new SseEmitter(Long.MAX_VALUE);
    }
    
    @Bean
    public ExecutorService taskExecutor() {
        return Executors.newCachedThreadPool();
    }
}
```

2. Implement proper cleanup:
```java
@PreDestroy
public void cleanup() {
    emitters.forEach((id, emitter) -> {
        try {
            emitter.complete();
        } catch (Exception e) {
            log.warning("Error during emitter cleanup: " + e.getMessage());
        }
    });
}
```

## Testing Considerations

1. Unit Testing:
```java
@Test
public void testHighRiskOfferRequiresApproval() {
    Task task = createHighValueOfferTask();
    assertTrue(riskManager.requiresApproval(task, buyerAgent));
}
```

2. Integration Testing:
```java
@SpringBootTest
public class WorkflowIntegrationTest {
    @Test
    public void testCompleteWorkflow() {
        // Test the entire workflow
        OfferRequest request = createTestOffer();
        SendTaskResponse response = workflowService.processOffer(request);
        assertNotNull(response.getTaskId());
        // Verify state transitions
    }
}
```

## Conclusion

This implementation demonstrates how to:
- Build complex multi-agent systems
- Handle real-time updates
- Manage risks
- Integrate human approval workflows
- Scale and monitor the system

Next steps could include:
- Adding more specialized agents
- Implementing advanced analytics
- Adding blockchain integration
- Implementing machine learning for risk assessment