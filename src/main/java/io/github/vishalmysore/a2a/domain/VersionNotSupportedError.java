package io.github.vishalmysore.a2a.domain;

/**
 * A2A v1.0: Error returned when the A2A protocol version specified 
 * in the request (via A2A-Version header) is not supported by the agent.
 */
public class VersionNotSupportedError extends JSONRPCError {
    public VersionNotSupportedError(String message) {
        super(-32009, message);
    }
    
    public VersionNotSupportedError(String message, java.util.Map<String, Object> data) {
        super(-32009, message, data);
    }
}
