package io.github.vishalmysore.a2a.domain;

import com.t4a.annotations.ListType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Capabilities {
    private boolean streaming;
    private boolean pushNotifications;
    private boolean stateTransitionHistory;
    
    /**
     * A2A Protocol extensions supported by this agent (e.g., A2UI)
     */
    @ListType(AgentExtension.class)
    private List<AgentExtension> extensions;
    
    /**
     * Backward-compatible constructor (without extensions)
     */
    public Capabilities(boolean streaming, boolean pushNotifications, boolean stateTransitionHistory) {
        this.streaming = streaming;
        this.pushNotifications = pushNotifications;
        this.stateTransitionHistory = stateTransitionHistory;
        this.extensions = null;
    }
}

