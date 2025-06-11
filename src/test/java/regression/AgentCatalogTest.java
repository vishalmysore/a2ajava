package regression;

import com.t4a.JsonUtils;
import com.t4a.processor.AIProcessingException;
import com.t4a.transform.OpenAIPromptTransformer;
import com.t4a.transform.PromptTransformer;
import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.common.Agent;
import io.github.vishalmysore.common.AgentIdentity;
import io.github.vishalmysore.mcp.client.MCPAgent;
import io.github.vishalmysore.mesh.AgentCatalog;
import lombok.extern.java.Log;

@Log
public class AgentCatalogTest {
    public static void main(String[] args) throws AIProcessingException {
        AgentCatalog agentCatalog = new AgentCatalog();
        Agent agent = agentCatalog.addAgent("https://vishalmysore-a2amcpspring.hf.space/");
        JsonUtils jsonUtils = new JsonUtils();
      //  MCPAgent mcpAgent = new MCPAgent();
    //    mcpAgent.connect("http://localhost:7860/");
      //  agentCatalog.addAgent(mcpAgent);

     //   log.info(agentCatalog.getAgentsInfo());
       // PromptTransformer promptTransformer = new OpenAIPromptTransformer();
       // String identiy = promptTransformer.transformIntoJson("{agentUniqueIDTobeUsedToIdentifyTheAgent:''}"," this is user prompt { what is vishals favorite food }  I am trying to find which agent can handle it from this info {"+agentCatalog.getAgentsInfo()+"}");
     //   log.info("Agent Identity: " + identiy);
        //agent = agentCatalog.retrieveAgentByJson(jsonUtils.extractJson(identiy));
        //log.info("Retrieved Agent: " + agent.getType());

        //identiy = promptTransformer.transformIntoJson("{agentUniqueIDTobeUsedToIdentifyTheAgent:''}"," this is user prompt { i need to read harry potter book }  I am trying to find which agent can handle it from this info {"+agentCatalog.getAgentsInfo()+"}");
      //  log.info("Agent Identity: " + identiy);
      //  agent = agentCatalog.retrieveAgentByJson(jsonUtils.extractJson(identiy));
     //   log.info("Retrieved Agent: " + agent.getType());

        log.info(agentCatalog.processQuery("what is vishal favorite food:").getTextResult());

        log.info(agentCatalog.processQuery("get me the list of the books").getTextResult());

    }
}
