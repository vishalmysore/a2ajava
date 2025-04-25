package io.github.vishalmysore;

class AgentCard {
    private String name;
    private String description;
    private String url;
    private Provider provider;
    private String version;
    private String documentationUrl;
    private Capabilities capabilities;
    private Authentication authentication;
    private String[] defaultInputModes;
    private String[] defaultOutputModes;
    private Skill[] skills;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDocumentationUrl() {
        return documentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    public Capabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public String[] getDefaultInputModes() {
        return defaultInputModes;
    }

    public void setDefaultInputModes(String[] defaultInputModes) {
        this.defaultInputModes = defaultInputModes;
    }

    public String[] getDefaultOutputModes() {
        return defaultOutputModes;
    }

    public void setDefaultOutputModes(String[] defaultOutputModes) {
        this.defaultOutputModes = defaultOutputModes;
    }

    public Skill[] getSkills() {
        return skills;
    }

    public void setSkills(Skill[] skills) {
        this.skills = skills;
    }
}
