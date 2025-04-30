package io.github.vishalmysore.server;

import io.github.vishalmysore.domain.AgentCard;
import org.springframework.http.ResponseEntity;

public interface A2AAgentCardController {
    public ResponseEntity<AgentCard> getAgentCard();
}
