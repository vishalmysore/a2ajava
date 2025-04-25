package io.github.vishalmysore;

public class Provider {
    private String organization;
    private String url;

    public Provider() {
    }

    public Provider(String organization, String url) {
        this.organization = organization;
        this.url = url;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}