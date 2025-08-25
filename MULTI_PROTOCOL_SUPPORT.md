# Multi-Protocol Support in a2ajava

The a2ajava framework now supports three major agent protocols, making it the most comprehensive agent integration platform available:

1. **A2A (App-to-App)** - Google's JSON-RPC based protocol
2. **MCP (Model Context Protocol)** - LLM communication protocol  
3. **ACP (Agent Connect Protocol)** - Cisco's REST-based protocol

## Overview

All three protocols work seamlessly with the same underlying agent implementations using `@Agent` and `@Action` annotations. The framework automatically handles protocol translation and routing.

## Agent Implementation

Create agents using standard annotations that work with all protocols:

```java
@Agent(groupName = "customer support", groupDescription = "actions related to customer support")
public class CustomerSupportAgent {
    
    @Action(description = "Create a support ticket for customer issues")
    public String createTicket(String customerName, String issue) {
        return "Ticket created for " + customerName + ": " + issue;
    }
    
    @Action(description = "Check the status of an existing ticket")
    public String checkTicketStatus(String ticketId) {
        return "Ticket " + ticketId + " is in progress";
    }
}
```

## A2A Protocol Usage

A2A uses JSON-RPC for communication. Here's how to interact with agents:

### Client Example
```java
// A2A JSON-RPC client
JsonRpcController controller = new JsonRpcController(applicationContext);

// Get agent card
AgentCard card = controller.getAgentCard();

// Execute action
Map<String, Object> params = Map.of(
    "customerName", "John Doe",
    "issue", "Login problem"
);
String result = controller.executeAction("createTicket", params);
```

### JSON-RPC Request
```json
{
    "jsonrpc": "2.0",
    "method": "createTicket",
    "params": {
        "customerName": "John Doe",
        "issue": "Login problem"
    },
    "id": 1
}
```

## MCP Protocol Usage

MCP is designed for LLM communication with structured tool calling:

### Client Example
```java
// MCP client
MCPToolsController mcpController = new MCPToolsController(applicationContext);

// List available tools
ListToolsResult tools = mcpController.listTools();

// Call tool
CallToolRequest request = new CallToolRequest();
request.setName("createTicket");
request.setArguments(Map.of(
    "customerName", "Jane Smith",
    "issue", "Payment issue"
));

CallToolResult result = mcpController.callTool(request);
```

### MCP Tool Call
```json
{
    "method": "tools/call",
    "params": {
        "name": "createTicket",
        "arguments": {
            "customerName": "Jane Smith",
            "issue": "Payment issue"
        }
    }
}
```

## ACP Protocol Usage

ACP uses REST endpoints for agent interaction:

### Client Example
```java
// ACP REST client
ACPRestController acpController = new ACPRestController(applicationContext);

// Search agents
AgentSearchRequest searchRequest = new AgentSearchRequest();
searchRequest.setQuery("customer support");
List<Agent> agents = acpController.searchAgents(searchRequest).getBody();

// Create stateless run
RunCreateStateless runRequest = new RunCreateStateless();
runRequest.setAgentId(agents.get(0).getAgentId());
runRequest.setInput(Map.of(
    "customerName", "Bob Wilson",
    "issue", "Account locked"
));

AgentRun run = acpController.createStatelessRun(runRequest).getBody();
```

### REST API Calls
```bash
# Search agents
curl -X POST http://localhost:8080/acp/agents/search \
  -H "Content-Type: application/json" \
  -d '{"query": "customer support", "limit": 10}'

# Get agent details
curl http://localhost:8080/acp/agents/{agent-id}

# Create stateless run
curl -X POST http://localhost:8080/acp/runs \
  -H "Content-Type: application/json" \
  -d '{
    "agent_id": "550e8400-e29b-41d4-a716-446655440000",
    "input": {
      "customerName": "Bob Wilson",
      "issue": "Account locked"
    }
  }'
```

## Stateful vs Stateless Execution

