# A2AJava: Bridging A2A and MCP Protocols for Intelligent Agent Development

## Introduction

A2AJava uniquely combines Google's Agent-to-Agent (A2A) protocol with the Model Context Protocol (MCP) in a single, unified framework. This implementation enables developers to create intelligent agents that can both communicate with other agents and directly interact with AI models using a simple annotation-based approach.

## Core Architecture

### Dual Protocol Support

#### A2A Protocol Implementation
- **Agent Cards**: Implemented through three distinct controllers:
  - `DynamicAgentCardController`: Generates cards based on runtime annotations
  - `RealTimeAgentCardController`: Uses AI for dynamic description generation
  - `TicketAgentCardController`: Example implementation for reference

#### MCP Protocol Implementation
- **MCPToolsController**: Core component that converts @Action annotated methods into MCP tools
- Automatic parameter mapping and JSON schema generation
- Real-time status updates through callbacks

### Unified Annotation System
```java
@Agent(groupName = "ticket booking", 
       groupDescription = "actions related to ticket booking")
public class BookingAgent {
    @Action(description = "Book a flight ticket")
    public String bookFlight(String from, String to, String date) {
        // Implementation
    }
}
```

## Technical Implementation

### Configuration Management
The system uses `tools4ai.properties` for AI provider configuration:
```properties
agent.provider=gemini  # or openai
```

### Dynamic Tool Generation For MCP
The MCPToolsController automatically:

- All the methods annotated with action are converted to tools and method parameters to tool parameters
- Generates JSON validation schemas
- Creates AI-friendly parameter descriptions
- Supports both structured and natural language inputs

### Dynamic Task Generation For A2A
- All the methods annotated with action are also converted to Tasks for A2A communication 

### Agent Card Generation Process
Three levels of agent card generation:

1. **Dynamic**: Runtime-based generation from annotations
2. **Real-time**: AI-powered dynamic description generation
3. **Static**: Template-based for specific implementations

### Protocol Translation Layer

#### A2A to MCP Translation
Methods annotated with @Action become:
- A2A tasks accessible via REST endpoints
- MCP tools with parameter mapping
- Streaming-capable endpoints for real-time updates

#### Parameter Handling
```java
private String getJsonType(Class<?> type) {
    if (type == String.class) return "string";
    if (type == Integer.class || type == int.class
            || type == Double.class || type == double.class
            || type == Float.class || type == float.class) return "number";
    if (type == Boolean.class || type == boolean.class) return "boolean";
    if (type.isArray() || Collection.class.isAssignableFrom(type)) return "array";
    return "object";
}
```

### Real-time Updates
Both protocols support status updates through:

- `ActionCallback` for A2A protocol
- `MCPActionCallback` for MCP protocol

#### Status Updates
```java
@Action(description = "Process an order")
public void processOrder(String orderId) {
    actionCallback.sendtStatus("Starting order processing", ActionState.WORKING);
    try {
        // Processing logic
        actionCallback.sendtStatus("Order processed successfully", ActionState.COMPLETED);
    } catch (Exception e) {
        actionCallback.sendtStatus("Error processing order: " + e.getMessage(), ActionState.ERROR);
    }
}
```

```java
public interface ActionCallback {
    void setContext(Object obj);
    Object getContext();
    void sendtStatus(String status, ActionState state);
    String getType();
    String setType(String type);
}
```

### Error Handling
```java
@Action(description = "Process complex transaction")
public TransactionResult processTransaction(Transaction tx) {
    try {
        actionCallback.sendtStatus("Validating transaction", ActionState.WORKING);
        validateTransaction(tx);
        
        actionCallback.sendtStatus("Processing payment", ActionState.WORKING);
        processPayment(tx);
        
        actionCallback.sendtStatus("Transaction completed", ActionState.COMPLETED);
        return new TransactionResult(true, "Success");
    } catch (ValidationException ve) {
        actionCallback.sendtStatus("Validation failed: " + ve.getMessage(), ActionState.ERROR);
        return new TransactionResult(false, ve.getMessage());
    } catch (ProcessingException pe) {
        actionCallback.sendtStatus("Processing failed: " + pe.getMessage(), ActionState.ERROR);
        return new TransactionResult(false, pe.getMessage());
    }
}
```

