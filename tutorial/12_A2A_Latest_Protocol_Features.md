# Latest A2A Protocol Features

The A2A specification has evolved since its initial release: the message-centric
`message/send` / `message/stream` methods superseded `tasks/send` / `tasks/sendSubscribe`,
push notification configurations gained full CRUD methods, agent cards can now declare
security schemes and carry JWS signatures, and tasks gained new lifecycle states.
a2ajava supports all of these while remaining fully backward compatible — clients
written against the original method names keep working against the same endpoint.

## 1. Message-centric messaging: `message/send` and `message/stream`

The newer spec sends a `Message` (with its own `messageId`) instead of wrapping
everything in a task. The server derives or continues the task automatically:

* If `message.taskId` is set, the message continues that task.
* Otherwise a new task id is generated.
* `message.contextId` groups related tasks and messages into one conversation and
  is mapped to the session id on the server.

Wire format:

```json
{
  "jsonrpc": "2.0",
  "id": "1",
  "method": "message/send",
  "params": {
    "message": {
      "role": "user",
      "messageId": "9229e770-767c-417b-a0b0-f0741243c589",
      "contextId": "ctx-1234",
      "parts": [{ "text": "tell me a joke" }]
    },
    "configuration": {
      "acceptedOutputModes": ["text"],
      "historyLength": 5,
      "blocking": true
    }
  }
}
```

From Java, both the remote and the local client expose `sendMessage`:

```java
A2ATaskClient client = new A2ATaskClient("http://localhost:8080/rpc");
SendTaskResponse response = client.sendMessage("tell me a joke");

// continue the same conversation
client.sendMessage("another one please", "ctx-1234");
```

`message/stream` behaves like `message/send` but responds over SSE, exactly like
the older `tasks/sendSubscribe`.

If you have implemented your own `A2ATaskController`, nothing changes — the
`JsonRpcController` adapts `MessageSendParams` to the familiar `TaskSendParams`
via `MessageSendParams.toTaskSendParams()` before invoking your controller.

## 2. Push notification configuration CRUD

The push notification methods are now namespaced and cover the full lifecycle:

| Method | Description |
|---|---|
| `tasks/pushNotificationConfig/set` | Create or replace the config for a task |
| `tasks/pushNotificationConfig/get` | Fetch the config |
| `tasks/pushNotificationConfig/list` | List all configs for a task |
| `tasks/pushNotificationConfig/delete` | Delete a config |

The legacy `tasks/setPushNotification` and `tasks/getPushNotification` names are
kept as aliases. `A2ATaskController` gained default implementations of
`listTaskPushNotificationConfig` and `deleteTaskPushNotificationConfig`; override
them if your controller stores multiple configurations per task.

## 3. New task states: `rejected` and `auth-required`

`TaskState` now includes:

* `rejected` — the agent declined to work on the task.
* `auth-required` — the task cannot proceed until the client completes an
  authentication step (pairs naturally with the security scheme declarations below).

```java
task.setDetailedAndMessage(TaskState.AUTH_REQUIRED,
        "Please authenticate with the bearer scheme before continuing");
```

## 4. Agent card security metadata and signatures

Agent cards can now advertise how to authenticate, OpenAPI-style:

```java
AgentCard card = new AgentCard();
card.setIconUrl("https://example.com/agent.png");

SecurityScheme bearer = new SecurityScheme();
bearer.setType("http");
bearer.setScheme("bearer");
bearer.setBearerFormat("JWT");
card.setSecuritySchemes(Map.of("bearerAuth", bearer));
card.setSecurity(List.of(Map.of("bearerAuth", List.of())));
```

Cards can also carry detached JWS signatures (RFC 7515) so clients can verify
integrity and provenance. The `protected` member of the JWS maps to
`AgentCardSignature.getProtectedHeader()` in Java:

```java
AgentCardSignature signature = new AgentCardSignature();
signature.setProtectedHeader("eyJhbGciOiJFUzI1NiJ9");
signature.setSignature("...");
card.setSignatures(List.of(signature));
```

## 5. Authenticated extended agent card

Agents that publish a minimal public card can offer a richer card to
authenticated clients. Set `supportsAuthenticatedExtendedCard` on the public
card and register the extended card with the RPC controller:

```java
AgentCard extended = new AgentCard();
extended.setName("my-agent (extended)");
// ... skills and endpoints only authenticated clients should see ...

jsonRpcController.setExtendedAgentCard(extended);
```

Clients retrieve it with the `agent/getAuthenticatedExtendedCard` method. If no
extended card is configured the server answers with error `-32007`
(`AuthenticatedExtendedCardNotConfiguredError`).

## 6. New standard error codes

| Code | Error | Meaning |
|---|---|---|
| `-32005` | `ContentTypeNotSupportedError` | Client and agent have no compatible content types |
| `-32006` | `InvalidAgentResponseError` | The agent produced a malformed response |
| `-32007` | `AuthenticatedExtendedCardNotConfiguredError` | No extended card is available |

These extend the existing A2A error family (e.g. `-32003
PushNotificationNotSupportedError`) and serialize as standard JSON-RPC error
objects.
