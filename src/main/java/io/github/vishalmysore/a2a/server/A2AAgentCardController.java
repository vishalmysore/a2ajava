package io.github.vishalmysore.a2a.server;

import com.t4a.api.AIAction;
import com.t4a.api.GenericJavaMethodAction;
import com.t4a.predict.PredictionLoader;
import com.t4a.processor.AIProcessingException;
import com.t4a.transform.PromptTransformer;
import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.a2a.domain.Skill;
import org.springframework.http.ResponseEntity;

public interface A2AAgentCardController {
    public ResponseEntity<AgentCard> getAgentCard();

    public PromptTransformer getPromptTransformer();

    default Skill getSkill(String actionName) throws AIProcessingException {
        GenericJavaMethodAction action = (GenericJavaMethodAction)PredictionLoader.getInstance().getAiAction(actionName);
        StringBuilder description = new StringBuilder("Name of the skill : "+action.getActionName()+" description : " + action.getDescription()+" parameter "+action.getJsonRPC());
        Skill skill = (Skill)getPromptTransformer().transformIntoPojo(
                "use this description and also populate skills in detail " + description,
                Skill.class);
        return skill;
    }
}
