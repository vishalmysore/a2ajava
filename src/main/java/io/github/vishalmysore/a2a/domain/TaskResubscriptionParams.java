package io.github.vishalmysore.a2a.domain;

import lombok.Data;

@Data
public class TaskResubscriptionParams {
    private String subscriptionId; // Subscription ID to resubscribe
    private String taskId; // Task ID to resubscribe
}
