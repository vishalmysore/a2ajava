package io.github.vishalmysore.mcp.domain;
import java.util.List;
import java.util.Map;
import java.util.Map;

/**
 * The server's response to a roots/list request from the client.
 */
public class ListRootsResult extends ClientResult {
    /**
     * This result property is reserved by the protocol to allow clients and servers to attach additional metadata to their responses.
     */
    private Map<String, Object> _meta;
    private List<Root> roots;

    public Map<String, Object> getMeta() {
        return _meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this._meta = meta;
    }

    public List<Root> getRoots() {
        return roots;
    }

    public void setRoots(List<Root> roots) {
        this.roots = roots;
    }
}