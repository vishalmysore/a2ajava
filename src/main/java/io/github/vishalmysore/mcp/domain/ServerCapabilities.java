package io.github.vishalmysore.mcp.domain;

import java.util.List;

public class ServerCapabilities {
    /**
     * The content types the server supports.
     */
    private List<String> contentTypes;
    /**
     * The roles the server supports in prompts.
     */
    private List<String> roles;
    /**
     * The sampling methods the server supports.
     */
    private List<String> samplingMethods;
    /**
     * The tool types the server supports.
     */
    private List<String> toolTypes;

    public List<String> getContentTypes() {
        return contentTypes;
    }

    public void setContentTypes(List<String> contentTypes) {
        this.contentTypes = contentTypes;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getSamplingMethods() {
        return samplingMethods;
    }

    public void setSamplingMethods(List<String> samplingMethods) {
        this.samplingMethods = samplingMethods;
    }

    public List<String> getToolTypes() {
        return toolTypes;
    }

    public void setToolTypes(List<String> toolTypes) {
        this.toolTypes = toolTypes;
    }
}
