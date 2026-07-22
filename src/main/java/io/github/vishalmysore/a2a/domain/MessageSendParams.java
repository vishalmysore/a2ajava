package io.github.vishalmysore.a2a.domain;

import lombok.Data;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

/**
 * Parameters for the A2A message/send and message/stream JSON-RPC methods.
 * These methods supersede the older tasks/send and tasks/sendSubscribe methods;
 * this class can adapt itself to the legacy parameter shapes so both wire formats
 * are served by the same task controllers.
 */
@Data
@ToString
public class MessageSendParams {
    /**
     * The message being sent to the agent.
     */
    private Message message;

    /**
     * Optional configuration for the send request.
     */
    private MessageSendConfiguration configuration;

    /**
     * Request-specific metadata.
     */
    private Map<String, Object> metadata;

    /**
     * Converts these params into the legacy TaskSendParams shape so existing
     * task controllers can serve message/send without modification. The task id is
     * taken from the message's taskId (continuing an existing task) or generated,
     * and the message's contextId is carried over as the session id.
     */
    public TaskSendParams toTaskSendParams() {
        TaskSendParams params = new TaskSendParams();
        if (message != null && message.getTaskId() != null) {
            params.setId(message.getTaskId());
        } else {
            params.setId(UUID.randomUUID().toString());
        }
        if (message != null) {
            params.setSessionId(message.getContextId());
        }
        params.setMessage(message);
        params.setMetadata(metadata);
        if (configuration != null) {
            params.setHistoryLength(configuration.getHistoryLength());
            params.setPushNotification(configuration.getPushNotificationConfig());
            params.setAcceptedOutputModes(configuration.getAcceptedOutputModes());
        }
        return params;
    }

    /**
     * Converts these params into the legacy TaskSendSubscribeParams shape so existing
     * streaming task controllers can serve message/stream without modification.
     */
    public TaskSendSubscribeParams toTaskSendSubscribeParams() {
        TaskSendParams base = toTaskSendParams();
        TaskSendSubscribeParams params = new TaskSendSubscribeParams();
        params.setId(base.getId());
        params.setSessionId(base.getSessionId());
        params.setMessage(base.getMessage());
        params.setMetadata(base.getMetadata());
        params.setHistoryLength(base.getHistoryLength());
        params.setPushNotification(base.getPushNotification());
        params.setAcceptedOutputModes(base.getAcceptedOutputModes());
        return params;
    }
}
