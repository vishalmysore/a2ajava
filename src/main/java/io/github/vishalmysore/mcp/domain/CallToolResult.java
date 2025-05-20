package io.github.vishalmysore.mcp.domain;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * The server's response to a tool call.
 * <p>
 * Any errors that originate from the tool SHOULD be reported inside the result
 * object, with `isError` set to true, _not_ as an MCP protocol-level error
 * response. Otherwise, the LLM would not be able to see that an error occurred
 * and self-correct.
 * <p>
 * However, any errors in _finding_ the tool, an error indicating that the
 * server does not support tool calls, or any other exceptional conditions,
 * should be reported as an MCP error response.
 */
@Data
@ToString
public class CallToolResult extends Result{
    @NotNull
    private List<Content> content;
  //  private Boolean isError;
  //  private Map<String, Object> _meta;
}