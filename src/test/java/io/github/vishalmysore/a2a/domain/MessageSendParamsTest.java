package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageSendParamsTest {

    private Message buildMessage() {
        TextPart textPart = new TextPart();
        textPart.setText("hello agent");
        Message message = new Message();
        message.setRole("user");
        message.setMessageId("msg-1");
        message.setParts(Collections.singletonList(textPart));
        return message;
    }

    @Test
    void testToTaskSendParamsGeneratesIdWhenNoTaskId() {
        MessageSendParams params = new MessageSendParams();
        params.setMessage(buildMessage());

        TaskSendParams taskParams = params.toTaskSendParams();

        assertNotNull(taskParams.getId());
        assertFalse(taskParams.getId().isEmpty());
        assertSame(params.getMessage(), taskParams.getMessage());
    }

    @Test
    void testToTaskSendParamsUsesTaskIdAndContextId() {
        Message message = buildMessage();
        message.setTaskId("task-42");
        message.setContextId("ctx-7");
        MessageSendParams params = new MessageSendParams();
        params.setMessage(message);

        TaskSendParams taskParams = params.toTaskSendParams();

        assertEquals("task-42", taskParams.getId());
        assertEquals("ctx-7", taskParams.getSessionId());
    }

    @Test
    void testToTaskSendParamsMapsConfiguration() {
        MessageSendParams params = new MessageSendParams();
        params.setMessage(buildMessage());

        TaskPushNotificationConfig pushConfig = new TaskPushNotificationConfig();
        MessageSendConfiguration configuration = new MessageSendConfiguration();
        configuration.setHistoryLength(5);
        configuration.setAcceptedOutputModes(Arrays.asList("text", "file"));
        configuration.setPushNotificationConfig(pushConfig);
        params.setConfiguration(configuration);

        TaskSendParams taskParams = params.toTaskSendParams();

        assertEquals(5, taskParams.getHistoryLength());
        assertEquals(Arrays.asList("text", "file"), taskParams.getAcceptedOutputModes());
        assertSame(pushConfig, taskParams.getPushNotification());
    }

    @Test
    void testToTaskSendSubscribeParams() {
        Message message = buildMessage();
        message.setTaskId("task-42");
        message.setContextId("ctx-7");
        MessageSendParams params = new MessageSendParams();
        params.setMessage(message);
        MessageSendConfiguration configuration = new MessageSendConfiguration();
        configuration.setHistoryLength(3);
        params.setConfiguration(configuration);

        TaskSendSubscribeParams subscribeParams = params.toTaskSendSubscribeParams();

        assertEquals("task-42", subscribeParams.getId());
        assertEquals("ctx-7", subscribeParams.getSessionId());
        assertEquals(3, subscribeParams.getHistoryLength());
        assertSame(message, subscribeParams.getMessage());
    }

    @Test
    void testDeserializationFromWireFormat() throws Exception {
        String json = "{\"message\":{\"role\":\"user\",\"messageId\":\"m1\",\"contextId\":\"c1\","
                + "\"parts\":[{\"text\":\"hi\"}]},"
                + "\"configuration\":{\"historyLength\":2,\"blocking\":true}}";

        MessageSendParams params = new ObjectMapper().readValue(json, MessageSendParams.class);

        assertEquals("m1", params.getMessage().getMessageId());
        assertEquals("c1", params.getMessage().getContextId());
        assertTrue(params.getConfiguration().isBlocking());
        assertEquals(2, params.getConfiguration().getHistoryLength());
        List<Part> parts = params.getMessage().getParts();
        assertEquals(1, parts.size());
    }
}
