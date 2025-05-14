package io.github.vishalmysore.a2a.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TaskResubscriptionParams {
    private String subscriptionId; // Subscription ID to resubscribe
    private String taskId; // Task ID to resubscribe
}
