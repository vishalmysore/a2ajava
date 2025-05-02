package io.github.vishalmysore.mcp.domain;

import java.util.Map;

/**
 * After receiving an initialize request from the client, the server sends this response.
 */
public class InitializeResult extends ClientResult {
    /**
     * This result property is reserved by the protocol to allow clients and servers to attach additional metadata to their responses.
     */
    private Map<String, Object> _meta;
    private ServerCapabilities capabilities;
    /**
     * Instructions describing how to use the server and its features.
     * <p>
     * This can be used by clients to improve the LLM's understanding of available tools, resources, etc.
     * It can be thought of like a "hint" to the model. For example, this information MAY be added to the system prompt.
     */
    private String instructions;
    /**
     * The version of the Model Context Protocol that the server wants to use.
     * This may not match the version that the client requested.
     * If the client cannot support this version, it MUST disconnect.
     */
    private String protocolVersion;
    private Implementation serverInfo;

    public Map<String, Object> getMeta() {
        return _meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this._meta = meta;
    }

    public ServerCapabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(ServerCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public Implementation getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(Implementation serverInfo) {
        this.serverInfo = serverInfo;
    }
}
