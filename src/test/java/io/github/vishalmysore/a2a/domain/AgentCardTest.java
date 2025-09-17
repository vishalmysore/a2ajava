package io.github.vishalmysore.a2a.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AgentCardTest {
    
    private AgentCard agentCard;
    
    @BeforeEach
    public void setUp() {
        agentCard = new AgentCard();
    }

    @Test
    public void testAgentCardInitialization() {
        assertNotNull(agentCard, "AgentCard should be initialized successfully");
    }
    
    @Test
    public void testBasicProperties() {
        // Set properties
        agentCard.setName("Test Agent");
        agentCard.setDescription("A test agent for unit tests");
        agentCard.setUrl("https://test-agent.example.com");
        agentCard.setVersion("1.0.0");
        agentCard.setDocumentationUrl("https://test-agent.example.com/docs");
        
        // Verify properties
        assertEquals("Test Agent", agentCard.getName());
        assertEquals("A test agent for unit tests", agentCard.getDescription());
        assertEquals("https://test-agent.example.com", agentCard.getUrl());
        assertEquals("1.0.0", agentCard.getVersion());
        assertEquals("https://test-agent.example.com/docs", agentCard.getDocumentationUrl());
    }
    
    @Test
    public void testProviderProperty() {
        Provider provider = new Provider();
        provider.setOrganization("Test Organization");
        provider.setUrl("https://provider.example.com");
        
        agentCard.setProvider(provider);
        
        assertNotNull(agentCard.getProvider());
        assertEquals("Test Organization", agentCard.getProvider().getOrganization());
        assertEquals("https://provider.example.com", agentCard.getProvider().getUrl());
    }
    
    @Test
    public void testCapabilitiesProperty() {
        Capabilities capabilities = new Capabilities();
        capabilities.setStreaming(true);
        capabilities.setPushNotifications(false);
        capabilities.setStateTransitionHistory(true);
        
        agentCard.setCapabilities(capabilities);
        
        assertNotNull(agentCard.getCapabilities());
        assertTrue(agentCard.getCapabilities().isStreaming());
        assertFalse(agentCard.getCapabilities().isPushNotifications());
        assertTrue(agentCard.getCapabilities().isStateTransitionHistory());
    }
    
    @Test
    public void testAuthenticationProperty() {
        Authentication authentication = new Authentication();
        String[] schemes = {"Bearer"};
        authentication.setSchemes(schemes);
        authentication.setCredentials("test-token");
        
        agentCard.setAuthentication(authentication);
        
        assertNotNull(agentCard.getAuthentication());
        assertArrayEquals(schemes, agentCard.getAuthentication().getSchemes());
        assertEquals("test-token", agentCard.getAuthentication().getCredentials());
    }
    
    @Test
    public void testAuthenticationMethods() {
        Authentication authentication = new Authentication();
        
        // Test Basic Auth
        authentication.setBasicAuth("username", "password");
        assertTrue(authentication.isBasicAuth());
        assertFalse(authentication.isBearerAuth());
        assertArrayEquals(new String[]{"Basic"}, authentication.getSchemes());
        assertNotNull(authentication.getCredentials());
        
        // Test Bearer Auth
        authentication.setBearerAuth("bearer-token");
        assertTrue(authentication.isBearerAuth());
        assertFalse(authentication.isBasicAuth());
        assertArrayEquals(new String[]{"Bearer"}, authentication.getSchemes());
        assertEquals("bearer-token", authentication.getCredentials());
        
        // Test API Key Auth
        authentication.setApiKeyAuth("api-key");
        assertArrayEquals(new String[]{"ApiKey"}, authentication.getSchemes());
        assertEquals("api-key", authentication.getCredentials());
        
        agentCard.setAuthentication(authentication);
        assertEquals(authentication, agentCard.getAuthentication());
    }
    
    @Test
    public void testDefaultModes() {
        String[] inputModes = {"text"};
        String[] outputModes = {"text", "json"};
        
        agentCard.setDefaultInputModes(inputModes);
        agentCard.setDefaultOutputModes(outputModes);
        
        assertArrayEquals(inputModes, agentCard.getDefaultInputModes());
        assertArrayEquals(outputModes, agentCard.getDefaultOutputModes());
    }
    
    @Test
    public void testAddSkill_WithNameAndDescription() {
        agentCard.addSkill("Test Skill", "A test skill");
        
        assertNotNull(agentCard.getSkills());
        assertEquals(1, agentCard.getSkills().size());
        assertEquals("Test Skill", agentCard.getSkills().get(0).getName());
        assertEquals("A test skill", agentCard.getSkills().get(0).getDescription());
        assertNotNull(agentCard.getSkills().get(0).getId(), "Skill ID should be auto-generated");
    }
    
    @Test
    public void testAddSkill_WithNameDescriptionAndTags() {
        String[] tags = {"tag1", "tag2"};
        agentCard.addSkill("Test Skill", "A test skill", tags);
        
        assertNotNull(agentCard.getSkills());
        assertEquals(1, agentCard.getSkills().size());
        assertEquals("Test Skill", agentCard.getSkills().get(0).getName());
        assertEquals("A test skill", agentCard.getSkills().get(0).getDescription());
        assertArrayEquals(tags, agentCard.getSkills().get(0).getTags());
    }
    
    @Test
    public void testAddMultipleSkills() {
        agentCard.addSkill("Skill 1", "First skill");
        agentCard.addSkill("Skill 2", "Second skill");
        
        assertNotNull(agentCard.getSkills());
        assertEquals(2, agentCard.getSkills().size());
        assertEquals("Skill 1", agentCard.getSkills().get(0).getName());
        assertEquals("Skill 2", agentCard.getSkills().get(1).getName());
    }
    
    @Test
    public void testEqualsAndHashCode() {
        AgentCard card1 = new AgentCard();
        card1.setName("Test Card");
        card1.setVersion("1.0");
        
        AgentCard card2 = new AgentCard();
        card2.setName("Test Card");
        card2.setVersion("1.0");
        
        AgentCard card3 = new AgentCard();
        card3.setName("Different Card");
        card3.setVersion("1.0");
        
        // Test equals
        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
        
        // Test hashCode
        assertEquals(card1.hashCode(), card2.hashCode());
        assertNotEquals(card1.hashCode(), card3.hashCode());
    }
    
    @Test
    public void testToString() {
        agentCard.setName("Test Agent");
        agentCard.setVersion("1.0.0");
        
        String toString = agentCard.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("name=Test Agent"));
        assertTrue(toString.contains("version=1.0.0"));
    }
}