package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * A JWS detached signature over an AgentCard (RFC 7515), allowing clients to
 * verify the integrity and provenance of a published agent card.
 */
@Data
@ToString
public class AgentCardSignature {
    /**
     * The protected JWS header, base64url encoded.
     */
    @JsonProperty("protected")
    private String protectedHeader;

    /**
     * The JWS signature value, base64url encoded.
     */
    private String signature;

    /**
     * Optional unprotected JWS header values.
     */
    private Map<String, Object> header;
}
