package io.github.vishalmysore.common;

/**
 * Registry for all the agents and cards for both MCP and A2A
 * I'd like to propose a flexible enhancement to the A2A protocol to support client-specific agent experiences. Currently, AgentCard objects are generally served uniformly to all clients, regardless of the context or identity of the requesting client. In many real-world applications, however, different clients may need different views, capabilities, or even agent skillsets based on:
 *
 * Their identity (e.g., clientId)
 *
 * Their type (e.g., mobile vs. desktop, internal vs. external)
 *
 * Runtime context (e.g., device info, region, tenant)
 *
 * 💡 Proposal Summary
 * Introduce a ClientInfo object in the A2A request schema that allows clients to optionally identify themselves, enabling the server to tailor AgentCard responses accordingly.
 *
 * Example Schema:
 * json
 * Copy
 * Edit
 * "clientInfo": {
 *   "type": "object",
 *   "properties": {
 *     "clientId": { "type": "string" },
 *     "clientType": { "type": "string" },
 *     "deviceId": { "type": "string" },
 *     "tags": {
 *       "type": "array",
 *       "items": { "type": "string" }
 *     }
 *   },
 *   "required": ["clientId"]
 * }
 * Optionally, enhance AgentCard with:
 * json
 * Copy
 * Edit
 * "audience": {
 *   "type": "array",
 *   "items": { "type": "string" },
 *   "description": "List of client IDs or tags this card is intended for."
 * }
 * 🧠 Use Cases
 * Multi-tenant SaaS: Serve different capabilities to clients based on their subscription plan.
 *
 * Context-aware agents: Deliver simplified AgentCards to mobile clients and full-featured ones to desktop.
 *
 * Security filtering: Hide certain skills or capabilities for restricted clients or user roles.
 *
 * A/B testing: Dynamically adjust agent exposure based on experiment tags.
 *
 * ✅ Benefits
 * Backward-compatible: Clients that don’t provide clientInfo can continue receiving the default card.
 *
 * Enhances personalization and UX.
 *
 * Enables richer agent ecosystem logic (like context-specific cards or private/internal agent networks).
 */
public class ClientRegistryForAgents {
}
