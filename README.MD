# Java Implementation of Google's A2A Protocol: Connecting the Agentverse

This project provides a Java implementation for both an A2A (Agent-to-Agent) server and client. 
**A2A is an open protocol developed by Google** to standardize how AI agents communicate and exchange information, fostering a vibrant ecosystem of interoperable AI. This api also supports building MCP Servers in Java with use of simple annotations. Imagine a world where diverse AI agents, built with different tools and by different creators, can seamlessly collaborate to solve complex problems - that's the vision A2A is bringing to life. This implementation demonstrates how to set up this communication in Java, using the Spring Framework, with a focus on sending and retrieving tasks.
a2ajava is a Swiss Army knife for building agentic applications. It is multi-protocol — works seamlessly with both A2A (Agent-to-Agent) and MCP (Model Context Protocol). It is multi-language — supports Java, Kotlin, and Groovy. It is multi-platform — compatible with Gemini, OpenAI, Claude, and Grok. It is multi-client — includes A2A and MCP clients with connectors in Java, Node, and Python. It offers multi-integration — out-of-the-box support for Selenium, human-in-the-loop workflows, and multi-LLM voting for consensus-based decision making. Agents built using the A2A protocol with a2ajava run seamlessly on MCP as well, ensuring maximum interoperability across platforms.

