package io.github.vishalmysore;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/.well-known")
public class AgentCardController {
    @GetMapping(value = "/agent.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentCard> getAgentCard() {
        AgentCard agentCard = new AgentCard();
        agentCard.setName("ChotuRobo : Control Robots using A2A");
        agentCard.setDescription("Control your robots using A2A");
        agentCard.setUrl("http://localhost:8080"); //  Replace with actual URL
        agentCard.setProvider(new Provider("Chotu Corp", "https://github.com/vishalmysore/choturobo")); // Replace
        agentCard.setVersion("1.0.0");
        agentCard.setDocumentationUrl("https://github.com/vishalmysore/Tools4AI");  // Replace
        agentCard.setCapabilities(new Capabilities(true, false, false)); // Corrected
        agentCard.setAuthentication(new Authentication(new String[]{"Bearer"}));
        agentCard.setDefaultInputModes(new String[]{"text/plain"});
        agentCard.setDefaultOutputModes(new String[]{"application/json"});

        Skill bookTicketSkill = new Skill();
        bookTicketSkill.setId("book-ticket");
        bookTicketSkill.setName("Book Ticket");
        bookTicketSkill.setDescription("Book an airline ticket");
        bookTicketSkill.setTags(new String[]{"travel", "airline", "booking"});
        bookTicketSkill.setExamples(new String[]{"Book a flight from New York to Los Angeles on 2024-05-10"});
        bookTicketSkill.setInputModes(new String[]{"application/json"}); //override
        bookTicketSkill.setOutputModes(new String[]{"application/json"});

        agentCard.setSkills(new Skill[]{bookTicketSkill});

        return ResponseEntity.ok(agentCard);
    }
}
