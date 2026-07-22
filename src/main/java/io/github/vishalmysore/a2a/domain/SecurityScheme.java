package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * Describes an authentication scheme that can be used to secure an agent,
 * modelled on the OpenAPI 3 security scheme object. Referenced by name from
 * the AgentCard's securitySchemes map and its security requirements.
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SecurityScheme {
    /**
     * The type of the scheme: "apiKey", "http", "oauth2", "openIdConnect" or "mutualTLS".
     */
    private String type;

    /**
     * Human readable description of the scheme.
     */
    private String description;

    /**
     * For apiKey schemes: the name of the header, query or cookie parameter.
     */
    private String name;

    /**
     * For apiKey schemes: the location of the key, one of "query", "header" or "cookie".
     */
    private String in;

    /**
     * For http schemes: the HTTP authorization scheme, e.g. "bearer" or "basic".
     */
    private String scheme;

    /**
     * For http bearer schemes: a hint on how the bearer token is formatted, e.g. "JWT".
     */
    private String bearerFormat;

    /**
     * For openIdConnect schemes: the OpenID Connect discovery URL.
     */
    private String openIdConnectUrl;

    /**
     * For oauth2 schemes: the supported flows keyed by flow type
     * ("authorizationCode", "clientCredentials", "implicit", "password").
     */
    private Map<String, Object> flows;
}
