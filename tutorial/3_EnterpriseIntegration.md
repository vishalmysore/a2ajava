# Enterprise Integration Guide

This guide explains how to integrate A2AJava with various enterprise systems through HTTP APIs and shell scripts.

## 1. HTTP Actions with Swagger/OpenAPI

A2AJava can automatically convert any REST API (defined in Swagger/OpenAPI) into actions. This means you can interact with external APIs using natural language prompts.

### Configuration

Create a `swagger_actions.json` file to define your API endpoints:

```json
{
  "endpoints": [
    {
      "swaggerurl": "https://fakerestapi.azurewebsites.net/swagger/v1/swagger.json",
      "group": "Books Author Activity",
      "description": "Actions for managing books, authors, photos and user activities",
      "baseurl": "https://fakerestapi.azurewebsites.net/",
      "id": "fakerestapi"
    },
    {
      "swaggerurl": "https://petstore3.swagger.io/api/v3/openapi.json",
      "baseurl": "https://petstore3.swagger.io/",
      "group": "Petstore API",
      "description": "Actions for managing pets and pet-related operations",
      "id": "petstore"
    },
    {
      "swaggerurl": "https://vishalmysore-instaservice.hf.space/v3/api-docs",
      "baseurl": "https://vishalmysore-instaservice.hf.space/",
      "group": "Enterprise Support and Ticketing System",
      "description": "Actions for creating and tracking enterprise support tickets",
      "id": "InstaService"
    }
  ]
}
```

### Usage Example

Here's how to use the HTTP actions in your code:

```java
@Test
public void testHttpActionOpenAI() throws AIProcessingException, IOException {
    // Initialize the processor
    OpenAiActionProcessor processor = new OpenAiActionProcessor();
    
    // Natural language prompt to create a book
    String postABook = "post a book harry poster with id 189 the publish date is " +
                      "2024-03-22 and the description is about harry who likes " +
                      "poster its around 500 pages";
    
    // Process the action
    String result = (String) processor.processSingleAction(postABook);
    
    // Verify the result
    Assertions.assertNotNull(result);
    String success = TestHelperOpenAI.getInstance().sendMessage(
        "Look at this message - " + result + " - was it a success? - Reply in true or false only"
    );
    Assertions.assertTrue("True".equalsIgnoreCase(success));
}
```

The system will automatically:
1. Parse the natural language prompt
2. Identify the appropriate API endpoint
3. Extract parameters from the text
4. Make the HTTP call with the correct method and parameters

## 2. Shell Action Integration

A2AJava can convert shell scripts into actions that can be triggered through natural language. This is particularly useful for system administration and automation tasks.

### Configuration

Create a `shell_actions.yml` file to define your shell actions:

```yaml
groups:
  - name: Employee Actions
    description: Actions for managing employee operations
    scripts:
      - scriptName: "test_script.cmd"
        actionName: saveEmployeeInformation
        parameters: employeeName,employeeLocation
        description: Saves new employee information to the system
```

### Usage Example

```java
// Initialize the action processor
OpenAiActionProcessor processor = new OpenAiActionProcessor();

// Natural language prompt
String promptText = "A new employee joined today in Toronto. Her name is Madhuri Khanna";

// Process the action - system will automatically determine which script to run
processor.processSingleAction(promptText);
```

## 3. Custom HTTP Actions

For REST APIs without Swagger documentation, you can define custom HTTP actions using a configuration file.

### Configuration

Create a `http_actions.json` file:

```json
{
  "endpoints": [
    {
      "actionName": "getUserDetails",
      "description": "Fetches user details from the corporate user inventory system",
      "url": "https://api.example.com/users/",
      "type": "GET",
      "input_object": [
        {
          "name": "userId",
          "type": "path_parameter",
          "description": "User ID"
        }
      ],
      "output_object": {
        "type": "json",
        "description": "User object"
      },
      "auth_interface": {
        "type": "Bearer Token",
        "description": "Authentication token required"
      }
    },
    {
      "actionName": "getTemperature",
      "url": "https://api.example.com/temperature",
      "description": "Retrieves real-time temperature from the weather API",
      "type": "GET",
      "input_object": [
        {
          "name": "locationId",
          "type": "query_parameter",
          "description": "Location ID"
        }
      ],
      "output_object": {
        "type": "json",
        "description": "Real-time temperature data"
      },
      "auth_interface": {
        "type": "API Key",
        "description": "API key required"
      }
    }
  ]
}
```

### Key Features

- **Automatic Parameter Extraction**: The system automatically extracts parameters from natural language prompts
- **Authentication Support**: Built-in support for various authentication methods (Bearer Token, API Key)
- **Flexible Input Types**: Supports both path and query parameters
- **Type Safety**: Validates input and output types according to the configuration

## Best Practices

1. **Group Organization**: Organize related actions into logical groups
2. **Clear Descriptions**: Provide detailed descriptions for actions and parameters
3. **Error Handling**: Include proper error responses in your API definitions
4. **Security**: Always configure appropriate authentication for production endpoints
5. **Testing**: Test your integrations with both valid and invalid prompts

## Monitoring and Debugging

For monitoring API calls and debugging issues:
- View InstaService logs: [HuggingFace Space Logs](https://huggingface.co/spaces/VishalMysore/InstaService?logs=container)
- Use the built-in test helpers for verification
- Enable debug logging for detailed operation tracing

