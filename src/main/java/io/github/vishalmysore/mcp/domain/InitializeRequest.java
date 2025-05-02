package io.github.vishalmysore.mcp.domain;

/**
 * This request is sent from the client to the server when it first connects, asking it to begin initialization.
 */
public class InitializeRequest extends ClientRequest {
    private final String method = "initialize";
    private Params params;

    public String getMethod() {
        return method;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public static class Params {
        private ClientCapabilities capabilities;
        private Implementation clientInfo;
        /**
         * The latest version of the Model Context Protocol that the client supports.
         * The client MAY decide to support older versions as well.
         */
        private String protocolVersion;

        public ClientCapabilities getCapabilities() {
            return capabilities;
        }

        public void setCapabilities(ClientCapabilities capabilities) {
            this.capabilities = capabilities;
        }

        public Implementation getClientInfo() {
            return clientInfo;
        }

        public void setClientInfo(Implementation clientInfo) {
            this.clientInfo = clientInfo;
        }

        public String getProtocolVersion() {
            return protocolVersion;
        }

        public void setProtocolVersion(String protocolVersion) {
            this.protocolVersion = protocolVersion;
        }
    }
}