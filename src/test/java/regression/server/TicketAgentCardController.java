package regression.server;

import io.github.vishalmysore.a2a.domain.*;
import io.github.vishalmysore.a2a.server.A2AAgentCardController;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This will be used to provide the agent card for the agent
 * The agent card is a JSON object that describes the capabilities of the agent
 * and how to interact with it.
 * RENAME THE /ticketagent.json to your agent.json before you runt the applicaiton
 */
//@RestController
//@RequestMapping("/.well-known")
@Service
@Log
public class TicketAgentCardController implements A2AAgentCardController {
    //@GetMapping(value = "/ticketagent.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentCard> getAgentCard() {
        AgentCard agentCard = new AgentCard();
        agentCard.setName("TicketQueen : Ticket Booking Agent");
        agentCard.setDescription("book your airlines ticket, hotel ticket, and train ticket , also find out the preference of food of a person, it will also provide details on your existing booking or help in cancelling it or provideing realt time update ");
        agentCard.setUrl("http://localhost:8080"); //  Replace with actual URL
        agentCard.setProvider(new Provider("Ticket Corp", "https://github.com/vishalmysore/choturobo"));
        agentCard.setVersion("1.0.0");
        agentCard.setDocumentationUrl("https://github.com/vishalmysore/Tools4AI");  // Replace
        agentCard.setCapabilities(new Capabilities(false, false, false));
        agentCard.setAuthentication(new Authentication(new String[]{"Bearer"}));
        agentCard.setDefaultInputModes(new String[]{"text/plain"});
        agentCard.setDefaultOutputModes(new String[]{"application/json"});

        Skill foodPreferenceSkill = new Skill();
        foodPreferenceSkill.setId("food-preference");
        foodPreferenceSkill.setName("Food Preference");
        foodPreferenceSkill.setDescription("Provide food preference of a person");
        foodPreferenceSkill.setTags(new String[]{"food", "food preference", "favorite food"});
        foodPreferenceSkill.setExamples(new String[]{"what does the james like to eat?", "what is the food preference of this person?"});
        foodPreferenceSkill.setInputModes(new String[]{"application/json"}); //override
        foodPreferenceSkill.setOutputModes(new String[]{"application/json"});

        Skill bookTicketSkill = new Skill();
        bookTicketSkill.setId("book-ticket");
        bookTicketSkill.setName("Book Ticket");
        bookTicketSkill.setDescription("Book an airline ticket");
        bookTicketSkill.setTags(new String[]{"travel", "airline", "booking"});
        bookTicketSkill.setExamples(new String[]{"Book a flight from New York to Los Angeles on 2024-05-10"});
        bookTicketSkill.setInputModes(new String[]{"application/json"}); //override
        bookTicketSkill.setOutputModes(new String[]{"application/json"});

        Skill[] sk = new Skill[2];
        sk[0] = foodPreferenceSkill;
        sk[1] = bookTicketSkill;

        agentCard.setSkills(List.of(sk));

        return ResponseEntity.ok(agentCard);
    }
}
