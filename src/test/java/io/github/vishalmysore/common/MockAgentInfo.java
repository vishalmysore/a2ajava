package io.github.vishalmysore.common;

public class MockAgentInfo implements AgentInfo {
    private String agentName;
    private String capabilities;

    public MockAgentInfo(String agentName) {
        this.agentName = agentName;
        this.capabilities = "Mock capabilities for " + agentName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @Override
    public String getAgentCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public String toString() {
        return "MockAgentInfo{" +
                "agentName='" + agentName + '\'' +
                ", capabilities='" + capabilities + '\'' +
                '}';
    }
}