# A2AJava: Quick Start Guide to Agent Development

## Table of Contents
1. [Getting Started](#getting-started)
2. [Creating Your First Agent](#creating-your-first-agent)
3. [Advanced Configuration](#advanced-configuration)
4. [Protocol Details](#protocol-details)
5. [Error Handling & Best Practices](#error-handling--best-practices)
6. [Advanced Features](#advanced-features)

## Getting Started

### Installation
Add the following Maven dependency to your project:
```xml
<dependency>
    <groupId>io.github.vishalmysore</groupId>
    <artifactId>a2ajava</artifactId>
    <version>0.0.7.1</version>
</dependency>
```

### Basic Configuration
1. Create `tools4ai.properties` in your resources folder:
```properties
agent.provider=gemini  # or openai
```

## Creating Your First Agent

### 1. Basic Agent Structure
```java
@Agent(groupName = "ticket-booking", 
       groupDescription = "Handles airline ticket booking operations")
public class BookingAgent {
    @Action(description = "Book a flight ticket")
    public String bookFlight(String from, String to, String date) {
        // Your implementation here
    }
}
```

### 2. Adding Real-time Updates
```java
@Action(description = "Book a flight ticket")
public String bookFlight(String from, String to, String date) {
    actionCallback.sendtStatus("Starting booking process", ActionState.WORKING);
    try {
        // Booking logic here
        actionCallback.sendtStatus("Booking completed", ActionState.COMPLETED);
        return "Booking confirmed";
    } catch (Exception e) {
        actionCallback.sendtStatus("Booking failed: " + e.getMessage(), ActionState.ERROR);
        throw e;
    }
}
```

### 3. Handling Different Message Types
```java
@Action(description = "Process booking request")
public void processBooking(Message message) {
    for (Part part : message.getParts()) {
        switch (part) {
            case TextPart textPart -> handleTextBooking(textPart);
            case DataPart dataPart -> handleStructuredBooking(dataPart);
            case FilePart filePart -> handleFileAttachment(filePart);
        }
    }
}
```

## Advanced Configuration

### AI Model Integration

1. Configure the AI Provider:
```properties
# tools4ai.properties
agent.provider=gemini
```

2. Implement a Custom Transformer:
```java
@Override
public PromptTransformer getPromptTransformer() {
    return new GeminiV2PromptTransformer();
}
```

### Protocol Support

Your agent automatically supports both:
- A2A Protocol (Agent-to-Agent communication)
- MCP Protocol (Model Context Protocol for AI model interaction)

The framework exposes:
1. `/.well-known/agent.json` - A2A protocol endpoint
2. `/mcp/tools` - MCP protocol endpoint

## Error Handling & Best Practices

### 1. Proper Error Handling
```java
@Action(description = "Process transaction")
public TransactionResult processTransaction(Transaction tx) {
    try {
        actionCallback.sendtStatus("Validating", ActionState.WORKING);
        validateTransaction(tx);
        
        actionCallback.sendtStatus("Processing", ActionState.WORKING);
        return processValidTransaction(tx);
    } catch (Exception e) {
        actionCallback.sendtStatus("Failed: " + e.getMessage(), ActionState.ERROR);
        return new TransactionResult(false, e.getMessage());
    }
}
```

### 2. Best Practices
- Group related actions using meaningful `@Agent` groupNames
- Provide clear action descriptions
- Use appropriate parameter types
- Implement proper status updates
- Follow security guidelines
  - Use HTTPS in production
  - Implement authentication
  - Secure sensitive data

## Advanced Features

### 1. Dynamic Tool Discovery
- Automatic tool registration
- Parameter type inference
- JSON schema generation
- AI-friendly descriptions

### 2. Message Types Support
- TextPart: Plain text
- FilePart: File transfers
- DataPart: Structured data

### 3. Real-time Updates
Both protocols support status updates through:
- `ActionCallback` (A2A)
- `MCPActionCallback` (MCP)

## Security Considerations

1. Authentication Setup
```java
agentCard.setAuthentication(new Authentication(new String[]{"Bearer"}));
```

2. HTTPS Configuration
3. OAuth2 Integration (if needed)
4. Proper input validation

## Future Development Areas

1. AI Provider Integration
   - Additional AI providers
   - Enhanced configuration options
   - Custom transformations

2. Protocol Enhancements
   - Extended message types
   - Improved real-time capabilities
   - Enhanced streaming

3. Developer Experience
   - Additional tools
   - Enhanced debugging
   - Comprehensive logging