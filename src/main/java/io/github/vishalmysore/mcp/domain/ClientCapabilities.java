package io.github.vishalmysore.mcp.domain;

import java.util.Map;

/**
 * Capabilities a client may support. Known capabilities are defined here,
 * in this schema, but this is not a closed set: any client can define its
 * own, additional capabilities.
 */
public class ClientCapabilities {
    /**
     * Experimental, non-standard capabilities that the client supports.
     */
    private Map<String, Map<String, Object>> experimental;
    /**
     * Present if the client supports listing roots.
     */
    private Roots roots;
    /**
     * Present if the client supports sampling from an LLM.
     */
    private Map<String, Object> sampling;

    public Map<String, Map<String, Object>> getExperimental() {
        return experimental;
    }

    public void setExperimental(Map<String, Map<String, Object>> experimental) {
        this.experimental = experimental;
    }

    public Roots getRoots() {
        return roots;
    }

    public void setRoots(Roots roots) {
        this.roots = roots;
    }

    public Map<String, Object> getSampling() {
        return sampling;
    }

    public void setSampling(Map<String, Object> sampling) {
        this.sampling = sampling;
    }

    public static class Roots {
        /**
         * Whether the client supports notifications for changes to the roots list.
         */
        private Boolean listChanged;

        public Boolean getListChanged() {
            return listChanged;
        }

        public void setListChanged(Boolean listChanged) {
            this.listChanged = listChanged;
        }
    }
}
