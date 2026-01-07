package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * A2A Protocol extension declaration in AgentCard.
 * Used to advertise agent capabilities like A2UI support.
 * 
 * Example for A2UI:
 * {
 *   "uri": "https://a2ui.org/a2a-extension/a2ui/v0.8",
 *   "description": "Ability to render A2UI",
 *   "required": false,
 *   "params": {
 *     "supportedCatalogIds": ["https://github.com/google/A2UI/..."],
 *     "acceptsInlineCatalogs": true
 *   }
 * }
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentExtension {
    /**
     * URI identifying the extension (e.g., "https://a2ui.org/a2a-extension/a2ui/v0.8")
     */
    private String uri;
    
    /**
     * Human-readable description of the extension
     */
    private String description;
    
    /**
     * Whether client MUST support this extension to use the agent
     */
    private boolean required;
    
    /**
     * Extension-specific parameters (e.g., supportedCatalogIds for A2UI)
     * Temporarily ignored for serialization to avoid tools4ai Map type warnings
     */
    @JsonIgnore
    private Map<String, Object> params;
}