### Message Handling Example
```java
@Action(description = "Handle multi-part message")
public void handleMessage(Message message) {
    for (Part part : message.getParts()) {
        if (part instanceof TextPart) {
            handleTextPart((TextPart) part);
        } else if (part instanceof FilePart) {
            handleFilePart((FilePart) part);
        } else if (part instanceof DataPart) {
            handleDataPart((DataPart) part);
        }
    }
}

private void handleTextPart(TextPart part) {
    actionCallback.sendtStatus("Processing text: " + part.getText(), ActionState.WORKING);
    // Text processing logic
}

private void handleFilePart(FilePart part) {
    actionCallback.sendtStatus("Processing file: " + part.getFileName(), ActionState.WORKING);
    // File processing logic
}

private void handleDataPart(DataPart part) {
    actionCallback.sendtStatus("Processing data", ActionState.WORKING);
    // Structured data processing logic
}
```

### Endpoint Exposures

The framework exposes functionality through two distinct endpoints:

1. **A2A Protocol Endpoints** (`/.well-known/`):
   - Agent Card exposure through `/.well-known/agent.json`
   - Provides agent capabilities and metadata
   - Implemented by AgentCardController classes

2. **MCP Protocol Endpoints** (`/mcp/`):
   - Tools exposure through `/mcp/tools`
   - Tool invocation through `/mcp/tools/call`
   - Automatic conversion of @Action methods to MCP tools

Example of how an @Action annotated method gets exposed:
```java
@Action(description = "Book a flight ticket")
public String bookFlight(String from, String to, String date) {
    // Implementation
}
```

This single method becomes available as:
- An A2A task through `/.well-known/agent.json` for agent discovery
- An MCP tool through `/mcp/tools` for direct AI model interaction

The MCPToolsController handles the MCP endpoint exposure:
```java
@Service
@RequestMapping("/mcp")
public class MCPToolsController {
    // Exposes tools listing
    @GetMapping("/tools")
    public ResponseEntity<Map<String, List<Tool>>> listTools() {
        // Implementation
    }

    // Handles tool invocation
    @PostMapping("/tools/call")
    public CallToolResult callTool(@RequestBody ToolCallRequest request) {
        // Implementation
    }
}
```

While AgentCardController handles A2A endpoint exposure:
```java
@Service
@RequestMapping("/.well-known")
public class AgentCardController {
    @GetMapping("/agent.json")
    public ResponseEntity<AgentCard> getAgentCard() {
        // Implementation
    }
}
```

## Advanced Features

### Dynamic Tool Discovery
The MCPToolsController provides:

- Automatic tool registration
- Parameter type inference
- JSON schema generation
- AI-friendly descriptions

### Flexible Message Handling
Supports multiple message types:

- **TextPart**: Plain text communication
- **FilePart**: File transfers
- **DataPart**: Structured data exchange

### AI Integration
- Configurable AI providers (Gemini/OpenAI)
- Automatic prompt transformation
- Natural language parameter handling

## Best Practices and Implementation Guidelines

### Agent Design
- Group related actions using meaningful `@Agent` groupNames
- Provide clear action descriptions
- Use appropriate parameter types
- Implement proper error handling

### Tool Implementation
When creating MCP tools:

- Use descriptive parameter names
- Provide comprehensive parameter descriptions
- Include examples in documentation
- Handle optional parameters appropriately

### Security Considerations
- Implement authentication for production use
- Secure sensitive data
- Use HTTPS in production
- Implement proper error handling

## Error Handling and Monitoring

### Status Updates
Both protocols support status updates through:

- `ActionCallback` for A2A protocol
- `MCPActionCallback` for MCP protocol

### Error Handling
- Comprehensive exception handling with try-catch blocks
- Detailed error messages through ActionCallback
- Structured status tracking with ActionState
- Comprehensive request/response logging

## Integration with AI Models

### AI Provider Configuration
```java
// Configuration in tools4ai.properties
agent.provider=gemini
```

### Prompt Transformation
```java
// Example of using PromptTransformer
@Override
public PromptTransformer getPromptTransformer() {
    return new GeminiV2PromptTransformer();
}
```

## Future Development

The framework is designed for extensibility in several key areas:

1. **AI Provider Integration**
   - Support for additional AI providers
   - Enhanced model configuration options
   - Custom prompt transformation strategies

2. **Validation and Security**
   - Enhanced parameter validation
   - Advanced security features
   - OAuth2 integration options

3. **Protocol Enhancements**
   - Extended message type support
   - Improved real-time capabilities
   - Enhanced streaming support

4. **Developer Experience**
   - Additional development tools
   - Enhanced debugging capabilities
   - Comprehensive logging options

## Conclusion

A2AJava provides a powerful foundation for building intelligent agents that can both communicate with other agents and interact directly with AI models. Its unique dual-protocol support and annotation-based approach make it an ideal choice for developing modern AI-powered applications.

The framework's automatic conversion of @Action methods to both A2A tasks and MCP tools, combined with its flexible configuration and robust error handling, makes it a comprehensive solution for agent development in the Java ecosystem.