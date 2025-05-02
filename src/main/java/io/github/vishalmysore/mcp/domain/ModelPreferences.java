package io.github.vishalmysore.mcp.domain;

/**
 * The server's preferences for which model to select.
 * The client MAY ignore these preferences.
 */
public class ModelPreferences {
    /**
     * The name of the model that the server prefers.
     */
    private String modelName;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}