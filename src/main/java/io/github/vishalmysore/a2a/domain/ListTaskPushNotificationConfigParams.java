package io.github.vishalmysore.a2a.domain;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * Parameters for the tasks/pushNotificationConfig/list JSON-RPC method.
 */
@Data
@ToString
public class ListTaskPushNotificationConfigParams {
    /**
     * The id of the task whose push notification configurations should be listed.
     */
    private String id;

    /**
     * Request-specific metadata.
     */
    private Map<String, Object> metadata;
}
