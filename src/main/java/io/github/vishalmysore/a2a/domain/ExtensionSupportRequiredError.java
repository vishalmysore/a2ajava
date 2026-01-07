package io.github.vishalmysore.a2a.domain;

/**
 * A2A v1.0: Error returned when client requested use of an extension 
 * marked as required: true in the Agent Card but the client did not 
 * declare support for it in the request.
 */
public class ExtensionSupportRequiredError extends JSONRPCError {
    public ExtensionSupportRequiredError(String message) {
        super(-32008, message);
    }
    
    public ExtensionSupportRequiredError(String message, java.util.Map<String, Object> data) {
        super(-32008, message, data);
    }
}
