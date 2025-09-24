package io.github.vishalmysore.a2a.server;

import java.util.List;

/**
 * Interface for providing actions for agent cards.
 */
public interface ActionProvider {
    
    /**
     * Gets a list of actions that can be performed.
     * 
     * @return A list of actions.
     */
    Object getActionList();
}