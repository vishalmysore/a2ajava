package io.github.vishalmysore.a2a.server;

import com.t4a.transform.PromptTransformer;
import io.github.vishalmysore.a2a.domain.AgentCard;
import org.springframework.http.ResponseEntity;

public interface A2AAgentCardController {
    public ResponseEntity<AgentCard> getAgentCard();

    public PromptTransformer getPromptTransformer();
}
