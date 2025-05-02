package io.github.vishalmysore.mcp.domain;

/**
 * The actual progress data.
 */
public class Progress {
    /**
     * An optional string describing the progress.
     */
    private String message;
    /**
     * A number between 0 and 1 (inclusive) indicating the fraction of work that has been completed.
     */
    private Double value;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}