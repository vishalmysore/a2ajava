package io.github.vishalmysore.common.server;

import io.github.vishalmysore.a2a.domain.*;
import io.github.vishalmysore.a2a.server.A2ATaskController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Verifies that the JSON-RPC dispatcher routes the newer A2A protocol methods
 * (message/send, message/stream, tasks/pushNotificationConfig/*,
 * agent/getAuthenticatedExtendedCard) to the task controller correctly.
 */
public class JsonRpcControllerProtocolTest {

    private A2ATaskController mockTaskController;
    private JsonRpcController controller;

    @BeforeEach
    void setUp() {
        mockTaskController = Mockito.mock(A2ATaskController.class);
        controller = new JsonRpcController() {
            @Override
            public io.github.vishalmysore.a2a.server.A2ATaskController getTaskController() {
                return mockTaskController;
            }
        };
    }

    private JsonRpcRequest request(String method, Object params) {
        return new JsonRpcRequest("2.0", method, params, UUID.randomUUID().toString());
    }

    @Test
    void testMessageSendRoutesToSendTask() {
        SendTaskResponse expected = new SendTaskResponse();
        when(mockTaskController.sendTask(any(TaskSendParams.class), any(), eq(false))).thenReturn(expected);

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("messageId", "m-1");
        message.put("contextId", "ctx-1");
        message.put("parts", Collections.singletonList(Collections.singletonMap("text", "hello")));
        Map<String, Object> params = Collections.singletonMap("message", message);

        Object result = controller.handleRpc(request("message/send", params));

        assertSame(expected, result);
        ArgumentCaptor<TaskSendParams> captor = ArgumentCaptor.forClass(TaskSendParams.class);
        verify(mockTaskController).sendTask(captor.capture(), any(), eq(false));
        assertEquals("ctx-1", captor.getValue().getSessionId());
        assertNotNull(captor.getValue().getId());
    }

    @Test
    void testMessageStreamRoutesToSendSubscribeTask() {
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("messageId", "m-2");
        message.put("taskId", "task-2");
        message.put("parts", Collections.singletonList(Collections.singletonMap("text", "stream this")));
        Map<String, Object> params = Collections.singletonMap("message", message);

        controller.handleRpc(request("message/stream", params));

        ArgumentCaptor<TaskSendSubscribeParams> captor = ArgumentCaptor.forClass(TaskSendSubscribeParams.class);
        verify(mockTaskController).sendSubscribeTask(captor.capture());
        assertEquals("task-2", captor.getValue().getId());
    }

    @Test
    void testPushNotificationConfigAliases() {
        Map<String, Object> setParams = Collections.singletonMap("taskId", "t-1");
        controller.handleRpc(request("tasks/pushNotificationConfig/set", setParams));
        verify(mockTaskController).setTaskPushNotification(any(TaskSetPushNotificationParams.class));

        Map<String, Object> getParams = Collections.singletonMap("taskId", "t-1");
        controller.handleRpc(request("tasks/pushNotificationConfig/get", getParams));
        verify(mockTaskController).getTaskPushNotification(any(TaskGetPushNotificationParams.class));

        Map<String, Object> listParams = Collections.singletonMap("id", "t-1");
        controller.handleRpc(request("tasks/pushNotificationConfig/list", listParams));
        verify(mockTaskController).listTaskPushNotificationConfig(any(ListTaskPushNotificationConfigParams.class));

        Map<String, Object> deleteParams = Collections.singletonMap("id", "t-1");
        controller.handleRpc(request("tasks/pushNotificationConfig/delete", deleteParams));
        verify(mockTaskController).deleteTaskPushNotificationConfig(any(DeleteTaskPushNotificationConfigParams.class));
    }

    @Test
    void testLegacyPushNotificationMethodsStillRoute() {
        controller.handleRpc(request("tasks/setPushNotification", Collections.singletonMap("taskId", "t-2")));
        verify(mockTaskController).setTaskPushNotification(any(TaskSetPushNotificationParams.class));

        controller.handleRpc(request("tasks/getPushNotification", Collections.singletonMap("taskId", "t-2")));
        verify(mockTaskController).getTaskPushNotification(any(TaskGetPushNotificationParams.class));
    }

    @Test
    void testAuthenticatedExtendedCardNotConfigured() {
        Object result = controller.handleRpc(request("agent/getAuthenticatedExtendedCard", Collections.emptyMap()));
        assertTrue(result instanceof AuthenticatedExtendedCardNotConfiguredError);
        assertEquals(-32007, ((AuthenticatedExtendedCardNotConfiguredError) result).getCode());
    }

    @Test
    void testAuthenticatedExtendedCardReturned() {
        AgentCard extended = new AgentCard();
        extended.setName("extended");
        extended.setSupportsAuthenticatedExtendedCard(true);
        controller.setExtendedAgentCard(extended);

        Object result = controller.handleRpc(request("agent/getAuthenticatedExtendedCard", Collections.emptyMap()));
        assertSame(extended, result);
    }
}
