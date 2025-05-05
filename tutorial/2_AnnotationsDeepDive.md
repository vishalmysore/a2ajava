# Annotations Deep Dive

## Introduction

A2AJava's annotation system provides a powerful way to convert natural language into structured data and actions. This guide explains how to use annotations effectively in your applications.

## Table of Contents
1. [Prompt to POJO Conversion](#1-prompt-to-pojo-conversion)
2. [Action System](#2-action-system)
3. [Data Annotations](#3-data-annotations)
4. [Special Features](#4-special-features)
5. [Security Features](#5-security-features)
6. [Troubleshooting](#6-troubleshooting)

## 1. Prompt to POJO Conversion

### Overview
A2AJava can automatically convert natural language text into Java objects. This feature uses AI to extract structured data from unstructured text.

### Basic Example
```java
OpenAIPromptTransformer transformer = new OpenAIPromptTransformer();

// Example prompt with employee information
String promptText = "Shahrukh Khan works for MovieHits inc and his salary is $100. " +
    "He joined Toronto on Labor day, his tasks are acting and dancing. " +
    "He also works out of Montreal and Bombay. " +
    "Krithik roshan is another employee based in Chennai, his tasks are jumping and Gym, " +
    "he joined on Indian Independence Day";

// Convert to Organization object
Organization org = (Organization) transformer.transformIntoPojo(promptText, Organization.class);

// The system automatically extracts:
// - Employee names and their locations
// - Salary information
// - Join dates
// - Tasks/responsibilities
// - Multiple office locations
```

### How It Works
1. The AI analyzes the text to identify relevant information
2. Maps identified data to POJO fields based on context
3. Handles data type conversion automatically
4. Creates object instances with populated data

## 2. Action System

### Overview
Actions are the core way to handle operations in A2AJava. They can be triggered explicitly or automatically based on natural language input.

### Explicit Actions

```java
@Agent(groupName = "diary", groupDescription = "Personal diary management")
public class DiaryAction implements JavaMethodAction {
    @Action(description = "Record appointments and meetings")
    public DiaryEntry recordEntry(String entry) {
        // Implementation
        return new DiaryEntry(entry);
    }
}

// Usage
OpenAiActionProcessor processor = new OpenAiActionProcessor();
DiaryAction action = new DiaryAction();
String prompt = "I have a dentist appointment on July 3rd at 2 PM, " +
                "and a meeting with the team on July 5th at 10 AM";
DiaryEntry result = (DiaryEntry) processor.processSingleAction(prompt, action);
```

### Automatic Action Selection
```java
// The system automatically chooses the appropriate action
String prompt = "Schedule a team meeting for tomorrow at 3 PM";
Object result = processor.processSingleAction(prompt);
```

## 3. Data Annotations

### List Handling
```java
public class Organization {
    // Automatically maps employee list from text
    @ListType(Employee.class)
    private List<Employee> employees;
    
    // Handles simple string lists (e.g., office locations)
    @ListType(String.class)
    private List<String> locations;
}
```

### Map Handling
```java
public class SportSchedule {
    // Maps key-value pairs from text
    @MapKeyType(DayOfWeek.class)
    @MapValueType(String.class)
    private Map<DayOfWeek, String> schedule;
}

// Can be populated from text like:
// "Monday is for swimming, Wednesday for gym, Friday for yoga"
```

## 4. Special Features

### Multi-Language Support
```java
public class TranslationRequest {
    @Prompt(describe = "translate to Hindi")
    private String hindi;
    
    @Prompt(describe = "translate to Tamil")
    private String tamil;
    
    @Prompt(describe = "translate to Punjabi")
    private String punjabi;
}

// Usage
String text = "Hello, how are you?";
TranslationRequest translations = transformer.transformIntoPojo(text, TranslationRequest.class);
// Automatically translates to all specified languages
```

### Smart Date Handling
```java
public class Event {
    @Prompt(
        dateFormat = "yyyy-MM-dd",
        describe = "Extract event date, use today if not specified"
    )
    private Date eventDate;
}
```

### Field Control
```java
public class UserProfile {
    // Field will be excluded from AI processing
    @Prompt(ignore = true)
    private String sensitiveData;
    
    // Custom processing instructions
    @Prompt(describe = "Extract user's full name, capitalize each word")
    private String fullName;
}
```

## 5. Security Features

### High-Risk Actions
Actions that could affect system stability require explicit invocation:

```java
@Agent(
    actionName = "serverRestart",
    description = "Restart the production server",
    riskLevel = ActionRisk.HIGH,
    groupName = "System Administration"
)
public class ServerAction implements JavaMethodAction {
    @Action
    public String restartServer(String reason, String approver) {
        // Implementation with safety checks
        return String.format("Server restarted by %s: %s", approver, reason);
    }
}
```

### Safe vs Unsafe Invocation
❌ Unsafe (will be blocked):
```java
processor.processSingleAction("Please restart the server");
```

✅ Safe (explicit invocation required):
```java
ServerAction action = new ServerAction();
processor.processSingleAction(
    "Restart server due to memory leak, approved by John Doe",
    action
);
```

## 6. Troubleshooting

### Common Issues and Solutions

1. **Incorrect Data Extraction**
   - Make sure prompts are clear and well-structured
   - Use the `describe` attribute to guide extraction
   - Check field types match expected data

2. **Action Selection Issues**
   - Verify action descriptions are clear
   - Group related actions appropriately
   - Use explicit action invocation for certainty

3. **Translation Problems**
   - Ensure language codes are correct
   - Check for proper encoding support
   - Verify API keys for translation services

### Debugging Tips

1. Enable debug logging:
```java
System.setProperty("tools4ai.debug", "true");
```

2. Use the test helper:
```java
TestHelper.validateExtraction(prompt, expectedData);
```

3. Monitor AI processing:
```java
processor.setVerboseMode(true);
```

## Best Practices

1. **Clear Annotations**
   - Use descriptive group names
   - Provide detailed action descriptions
   - Document parameter requirements

2. **Data Safety**
   - Mark sensitive fields with `@Prompt(ignore = true)`
   - Use HIGH risk level for dangerous operations
   - Implement proper validation

3. **Performance**
   - Group related actions together
   - Use appropriate data types
   - Cache frequently used transformations