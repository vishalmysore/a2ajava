import { Server } from "@modelcontextprotocol/sdk/server/index.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { CallToolRequestSchema, ListResourcesRequestSchema, ListToolsRequestSchema, ReadResourceRequestSchema, } from "@modelcontextprotocol/sdk/types.js";
import fs from 'node:fs/promises';

const logFilePath = '/temp/proxy_server.log';

const SERVER_BASE_URL = process.env.SERVER_BASE_URL || "http://localhost:7860";

// Create server
const server = new Server({
  name: "springboot-proxy",
  version: "1.0.0",
}, {
  capabilities: {
    tools: {},  // We'll load tools dynamically from Spring Boot
  },
});

// Handler: List tools from Spring Boot
server.setRequestHandler(ListToolsRequestSchema, async () => {
  try {
    const response = await fetch("${SERVER_BASE_URL}/mcp/list-tools", {
      method: "GET",
      headers: { "Content-Type": "application/json" }
    });

    if (!response.ok) {
      const errorMessage = `Failed to fetch tools: ${response.statusText}`;
     // await logToFile(errorMessage);
      throw new Error(errorMessage);
    }

    const data = await response.json();
   // await logToFile(`Available tools from Spring Boot: ${JSON.stringify(data, null, 2)}`);
    return {
      tools: data.tools,
    };
  } catch (error) {
    console.error("Error listing tools:", error);
    throw error;
  }
});

// Handler: Call a tool by proxying to Spring Boot
server.setRequestHandler(CallToolRequestSchema, async (request) => {
  try {
  fs.appendFile(logFilePath, "receivedResponseLog", 'utf8');
    // 🔍 Log the outgoing request
    const outgoingRequestLog = "➡️ Sending request to Spring Boot:\n" + JSON.stringify({
      name: request.params.name,
      arguments: request.params.arguments ?? {},
    }, null, 2);
  //  await logToFile(outgoingRequestLog);

    const response = await fetch("${SERVER_BASE_URL}/mcp/call-tool", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        name: request.params.name,
        arguments: request.params.arguments ?? {},
      }),
    });

    // ❌ Log failure if not OK
    if (!response.ok) {
      const errorText = await response.text();  // Read error body
      const errorMessage = `❌ Tool call failed: ${response.statusText}\n🔻 Error response body: ${errorText}`;
      await logToFile(errorMessage);
      throw new Error(`Tool call failed: ${response.statusText}`);
    }

    // console.log(response.json());
    // ✅ Log the response data
    const data = await response.json();
    fs.appendFile(logFilePath, "-----------------------------", 'utf8');
    fs.appendFile(logFilePath, JSON.stringify(data["result"]), 'utf8');
    fs.appendFile(logFilePath, "-----------------------------", 'utf8');

    // Log the received response
    // console.log("------------------------------------------");
    // console.log("Received response from Spring Boot:", data);
    // console.log("------------------------------------------");
    // const receivedResponseLog = "✅ Received response from Spring Boot:\n" + JSON.stringify(data, null, 2);
    //   fs.appendFile(logFilePath, receivedResponseLog, 'utf8');
    // let res =  {"content":[{"annotations":null,"text":"Temprature for toronto is 18.0","type":"text"}],"isError":false,"_meta":null};
    // json remove attribute _meta

    const res = data.result;
    delete res._meta;
    delete res.isError;
    // console.log("Received response from Spring Boot:", res);
    return res; // Must match CallToolResponseSchema
  } catch (error) {
    console.error("Error calling tool:", error);
    throw error;
  }
});



// Launch server over stdio
async function runServer() {
  const transport = new StdioServerTransport();
  await server.connect(transport);
 // await logToFile("Proxy server is running on stdio...");
}

runServer().catch(console.error);