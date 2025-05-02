package io.github.vishalmysore.mcp.domain;

import java.util.List;
import java.util.Map;

/**
 * The server's response to a completion/complete request
 */
public class CompleteResult extends ClientResult {
    /**
     * This result property is reserved by the protocol to allow clients and servers to attach additional metadata to their responses.
     */
    private Map<String, Object> _meta;
    private Completion completion;

    public Map<String, Object> getMeta() {
        return _meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this._meta = meta;
    }

    public Completion getCompletion() {
        return completion;
    }

    public void setCompletion(Completion completion) {
        this.completion = completion;
    }

    public static class Completion {
        /**
         * Indicates whether there are additional completion options beyond those provided in the current response, even if the exact total is unknown.
         */
        private Boolean hasMore;
        /**
         * The total number of completion options available. This can exceed the number of values actually sent in the response.
         */
        private Integer total;
        /**
         * An array of completion values. Must not exceed 100 items.
         */
        private List<String> values;

        public Boolean getHasMore() {
            return hasMore;
        }

        public void setHasMore(Boolean hasMore) {
            this.hasMore = hasMore;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<String> getValues() {
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }
    }
}
