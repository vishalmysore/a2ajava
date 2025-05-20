const http = require("http");

const targetUrl = process.argv[2] || "http://localhost:7860"; // Get URL from arguments
const { hostname, port, pathname } = new URL(targetUrl);

process.stdin.setEncoding("utf8");

let buffer = "";

// More robust keep-alive mechanism
const keepAliveInterval = setInterval(() => {
  console.error("💓 Keep-alive ping...");
}, 10000);

// Handle termination signals properly
process.on('SIGINT', () => {
  console.error("Received SIGINT. Shutting down gracefully...");
  clearInterval(keepAliveInterval);
  process.exit(0);
});

process.on('uncaughtException', (err) => {
  console.error("❌ Uncaught exception:", err);
  // Don't exit, try to keep the process alive
});

process.stdin.on("data", (chunk) => {
  buffer += chunk;
  let boundary;
  while ((boundary = buffer.indexOf("\n")) >= 0) {
    const line = buffer.slice(0, boundary);
    buffer = buffer.slice(boundary + 1);
    if (line.trim()) {
      traceAndForward(line);
    }
  }
});

function traceAndForward(line) {
  try {
    console.error("\n⬅️ Incoming from client:\n" + line);

    const json = JSON.parse(line);
    const postData = JSON.stringify(json);

    const options = {
      hostname,
      port,
      path: pathname || "/invoke",
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Content-Length": Buffer.byteLength(postData),
        "Connection": "keep-alive" // Explicitly keep the connection alive
      },
    };

    const req = http.request(options, (res) => {
      let responseBody = "";
      res.setEncoding("utf8");
      res.on("data", (chunk) => (responseBody += chunk));
      res.on("end", () => {
            if (responseBody.trim() === "") {
                console.warn("⚠️ Empty response body received (likely a JSON-RPC notification).");
                return; // Do nothing for empty body
              }
        try {
          console.error("➡️ Response from Spring server:\n" + responseBody);
          const response = JSON.parse(responseBody);
          const finalResponse = JSON.stringify(response);
          process.stdout.write(finalResponse + "\n");
        } catch (e) {
          console.error("❌ Failed to parse response from backend:", e.message);
          // Send an error response to client instead of failing silently
          const errorResponse = {
            jsonrpc: "2.0",
            id: json.id || null,
            error: {
              code: -32603,
              message: "Internal error processing response: " + e.message
            }
          };
          process.stdout.write(JSON.stringify(errorResponse) + "\n");
        }
      });
    });

    req.on("error", (e) => {
      console.error("❌ Request error to Spring server:", e.message);
      // Send error back to client
      const errorResponse = {
        jsonrpc: "2.0",
        id: json.id || null,
        error: {
          code: -32603,
          message: "Failed to connect to backend: " + e.message
        }
      };
    //  process.stdout.write(JSON.stringify(errorResponse) + "\n");
    });

    req.write(postData);
    req.end();
  } catch (e) {
    console.error("❌ Invalid JSON from client:", e.message);
    // Send error back to client
    try {
      const errorResponse = {
        jsonrpc: "2.0",
        id: null,
        error: {
          code: -32700,
          message: "Parse error: " + e.message
        }
      };
      process.stdout.write(JSON.stringify(errorResponse) + "\n");
    } catch (writeErr) {
      console.error("❌ Failed to write error response:", writeErr);
    }
  }
}

// Log startup information
console.error(`🚀 MCP passthrough server started, forwarding to ${targetUrl}`);