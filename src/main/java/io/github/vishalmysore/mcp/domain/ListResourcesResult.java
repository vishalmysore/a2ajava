package io.github.vishalmysore.mcp.domain;
import java.util.List;
import java.util.Map;
import java.util.Map;

/**
 * The server's response to a resources/list request from the client.
 */
public class ListResourcesResult extends ClientResult {
    /**
     * This result property is reserved by the protocol to allow clients and servers to attach additional metadata to their responses.
     */
    private Map<String, Object> _meta;
    /**
     * An opaque token representing the pagination position after the last returned result.
     * If present, there may be more results available.
     */
    private String nextCursor; // Changed from Cursor to String
    private List<Resource> resources;

    public Map<String, Object> getMeta() {
        return _meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this._meta = meta;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }
}