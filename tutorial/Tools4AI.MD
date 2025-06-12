# Configuration Guide

## ✅ Gemini Related Settings
These settings configure the use of Google's Gemini models.

- **gemini.modelName**: Specifies the main Gemini model to use (e.g., gemini-2.0-flash-001).
- **gemini.location**: The regional location for the Gemini service (e.g., us-central1).
- **gemini.projectId**: Google Cloud project ID associated with the Gemini API.
- **gemini.vision.modelName**: Specifies the Gemini Vision model for image-related tasks (e.g., gemini-1.0-pro-vision).

## 🧠 Anthropic (Claude) Related Settings
These are settings for integrating with Anthropic's Claude models.

- **anthropic.modelName**: The Claude model to use (e.g., claude-3-haiku-20240307).
- **anthropic.logRequests**: If set to true, the system logs requests made to Claude.
- **anthropic.logResponse**: If true, logs the response from Claude.
- **claudeKey**: API key for authenticating with Claude; can be set in the properties file or passed as a JVM argument (-DclaudeKey).

## 🔓 OpenAI Related Settings
These define how to connect to OpenAI models.

- **openAiKey**: API key for OpenAI. Can be set here or passed as a JVM option (-DopenAiKey).
- **openAiBaseURL**: Base URL for OpenAI requests. Useful when using a proxy or custom endpoint (e.g., Langchain4j).
- **openAiModelName**: The OpenAI model to use (e.g., gpt-4o-mini).

## 🔍 Serper (Google Search/Hallucination Detection)
- **serperKey**: API key for Serper, a service used for Google Search or hallucination detection. If empty, the feature may be disabled.

## 🧠 Agent Provider Configuration
- **agent.provider**: Indicates the default agent provider being used (openai, anthropic, etc.).

## 🧘 Action Packages
- **action.packages.to.scan** : Specifies which Java package(s) to scan for agent actions. Only actions from these packages will be registered with the server.

## 🔧 MCP / A2A Settings
- **mcp.tools.servername**: Name of the MCP tools server, shown in UI or logs (e.g., "The Best Server For Customer Service").
- **mcp.tools.version**: Version of the tools server (e.g., "1.0.0").
- **a2a.card.name**: The display name for the A2A card, typically shown in UIs (e.g., "Tools4AI Based A2A Server").

## 🧠 A2A Card Metadata Configuration
These properties configure how an AI Agent card appears and behaves in your A2A/MCP system.

| Property Key | Description |
|-------------|-------------|
| a2a.card.description | A human-readable description of the agent or tool. |
| a2a.card.name | The display name of the agent card. |
| a2a.card.capabilities.streaming | Enables or disables support for streaming responses (true / false). |
| a2a.card.capabilities.pushNotifications | Enables push notifications capability (true / false). |
| a2a.card.capabilities.stateTransitionHistory | Records the history of state transitions for the card (true / false). |
| a2a.card.url | A URL pointing to the service, dashboard, or card landing page. |
| a2a.card.version | The version of the card or tool. |
| a2a.card.documentationUrl | URL to the agent/tool's official documentation. |
| a2a.card.defaultOutputModes | Comma-separated list of default output formats (e.g., text,json). |
| a2a.card.defaultInputModes | Comma-separated list of input formats supported by the agent. |
| a2a.card.provider.organization | The organization providing the agent/tool. |
| a2a.card.provider.url | URL to the provider's homepage or service endpoint. |
| a2a.card.authentication.schemes | Comma-separated list of supported authentication schemes. |
| a2a.card.authentication.credentials | Credentials (e.g., tokens or API keys) required to authenticate. |

## ⚙️ MCP Tooling Server Settings
These properties configure the MCP tools server metadata, usually for UI or service registration.

| Property Key | Description |
|-------------|-------------|
| mcp.tools.servername | The display name of your MCP tools server. |
| mcp.tools.version | Version of the MCP tools server. |
| mcp.tools.protocolversion | The protocol version of the MCP/A2A communication being used. |
