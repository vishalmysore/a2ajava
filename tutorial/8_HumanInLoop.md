# Human In Loop Integration

## Overview

A2AJava uses Tools4AI which provides a simple but efficient mechanism for human validation. Any high-risk actions or actions marked for human validation are sent via this callback.

This API can be extended to perform validation actions using:
- Spring state machine
- Any workflow library

The tasks can be stored in DB or file system in pending state with task ID and then picked up when the human validation is done.

## Key Interfaces

### HumanInLoop Interface

This interface provides a mechanism for allowing human involvement in a feedback loop process.

```java
package com.t4a.detect;

import java.util.Map;

/**
 * The {@code HumanInLoop} interface represents a mechanism for allowing human involvement
 * in a feedback loop process.
 * <p>
 * This interface defines a method {@link #allow(String, String, Map)} that can be used
 * to request human input for a given prompt text and method name with optional parameters.
 * </p>
 */
public interface HumanInLoop {
    public FeedbackLoop allow(String promptText, String methodName, Map<String, Object> params);
    public FeedbackLoop allow(String promptText, String methodName, String params);

    default void setCallback(ActionCallback callback) {
    }
    
    default ActionCallback getCallback() {
        return null;
    }
}
```

### ExplainDecision Interface

This interface allows AI to explain its decision-making process.

```java
package com.t4a.detect;

/**
 * The {@code ExplainDecision} interface represents a mechanism for AI to explain decisions
 * regarding a particular prompt text, method name, and reason. AI will call this back
 * <p>
 * This interface defines a method {@link #explain(String, String, String)} that can be used
 * to provide an explanation by AI to a human regarding a decision made based on a prompt text,
 * method name, and reason.
 * </p>
 */
public interface ExplainDecision {
    public String explain(String promptText, String methodName, String reason);
}
```

## Spring State Machine Integration Example

Below is an example of how to integrate Spring State Machine with the Human-in-Loop functionality to manage approval workflows:

### 1. Define States and Events

```java
public enum ApprovalStates {
    PENDING,
    UNDER_REVIEW,
    APPROVED,
    REJECTED
}

public enum ApprovalEvents {
    SUBMIT,
    START_REVIEW,
    APPROVE,
    REJECT
}
```

### 2. Configure State Machine

```java
@Configuration
@EnableStateMachine
public class ApprovalStateMachineConfig extends StateMachineConfigurerAdapter<ApprovalStates, ApprovalEvents> {

    @Override
    public void configure(StateMachineStateConfigurer<ApprovalStates, ApprovalEvents> states) throws Exception {
        states
            .withStates()
            .initial(ApprovalStates.PENDING)
            .states(EnumSet.allOf(ApprovalStates.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<ApprovalStates, ApprovalEvents> transitions) throws Exception {
        transitions
            .withExternal()
                .source(ApprovalStates.PENDING)
                .target(ApprovalStates.UNDER_REVIEW)
                .event(ApprovalEvents.START_REVIEW)
                .and()
            .withExternal()
                .source(ApprovalStates.UNDER_REVIEW)
                .target(ApprovalStates.APPROVED)
                .event(ApprovalEvents.APPROVE)
                .and()
            .withExternal()
                .source(ApprovalStates.UNDER_REVIEW)
                .target(ApprovalStates.REJECTED)
                .event(ApprovalEvents.REJECT);
    }
}
```

### 3. Integrate with HumanInLoop

```java
@Service
@WithStateMachine
public class ApprovalWorkflowService implements HumanInLoop {
    
    @Autowired
    private StateMachine<ApprovalStates, ApprovalEvents> stateMachine;
    
    private ActionCallback callback;
    
    @Override
    public FeedbackLoop allow(String promptText, String methodName, Map<String, Object> params) {
        // Start the approval process
        stateMachine.sendEvent(ApprovalEvents.START_REVIEW);
        
        // Create feedback loop with state machine integration
        return new FeedbackLoop() {
            @Override
            public void onApprove() {
                stateMachine.sendEvent(ApprovalEvents.APPROVE);
                if (callback != null) {
                    callback.onComplete(ActionState.APPROVED);
                }
            }
            
            @Override
            public void onReject() {
                stateMachine.sendEvent(ApprovalEvents.REJECT);
                if (callback != null) {
                    callback.onComplete(ActionState.REJECTED);
                }
            }
        };
    }
    
    @Override
    public void setCallback(ActionCallback callback) {
        this.callback = callback;
    }
}
```

### 4. Using the State Machine Workflow

```java
@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    @Autowired
    private ApprovalWorkflowService approvalService;
    
    @PostMapping("/submit")
    public ResponseEntity<String> submitForApproval(@RequestBody Map<String, Object> request) {
        FeedbackLoop feedback = approvalService.allow(
            "High-risk operation requiring approval",
            "performAction",
            request
        );
        
        // The state machine will track the approval workflow
        return ResponseEntity.ok("Request submitted for approval");
    }
}
```

This integration provides several benefits:
- Clear separation of states and transitions
- Automatic state management
- Integration with existing HumanInLoop interface
- Scalable workflow management
- Audit trail of state transitions

You can extend this further by:
- Adding persistence for state machine
- Implementing event listeners for state transitions
- Adding timeout handling
- Integrating with notification systems
