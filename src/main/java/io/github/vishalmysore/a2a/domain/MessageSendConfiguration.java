package io.github.vishalmysore.a2a.domain;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Configuration options for a message/send or message/stream request.
 */
@Data
@ToString
public class MessageSendConfiguration {
    /**
     * Output MIME types the client is prepared to accept in the response.
     */
    private List<String> acceptedOutputModes;

    /**
     * Number of most recent messages from the task history to include in the response.
     */
    private int historyLength;

    /**
     * Push notification configuration the agent should use to notify the client of updates.
     */
    private TaskPushNotificationConfig pushNotificationConfig;

    /**
     * If true, the client waits for the task to reach a terminal state before the response is returned.
     */
    private boolean blocking;
}