### ACP Stateful Execution with Threads
```java
// Create thread for stateful conversation
Map<String, Object> metadata = Map.of("session", "customer-session-123");
Thread thread = acpController.createThread(metadata).getBody();

// Create stateful run
RunCreateStateful statefulRequest = new RunCreateStateful();
statefulRequest.setAgentId(agentId);
statefulRequest.setThreadId(thread.getThreadId());
statefulRequest.setInput(Map.of("customerName", "Alice Brown"));

AgentRun run = acpController.createStatefulRun(
    thread.getThreadId(), 
    statefulRequest
).getBody();
```

### A2A Task-based Execution
```java
// A2A uses Task model for state management
Task task = new Task();
task.setId("task-123");
task.setSessionId("session-456");

// Execute with task context
controller.executeWithTask(task, "createTicket", params);
```

## Protocol Comparison

| Feature | A2A | MCP | ACP |
|---------|-----|-----|-----|
| Transport | JSON-RPC | JSON-RPC | REST |
| State Management | Task-based | Stateless | Thread-based |
| Agent Discovery | AgentCard | Tool listing | Agent search |
| Streaming | Yes | Yes | Yes |
| Authentication | Custom | Custom | REST standard |
| Use Case | App integration | LLM tools | Enterprise agents |

## Configuration

### Spring Boot Configuration
```java
@Configuration
public class MultiProtocolConfig {
    
    @Bean
    public JsonRpcController a2aController(ApplicationContext context) {
        return new SpringAwareJSONRpcController(context);
    }
    
    @Bean
    public MCPToolsController mcpController(ApplicationContext context) {
        return new SpringAwareMCPToolsController(context);
    }
    
    @Bean
    public ACPRestController acpController(ApplicationContext context) {
        return new SpringAwareACPController(context);
    }
}
```

### Application Properties
```properties
# Enable all protocols
a2a.enabled=true
mcp.enabled=true
acp.enabled=true

# Protocol-specific settings
acp.base-url=http://localhost:8080/acp
a2a.jsonrpc.endpoint=/jsonrpc
mcp.tools.endpoint=/mcp
```

## Testing All Protocols

The framework includes comprehensive tests for all protocols:

```java
@Test
void testMultiProtocolIntegration() {
    // Test A2A
    String a2aResult = a2aController.executeAction("createTicket", params);
    
    // Test MCP
    CallToolResult mcpResult = mcpController.callTool(mcpRequest);
    
    // Test ACP
    AgentRun acpResult = acpController.createStatelessRun(acpRequest).getBody();
    
    // All should produce equivalent results
    assertThat(a2aResult).contains("Ticket created");
    assertThat(mcpResult.getContent()).contains("Ticket created");
    assertThat(acpResult.getStatus()).isEqualTo(AgentRun.RunStatus.success);
}
```

## Best Practices

1. **Single Agent Implementation**: Write agents once using `@Agent`/`@Action` annotations
2. **Protocol Selection**: Choose protocol based on client needs:
   - A2A for app-to-app integration
   - MCP for LLM tool calling
   - ACP for enterprise REST APIs
3. **State Management**: Use appropriate state models for each protocol
4. **Error Handling**: Implement consistent error handling across protocols
5. **Testing**: Test agents with all three protocols to ensure compatibility

## Migration Guide

### From A2A-only to Multi-Protocol

1. Add ACP and MCP dependencies to `pom.xml`
2. Configure additional controllers in Spring
3. Existing `@Agent`/`@Action` code works unchanged
4. Add protocol-specific endpoints as needed

### Protocol-Specific Considerations

- **A2A**: Maintains backward compatibility
- **MCP**: Requires tool schema definitions
- **ACP**: Needs REST endpoint configuration

## Conclusion

The a2ajava framework's multi-protocol support enables seamless agent integration across different ecosystems. Whether you're building LLM tools, enterprise APIs, or app integrations, a single agent implementation works across all three major protocols.

For more examples and detailed API documentation, see the individual protocol documentation in the `/docs` directory.
