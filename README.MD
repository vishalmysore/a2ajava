# Java Implementation of Google's A2A Protocol: Connecting the Agentverse

This project provides a Java implementation for both an A2A (Agent-to-Agent) server and client. 
**A2A is an open protocol developed by Google** to standardize how AI agents communicate and exchange information, fostering a vibrant ecosystem of interoperable AI. This api also supports building MCP Servers in Java with use of simple annotations. Imagine a world where diverse AI agents, built with different tools and by different creators, can seamlessly collaborate to solve complex problems - that's the vision A2A is bringing to life. This implementation demonstrates how to set up this communication in Java, using the Spring Framework, with a focus on sending and retrieving tasks, ( I have added a simple example agent for booking and managing airline tickets.)  



## Quick Start  

Use these example to quickly build agents using this library  
* [Database Agent](https://github.com/vishalmysore/SqlAIAgent) - Use A2A to build a database agent using Derby DB  
* [Spring A2A Agent](https://github.com/vishalmysore/SpringActions) - Use A2A to build a spring agent using A2A and MCP  
* [Standalone Java](https://github.com/vishalmysore/a2a-examples) - Several Examples on how to Use A2A to build a standalone java agent using A2A and MCP    
* [Log Monitoring Agent](https://github.com/vishalmysore/agenticlog) - Use A2A to build a log monitoring agent which triggers a2a task based on the log messages.
* [A2A Kafka Agent](https://github.com/vishalmysore/a2akafkaagent) - Use A2A to build a kafka agent which can trigger a2a task based on the kafka messages.
* [A2A Selenium Web Agent](https://github.com/vishalmysore/a2awebagent) - Use A2A to build a selenium agent which can trigger a2a task based for browsing , validation of web pages.

## Tutorials
* [FAQ](tutorial/0_FAQ.md)
* [Introduction](tutorial/1_introduction.md)
* [Annotations Deep Dive](tutorial/2_AnnotationsDeepDive.md)
* [Enterprise Integration](tutorial/3_EnterpriseIntegration.md)
* [Image Processing](tutorial/4_ImageProcessing.md)
* [Selenium Integration with A2A and MCP](tutorial/5_UISeleniumI.md)
* [Advanced Features with Spring](tutorial/6_SpringAndSelenium.md)
* [Handling Risk in A2A and MCP](tutorial/7_HandlingRisksinAgents.md)
* [Human In Loop](tutorial/8_HumanInLoop.md)
* [Kubernetes Example](tutorial/9_Kubernetes.md)
* [Multi Agents](tutorial/10_ComplexAgents.md)

## Whats so special about A2AJava library?

You can simple annotate your classes with @Agent and @Action and build a server.  The library will take care of the rest.  You can also use this library to build a client to send and receive messages from the server.  The library is built on top of Spring Boot and uses Jackson for JSON serialization/deserialization.  The library is designed to be easy to use and extend, so you can build your own agents quickly and easily.
ALl methods annotated with @Action are exposed as A2A tasks and also MCP tools you dont need to do anything . 
Infuse AI in any running application  

## Maven Dependency
you can include this library in your project by following dependency in your `pom.xml` file:

```
<dependency>
    <groupId>io.github.vishalmysore</groupId>
    <artifactId>a2ajava</artifactId>
    <version>0.0.6.1</version>
</dependency>
```
Get the latest version from [here](https://repo1.maven.org/maven2/io/github/vishalmysore/a2ajava) 

## Articles
* [A2A Protocol and Java](https://medium.com/p/54deb16302c0)  
* [Building A2A Agents in Java](https://medium.com/p/e61bfcec28ea)  
* [MCP vs A2A](https://medium.com/p/fef8d1d6d3ca)  
* [Build Database Agent](https://medium.com/@visrow/google-a2a-protocol-building-a-database-agent-d5287a387b41)

## A2A Protocol: A Universal Agent Language

The **A2A protocol**, spearheaded by Google, is designed to be the universal language for AI agents.  It moves us away from a closed way of  agent communication methods and towards a future where agents can:

* **Discover** each other's capabilities.
* **Securely** exchange information.
* **Coordinate** actions to achieve common goals.

Key components of the A2A protocol include:

* **Agents:** The autonomous entities that communicate.
* **Messages:** The containers for information exchanged between agents.
* **Parts:** The building blocks of a message (text, files, structured data).
* **Tasks:** The units of work that agents perform for each other.
* **Agent Card:** A cornerstone of A2A, think of it as an agent's digital business card.

### The Agent Card: An Agent's Identity

The **Agent Card** is a JSON-formatted file that an agent publishes to advertise its capabilities.  It's typically located at a well-known URL (`/.well-known/agent.json`) and provides essential information for other agents to discover and interact with it.  An Agent Card typically includes:

* **Agent Name and Description:** Human-readable information about the agent.
* **Endpoint URL:** The address where the agent can be reached for A2A communication.
* **Version:** The A2A protocol version the agent supports.
* **Capabilities:** The features the agent supports (e.g., streaming, push notifications).
* **Authentication:** The security mechanisms the agent requires.
* **Skills:** A detailed description of the specific functions the agent can perform.

With Agent Cards, agents can dynamically discover each other and understand how to communicate, enabling flexible and extensible multi-agent systems.

In our case we have exposed it as a rest end point

## Features

This Java implementation provides the foundation for building A2A-compliant agents:

* **A2A Server:**
    * Receives and processes tasks from client agents, acting as a hub for agent collaboration.
    * Manages the lifecycle of tasks, tracking their state and history.
    * Serializes task data into JSON for standardized communication.
    * Exposes A2A endpoints using the Spring MVC framework. As rest calls as well as one endpoint for JSONRPC communicaiton 
    * 
* **A2A Client:**
    * Sends tasks to A2A servers, initiating agent interactions.
    * Retrieves task information, allowing agents to monitor progress and obtain results.
    * Communicates with A2A servers using Spring's `RestTemplate`. With JSONRPC protocol, there is only one endpoint to send and receive messages.
* **JSON Handling:** Uses Jackson, a  Java library, for seamless JSON serialization/deserialization.
* **Task Management:** Supports the core A2A task operations: sending tasks and retrieving their information, including historical data.
* **Message Parts:** Handles `TextPart`, `FilePart`, and `DataPart`, providing flexibility in message content to support various data formats.

## Code Structure

The project is organized to clearly separate the server and client components:

* **`io.github.vishalmysosre.A2aApplication`:** The server-side implementation. ( I will be refactoring this in future )
* **`io.github.vishalmysosre.client.TaskClient`:** The client-side implementation. ( I might build a full client in angular or react in future)

### Server (`io.github.vishalmysosre.A2aApplication`)

* **`TaskController.java`:**
    * Defines the REST endpoints that make the A2A server accessible.
    * Handles incoming task requests and retrieves task data.
    * Leverages Spring MVC annotations (`@RestController`, `@PostMapping`, `@GetMapping`) for streamlined web development.
* **`TaskService.java` (Conceptual):**
    * *(Conceptual)* This class represents the heart of the server's business logic.  It would manage the intricacies of task processing, storage, and interaction with other services. While the provided code might not have a full implementation, this is where the core server-side logic resides.
* **Server Data Model:**
    * The `com.example.a2a.server` package contains classes that define the structure of A2A data: `Task`, `TaskStatus`, `Message`, `Part`, `TextPart`, `FilePart`, `DataPart`, `FileInfo`, and `Artifact`.  These classes are annotated with Jackson annotations to ensure smooth conversion to and from JSON.

### Client (`io.github.vishalmysosre.client`)

* **`TaskClient.java`:**
    * Provides methods to interact with an A2A server (`sendTask`, `getTask`), simplifying the process of sending requests.
    * Uses Spring's `RestTemplate` to handle the underlying HTTP communication.
    * Includes a `main` method to demonstrate how to use the client to communicate with a server.
* **Client Data Model:**
    * The client reuses the data model classes from the server (`Task`, `TaskStatus`, `Message`, `Part`, `TextPart`, `FilePart`, `DataPart`, `FileInfo`, `Artifact`, `TaskSendParams`, `TaskPushNotificationConfig`, and `Authentication`) to maintain consistency with the server's data structures.

## Dependencies

This project relies on the following Java technologies:

* Java 8 or later
* Spring Boot Web (`org.springframework.boot:spring-boot-starter-web`): Provides the foundation for building web applications with Spring, including Spring MVC and `RestTemplate`.
* Jackson Databind (`com.fasterxml.jackson.core:jackson-databind`): A powerful library for handling JSON serialization and deserialization.

## Setup

Setting up the server and client involves the following steps:

### Server Setup

1.  **Create a Spring Boot project:** Use Spring Initializr (https://start.spring.io/) or your favorite IDE to create a new Spring Boot project.  Include the "Web" dependency to enable web functionality.
2.  **Implement Server Components:**
    * Create the `TaskController.java` class to define the server's A2A endpoints.
    * *(Conceptual)* Implement the `TaskService.java` class to handle the server's core business logic.
    * Define the data model classes in the `com.example.a2a.server` package, ensuring they are properly annotated with Jackson annotations (e.g., `@JsonTypeInfo`, `@JsonSubTypes`, `@JsonProperty`) for JSON processing.
3.  **Configure Application Properties:** Configure the server's port and other settings in `application.properties` or `application.yml`.
4.  **Run the Server:** Start the Spring Boot application to launch the A2A server.

### Client Setup

1.  **Create a Spring Boot project:** You can create a separate Spring Boot project for the client or include the client code in the same project as the server.  Include the "Web" dependency.
2.  **Implement `TaskClient.java`:** Create the `TaskClient.java` class to handle communication with the A2A server.
3.  **Configure `BASE_URL`:** In `TaskClient.java`, set the `BASE_URL` constant to the address of your running A2A server (e.g., `"http://localhost:8080"`).
4.  **Run the Client:** Execute the `main` method in `TaskClient.java` to see the client in action, sending requests to the server.

## Implementation Details

Here's a closer look at the key implementation aspects:

### Server Implementation

* **REST Endpoints:**
    * `POST /tasks/send`:  This endpoint receives a task from a client agent.  The request body should contain a JSON representation of the `TaskSendParams` object.
    * `GET /tasks/get`:  This endpoint retrieves a task by its ID.  The `id` and `historyLength` parameters are passed as query parameters in the URL.
    * `POST /tasks/sendSubscribe`:  This endpoint is used for streaming tasks.  It allows the client to subscribe to task updates via Server-Sent Events (SSE).
    * `POST /tasks/pushNotification/set`:  This endpoint sets up push notifications for a task.  The request body should contain a JSON representation of the `TaskPushNotificationConfig` object.
    * `GET /tasks/pushNotification/get`:  This endpoint retrieves the current push notification configuration for a task.  
Its important to note that the server is designed to handle both JSON-RPC and RESTful requests.  The `TaskController` class is responsible for routing incoming requests to the appropriate methods based on the request type.    
The client will be calling the JSONRPC endpoint, which is a single endpoint that handles all the requests.  The server will be able to handle both JSON-RPC and RESTful requests.  
    * 
* **JSON Processing:**  The data model classes in `com.example.a2a.server` are annotated with Jackson annotations to automate the conversion between Java objects and JSON.  Pay special attention to `@JsonTypeInfo` and `@JsonSubTypes`, which are crucial for correctly handling the `Part` interface and its various implementations.
* **Error Handling:**  I have tried to add basic error handling which will be enhanced further.  Spring Boot provides powerful mechanisms for handling exceptions and generating appropriate HTTP status codes and error responses, ensuring a smooth and informative experience for client agents.

### Client Implementation

* **`RestTemplate`:** The `RestTemplate` simplifies the process of making HTTP requests to the A2A server.  It handles the complexities of sending requests and receiving responses.
* **JSON Processing:** The `ObjectMapper` plays a key role in the client, converting Java objects to JSON before sending them to the server, and converting the JSON responses from the server back into Java objects.
* **Error Handling:** The client includes basic error checking, printing error messages to the console.  In a production environment, you would implement more sophisticated error handling, potentially including retry mechanisms and custom exception types.

## Important Considerations For Agent Development



* **Data Model Harmony:** The data model classes (`Task`, `Message`, `Part`, etc.) must be defined consistently on both the server and the client. 

* **Error Handling is Essential:** Implement robust error handling on both the server and the client.  This will help us gracefully handle network issues, invalid data, and server-side errors, leading to more reliable and resilient agents.
* **Security First:** (This is just a POC) In a real-world application, security is paramount.  We should Implement authentication and authorization to protect your A2A server from unauthorized access.  Spring Security is a powerful tool for securing Spring Boot applications.
* **A2A Protocol Adherence:** We should Strive for strict compliance with the A2A protocol specification as its continuousely evoloving.  This will ensure interoperability with other A2A-compliant agents and future-proof your implementation.

## Important Disclaimer(s)

* This Java implementation provides a starting point for building A2A-compliant and MCP-Compliant agents and is being actively developed as and when i get time.   
* The client-side implementation  may require enhancements for error handling, security, and other production considerations.  The provided code is not a complete solution but rather a foundation upon which to build your A2A-compliant agents.  
* The A2A protocol is an evolving standard, and this implementation may need to be updated as the protocol matures.  Always refer to the official A2A documentation for the latest specifications and best practices.  
* This implementation is not affiliated with or endorsed by Google.  It is my independent effort to demonstrate the A2A protocol in Java.  
  
  
  

