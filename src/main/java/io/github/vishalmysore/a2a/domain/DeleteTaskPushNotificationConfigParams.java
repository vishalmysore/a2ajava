package io.github.vishalmysore.a2a.domain;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * Parameters for the tasks/pushNotificationConfig/delete JSON-RPC method.
 */
@Data
@ToString
public class DeleteTaskPushNotificationConfigParams {
    /**
     * The id of the task whose push notification configuration should be deleted.
     */
    private String id;

    /**
     * The id of the push notification configuration to delete.
     */
    private String pushNotificationConfigId;

    /**
     * Request-specific metadata.
     */
    private Map<String, Object> metadata;
}
