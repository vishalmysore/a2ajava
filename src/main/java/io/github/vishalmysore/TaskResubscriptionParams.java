package io.github.vishalmysore;

import lombok.Data;

@Data
public class TaskResubscriptionParams {
    private String subscriptionId; // Subscription ID to resubscribe
    private String taskId; // Task ID to resubscribe
}
