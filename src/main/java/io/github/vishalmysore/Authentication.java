package io.github.vishalmysore;

public class Authentication {
    private String[] schemes;
    private String credentials;

    public Authentication() {
    }

    public Authentication(String[] schemes) {
        this.schemes = schemes;
    }

    public String[] getSchemes() {
        return schemes;
    }

    public void setSchemes(String[] schemes) {
        this.schemes = schemes;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }
}

