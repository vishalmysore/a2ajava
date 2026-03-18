package io.github.vishalmysore.mcp.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JSONRPCRequestTest {

    @Test
    public void testDefaultValues() {
        JSONRPCRequest request = new JSONRPCRequest();
        assertEquals("2.0", request.getJsonrpc());
        assertNull(request.getId());
        assertNull(request.getMethod());
        assertNull(request.getParams());
    }

    @Test
    public void testSetAndGetId() {
        JSONRPCRequest request = new JSONRPCRequest();
        request.setId("req-001");
        assertEquals("req-001", request.getId());
    }

    @Test
    public void testSetAndGetMethod() {
        JSONRPCRequest request = new JSONRPCRequest();
        request.setMethod("tools/list");
        assertEquals("tools/list", request.getMethod());
    }

    @Test
    public void testSetAndGetParams() {
        JSONRPCRequest request = new JSONRPCRequest();
        JSONRPCRequest.Params params = new JSONRPCRequest.Params();

        JSONRPCRequest.Params.Meta meta = new JSONRPCRequest.Params.Meta();
        meta.setProgressToken("progress-token-123");
        params.setMeta(meta);

        request.setParams(params);

        assertNotNull(request.getParams());
        assertNotNull(request.getParams().getMeta());
        assertEquals("progress-token-123", request.getParams().getMeta().getProgressToken());
    }

    @Test
    public void testJsonrpcVersionIsFixed() {
        JSONRPCRequest request = new JSONRPCRequest();
        assertEquals("2.0", request.getJsonrpc());
    }

    @Test
    public void testFullRequest() {
        JSONRPCRequest request = new JSONRPCRequest();
        request.setId("42");
        request.setMethod("tools/call");

        JSONRPCRequest.Params params = new JSONRPCRequest.Params();
        JSONRPCRequest.Params.Meta meta = new JSONRPCRequest.Params.Meta();
        meta.setProgressToken("token-abc");
        params.setMeta(meta);
        request.setParams(params);

        assertEquals("42", request.getId());
        assertEquals("tools/call", request.getMethod());
        assertEquals("2.0", request.getJsonrpc());
        assertEquals("token-abc", request.getParams().getMeta().getProgressToken());
    }

    @Test
    public void testBatchRequest() {
        JSONRPCBatchRequest batch = new JSONRPCBatchRequest();
        List<Object> requests = new ArrayList<>();

        JSONRPCRequest req1 = new JSONRPCRequest();
        req1.setId("1");
        req1.setMethod("tools/list");

        JSONRPCRequest req2 = new JSONRPCRequest();
        req2.setId("2");
        req2.setMethod("tools/call");

        requests.add(req1);
        requests.add(req2);
        batch.setJsonRpcRequests(requests);

        assertNotNull(batch.getJsonRpcRequests());
        assertEquals(2, batch.getJsonRpcRequests().size());
    }

    @Test
    public void testBatchResponse() {
        JSONRPCBatchResponse batch = new JSONRPCBatchResponse();
        List<Object> responses = new ArrayList<>();

        JSONRPCResponse resp1 = new JSONRPCResponse();
        JSONRPCResponse resp2 = new JSONRPCResponse();

        responses.add(resp1);
        responses.add(resp2);
        batch.setJsonRpcResponses(responses);

        assertNotNull(batch.getJsonRpcResponses());
        assertEquals(2, batch.getJsonRpcResponses().size());
    }
}