[![Need More Info? Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/vishalmysore/a2ajava) [![codecov](https://codecov.io/gh/vishalmysore/a2ajava/graph/badge.svg?token=HieisRv0xC)](https://codecov.io/gh/vishalmysore/a2ajava)
<a target="_blank" href="https://sonarcloud.io/summary/new_code?id=vishalmysore_a2ajava"><img src="https://sonarcloud.io/api/project_badges/measure?project=vishalmysore_a2ajava&metric=alert_status"/></a>
<a target="_blank" href="https://sonarcloud.io/summary/new_code?id=vishalmysore_a2ajava"><img src="https://sonarcloud.io/api/project_badges/measure?project=vishalmysore_a2ajava&metric=bugs"/></a>
<a target="_blank" href="https://sonarcloud.io/summary/new_code?id=vishalmysore_a2ajava"><img src="https://sonarcloud.io/api/project_badges/measure?project=vishalmysore_a2ajava&metric=vulnerabilities"/></a>
<a target="_blank" href="https://sonarcloud.io/summary/new_code?id=vishalmysore_a2ajava"><img src="https://sonarcloud.io/api/project_badges/measure?project=vishalmysore_a2ajava&metric=sqale_rating"/></a>
<a target="_blank" href="https://sonarcloud.io/summary/new_code?id=vishalmysore_a2ajava"><img src="https://sonarcloud.io/api/project_badges/measure?project=vishalmysore_a2ajava&metric=security_rating"/></a>
<a target="_blank" href="https://sonarcloud.io/summary/new_code?id=vishalmysore_a2ajava"><img src="https://sonarcloud.io/api/project_badges/measure?project=vishalmysore_a2ajava&metric=reliability_rating"/></a>
<a target="_blank" href="https://github.com/vishalmysore/a2ajava/actions/workflows/maven.yml"><img src="https://github.com/vishalmysore/a2ajava/actions/workflows/maven.yml/badge.svg"/></a>
<a target="_blank" href="https://github.com/vishalmysore/a2ajava/actions/workflows/codecov.yml"> <img src="https://github.com/vishalmysore/a2ajava/actions/workflows/codecov.yml/badge.svg"/></a>
<img alt="Test Number" src="https://img.shields.io/endpoint?url=https://gist.githubusercontent.com/vishalmysore/6c825c69028a4f8fac9d7c1707628733/raw/test.json"/>
[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
[![Java Version](https://img.shields.io/badge/Java-23-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen.svg)](https://spring.io/projects/spring-boot)

## Quick Start  

Use these example to quickly build agents using this library , forking and cloning these examples is the best way to get started with a2ajava. 

* [Spring A2A Agent](https://github.com/vishalmysore/SpringActions) - Use a2ajava to build a spring agent using A2A and MCP, can work with a2a client or with mcp client like claude desktop or connect to visual studio code as agent.
* [Connect MCP Agent in Java](https://github.com/vishalmysore/mcp-connector/) - MCP Connector for Java Based agents , connect your a2a server to MCP client like claude or connect your MCP server to a2a client. Or vice versa or any permutation and combination
* [Database Agent](https://github.com/vishalmysore/SqlAIAgent) - Use a2ajava to build a database agent using Derby DB, start stop database , insert data , create table all with agentic operation for both mcp as well as a2a      
* [Standalone Java](https://github.com/vishalmysore/a2a-examples) - Several Examples on how to Use a2ajava to build a standalone java agent using A2A and MCP work with any client     
* [Log Monitoring Agent](https://github.com/vishalmysore/agenticlog) - Use a2ajava to build a log monitoring agent which triggers a2a task based on the log messages. Works with both a2a and mcp clients dual protocol 
* [A2A Kafka Agent](https://github.com/vishalmysore/a2akafkaagent) - Use a2ajava to build a kafka agent which can trigger a2a task or mcp tool based on the kafka messages.
* [A2A Selenium Web Agent](https://github.com/vishalmysore/a2awebagent) - Use a2ajava to build a selenium agent which can trigger a2a task or mcp tool for web browsing , validation of web pages.
* [A2A Grafana Agent](https://github.com/vishalmysore/a2a-grafana) - Build observable Agents using A2A or MCP and Grafana
* [A2A MongoDB RAG Agent](https://github.com/vishalmysore/a2a-mongo-rag-search) – Perform semantic search and reasoning across agent tasks using RAG with MongoDB Agent. 
* [Simple Agent in Kotlin](https://github.com/vishalmysore/a2a-kotlin) - Use A2AJava to build a simple agent in Kotlin.
* [Build Agent in MCP](tutorial/mcp/11_MCP.md) - Use A2AJava to build a simple agent in MCP. 
* [MCP Agent in Kotlin](https://github.com/vishalmysore/mcp-kotlin) - Build MCP Agent in Kotlin and connect to Claude or A2A Clients  
* [MCP and A2A RAG Server](https://github.com/vishalmysore/mcp-rag-server) - State of the art RAG server using A2A and MCP with MongoDB Atlas works with both MCP as well as A2A clients
* [A2A and MCP Agent with Security](https://github.com/vishalmysore/a2a-mcp-with-security) - Build A2A and MCP agent with security using Spring Security and JWT. 
* [A2A and MCP in Groovy](https://github.com/vishalmysore/a2aGroovy) - Build A2A and MCP agent in Groovy using A2AJava library.
* [A2A and MCP in Scala](https://github.com/vishalmysore/a2aScala) - Build A2A and MCP agent in Scala using A2AJava library.
* [A2A and MCP with Playwright](https://github.com/vishalmysore/a2aPlaywright) - Build A2A and MCP agent with Playwright for web automation using A2AJava library.

### Coming Soon
* **A2A Sensor Agent** – Use A2A to stream data from physical sensors and trigger real-time actions. ( [Please look at ChotuRobo Series](https://www.linkedin.com/posts/vishalrow_github-modelcontextprotocolservers-model-activity-7318222463857766400-YuFV/))
* **A2A Home Automation Agent** – Use A2A to control smart home devices via MQTT or Home Assistant integrations. ( [Please look at ChotuRobo Series](https://www.linkedin.com/posts/vishalrow_and-here-is-a-demo-of-%F0%9D%90%82%F0%9D%90%A1%F0%9D%90%A8%F0%9D%90%AD%F0%9D%90%AE%F0%9D%90%91%F0%9D%90%A8%F0%9D%90%9B%F0%9D%90%A8-activity-7319065900031098880-SV4p/))
* **A2A MyScale Agent** – Use A2A to run scalable, SQL-based vector searches on tasks and documents with MyScale.

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

## Live Demo 
Live demos have been deployed on hugginface you are welcome to try them out. Or clone the space and create your own space

* [A2A MCP RAG Application with Tool calling](https://vishalmysore-a2amcpmongo.hf.space/) - A2A Java Agent with Spring Boot and MongoDB Atlas for semantic search and reasoning. This application demonstrates how to use A2A and MCP protocols to build a RAG application with tool calling capabilities. It uses MongoDB Atlas for vector search and reasoning across agent tasks.
* [A2A and MCP RBAC Security](https://vishalmysore-a2amcpdemo.hf.space/) - A2A and MCP demo with RBAC security. This application demonstrates how to use A2A and MCP protocols to build a secure application with role-based access control. It uses Spring Security and JWT for authentication and authorization.
* [A2A and MCP with Selenium](https://vishalmysore-a2amcpselenium.hf.space/) - A2A and MCP demo with Selenium integration. This application demonstrates how to use A2A and MCP protocols to build a web automation application with Selenium. It uses Spring Boot and Selenium for web automation and A2A and MCP protocols for communication.
* [A2A and MCP with Playwright](https://vishalmysore-a2apw.hf.space/) - A2A and MCP demo with Playwright integration. This application demonstrates how to use A2A and MCP protocols to build a web automation application with Playwright. It uses Spring Boot and Playwright for web automation and A2A and MCP protocols for communication.
* [A2A and MCP with Spring](https://huggingface.co/spaces/VishalMysore/a2amcpspring) - A2A and MCP demo with Spring integration. This application demonstrates how to use A2A and MCP protocols to build a web application with Spring Boot. It uses Spring Boot and A2A and MCP protocols for communication.
* [A2A and MCP with Database](https://huggingface.co/spaces/VishalMysore/a2amcpdatabase) - A2A and MCP demo with Database integration. This application demonstrates how to use A2A and MCP protocols to build a database application with Spring Boot and Derby DB. It uses Spring Boot and A2A and MCP protocols for communication.

## Whats so special about A2AJava library?

You can simple annotate your classes with @Agent and @Action and build a server.  The library will take care of the rest.  You can also use this library to build a client to send and receive messages from the server.  The library is built on top of Spring Boot and uses Jackson for JSON serialization/deserialization.  The library is designed to be easy to use and extend, so you can build your own agents quickly and easily.
ALl methods annotated with @Action are exposed as A2A tasks and also MCP tools you dont need to do anything . 
Infuse AI in any running application  

You can convert you entire springboot based application into a2a and mcp compliant agent by using these  4 annotations:

```java

1 @EnableAgent  - converts your springboot application into an A2A agent
2 @EnabaleAgentSecurity- adds security features to your agent
3 @Agent(groupName = "", groupDescription = "") - creates an agent group
4 @Action(description = "") - creates an action within the agent group

```


## Maven Dependency
you can include this library in your project by following dependency in your `pom.xml` file:

```
<dependency>
    <groupId>io.github.vishalmysore</groupId>
    <artifactId>a2ajava</artifactId>
    <version>0.1.8.2</version>
</dependency>
```

and for annotations you can use the following dependency:

```json
<dependency>
    <groupId>io.github.vishalmysore</groupId>
    <artifactId>tools4ai-annotations</artifactId>
    <version>0.0.2</version>
</dependency>
```


optionally for security you can use the following dependency:

```json
<dependency>
<groupId>io.github.vishalmysore</groupId>
<artifactId>tools4ai-security</artifactId>
<version>0.0.3</version>
</dependency>
```

Get the latest version from [here](https://repo1.maven.org/maven2/io/github/vishalmysore/a2ajava) 

## Articles
* [A2A Protocol and Java](https://medium.com/p/54deb16302c0)  
* [Building A2A Agents in Java](https://medium.com/p/e61bfcec28ea)  
* [MCP vs A2A](https://medium.com/p/fef8d1d6d3ca)  
* [Build Database Agent](https://medium.com/@visrow/google-a2a-protocol-building-a-database-agent-d5287a387b41)

## A2A Protocol: A Universal Agent Language + MCP Protocol USB-C port for AI applications

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

This Java implementation provides the foundation for building A2A and MCP compliant agents:

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

* ** MCP Server:**
    * Exposes MCP endpoints using the Spring MVC framework.
    * Supports MCP tools and tasks, allowing agents to interact with MCP clients.
    * Provides a unified interface for both A2A and MCP clients, enabling seamless communication across protocols.


## Usage

Fastest way to use to create the A2A amd MCP server is by using the annonvation 

``` 
@SpringBootApplication
@EnableAgent
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

This will expose all the services annotated with `@Action` as A2A tasks and MCP tools. You can also use the `@Agent` annotation to create an agent and register it with the A2A server.

```java
@Service
@Agent(groupName ="whatThisPersonFavFood", groupDescription = "Provide persons name and then find out what does that person like")
@Slf4j
public class SimpleService {

    /**
     * Each action has access to AIProcessor and ActionCallback which are autowired by tools4ai
     */
    private ActionCallback callback;

    /**
     * Each action has access to AIProcessor and ActionCallback which are autowired by tools4ai
     */
    private AIProcessor processor;
    public SimpleService(){
      log.info(" Created Simple Service");
    }

    @Action(description = "Get the favourite food of a person")
    public String whatThisPersonFavFood(String name) {
        if("vishal".equalsIgnoreCase(name))
        return "Paneer Butter Masala";
        else if ("vinod".equalsIgnoreCase(name)) {
            return "aloo kofta";
        }else
            return "something yummy";
    }

}
```

Thats it , yeah its that simple!!!

Now this agent will be avaiable to both a2a as well as MCP clients. For more advanced features look at the tutorials section

## Client 

If you want to connect a java base client you can use 
```java
  A2AAgent a2aagent = new A2AAgent();
  a2aagent.connect("http://localhost:7860");
  Object task = a2aagent.remoteMethodCall("vishal is coming home what should i cook");
```


## Contributing
Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change

## Important Disclaimer(s)

  
* The A2A and MCP protocol is an evolving standard, and this implementation may need to be updated as the protocols matures.  Always refer to the official A2A documentation for the latest specifications and best practices.  
* This implementation is not affiliated with or endorsed by Google or Anthropic.  It is my independent effort to demonstrate the A2A and MCP protocol in Java.
* Unit test coverage needs to be enhanced , will be working on it 
  
  
  

