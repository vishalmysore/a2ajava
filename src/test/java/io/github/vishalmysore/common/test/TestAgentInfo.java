package io.github.vishalmysore.common.test;

import io.github.vishalmysore.common.AgentInfo;

public class TestAgentInfo implements AgentInfo {
    private String capabilities;
    private String description;

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getAgentCapabilities() {
        return capabilities != null ? capabilities : AgentInfo.super.getAgentCapabilities();
    }
}
