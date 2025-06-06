package regression;

import com.t4a.JsonUtils;
import io.github.vishalmysore.common.Agent;
import io.github.vishalmysore.mcp.client.MCPAgent;
import io.github.vishalmysore.mesh.AgentCatalog;
import io.github.vishalmysore.mesh.AgenticMesh;

public class AgenticMeshTest {
    public static void main(String[] args) {
        AgentCatalog agentCatalog = new AgentCatalog();
        Agent agent = agentCatalog.addAgent("https://vishalmysore-a2amcpspring.hf.space/");
        MCPAgent mcpAgent = new MCPAgent();
        mcpAgent.connect("http://localhost:7860/");
        agentCatalog.addAgent(mcpAgent);
        AgenticMesh agenticMesh = new AgenticMesh(agentCatalog);
        String reponse = agenticMesh.pipeLineMesh("what is vishal favorite food:").getTextResult();
        System.out.println("Response: " + reponse);
        reponse = agenticMesh.pipeLineMesh("what is vishal favorite food and get him a list of books to read").getTextResult();
        System.out.println("Response: " + reponse);
    }
}
