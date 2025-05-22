package io.github.vishalmysore.a2a.domain;



import lombok.Data;
import lombok.ToString;

import java.util.Base64;

@Data

@ToString(exclude = "credentials")
public class Authentication {

    private String[] schemes;
    private String credentials;

    public Authentication() {
    }

    public Authentication(String[] schemes) {
        this.schemes = schemes;
    }

    public void setBasicAuth(String username, String password) {
        this.schemes = new String[]{"Basic"};
        String auth = username + ":" + password;
        this.credentials = Base64.getEncoder().encodeToString(auth.getBytes());
    }

    public void setBearerAuth(String token) {
        this.schemes = new String[]{"Bearer"};
        this.credentials = token;
    }

    public void setApiKeyAuth(String apiKey) {
        this.schemes = new String[]{"ApiKey"};
        this.credentials = apiKey;
    }

    public boolean isBasicAuth() {
        return schemes != null && schemes.length > 0 && "Basic".equals(schemes[0]);
    }

    public boolean isBearerAuth() {
        return schemes != null && schemes.length > 0 && "Bearer".equals(schemes[0]);
    }


    public static Authentication fromAuthorizationHeader(String header) {
        if (header == null || header.isEmpty()) {
            return null;
        }

        Authentication auth = new Authentication();
        if (header.startsWith("Basic ")) {
            auth.setSchemes(new String[]{"Basic"});
            auth.setCredentials(header.substring(6));
        } else if (header.startsWith("Bearer ")) {
            auth.setSchemes(new String[]{"Bearer"});
            auth.setCredentials(header.substring(7));
        } else {
            auth.setSchemes(new String[]{"ApiKey"});
            auth.setCredentials(header);
        }
        return auth;
    }

    public boolean isValid() {
        return schemes != null &&
                schemes.length > 0 &&
                credentials != null &&
                !credentials.isEmpty();
    }
}
