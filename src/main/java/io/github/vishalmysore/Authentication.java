package io.github.vishalmysore;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "credentials")
public class Authentication {
    private String[] schemes;
    private String credentials;
}

