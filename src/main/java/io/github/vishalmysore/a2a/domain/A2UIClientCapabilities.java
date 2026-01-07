package io.github.vishalmysore.a2a.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * A2UI client capabilities sent in message metadata.
 * Declares which A2UI catalogs the client can render.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class A2UIClientCapabilities {
    /**
     * List of catalog URIs the client supports
     */
    private List<String> supportedCatalogIds;
    
    /**
     * Optional inline catalog definitions for custom components
     */
    private List<Map<String, Object>> inlineCatalogs;
}
