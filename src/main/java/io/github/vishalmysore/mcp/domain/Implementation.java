package io.github.vishalmysore.mcp.domain;

/**
 * Describes the name and version of an MCP implementation.
 */
public class Implementation {
    private String name;
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
