package io.github.vishalmysore.a2a.domain;



import lombok.Data;
import lombok.ToString;

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
}
