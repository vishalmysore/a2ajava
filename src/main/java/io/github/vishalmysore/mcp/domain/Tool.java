package io.github.vishalmysore.mcp.domain;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class Tool {
    private ToolParameters parameters;



    private ToolInputSchema inputSchema;

    private ToolAnnotations annotations;
    /**
     * Optional annotations for the client.
     */

    /**
     * A description of what the tool does.
     */
    private String description;
    /**
     * The name of the tool.
     */
    private String name;
    /**
     * The type of the tool.
     */
    private String type;



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}