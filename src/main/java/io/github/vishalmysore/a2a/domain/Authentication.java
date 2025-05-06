package io.github.vishalmysore.a2a.domain;


import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.ToString;

@Data
@Embeddable
@ToString(exclude = "credentials")
public class Authentication {
    @ElementCollection
    private String[] schemes;
    private String credentials;

    public Authentication() {
    }

    public Authentication(String[] schemes) {
        this.schemes = schemes;
    }
}
