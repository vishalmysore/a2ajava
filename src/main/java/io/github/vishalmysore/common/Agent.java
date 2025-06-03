package io.github.vishalmysore.common;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vishalmysore.mcp.domain.JSONRPCResponse;
import io.github.vishalmysore.mcp.domain.MCPGenericResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public interface Agent {

    void connect(String url, String token);
    default void connect(String url) {
        connect(url, null);
    }
    void disconnect();
    AgentInfo getInfo();
    boolean isConnected();

    URL getServerUrl();

    ObjectMapper getMapper();

    String getType();

    default <T> T invokeRPC(CommonClientRequest request, Class<T> responseType) {
        if (!isConnected() || getServerUrl() == null) {
            throw new IllegalStateException("Agent not connected");
        }

        return getRemoteData(request, responseType);
    }



    default <T> MCPGenericResponse<T> getRemoteData(CommonClientRequest request, TypeReference<MCPGenericResponse<T>> typeRef) {
        try {
            HttpURLConnection conn = (HttpURLConnection) getServerUrl().openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonRequest = getMapper().writeValueAsString(request);
            try (var os = conn.getOutputStream()) {
                os.write(jsonRequest.getBytes(StandardCharsets.UTF_8));
            }

            if (conn.getResponseCode() >= 400) {
                throw new RuntimeException("Request failed: HTTP " + conn.getResponseCode());
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String response = br.lines().collect(Collectors.joining());
                return getMapper().readValue(response, typeRef);  // return full response wrapper
            }

        } catch (IOException e) {
            throw new RuntimeException("Request failed", e);
        }
    }


    default  <T> T getRemoteData(CommonClientRequest request, Class<T> responseType) {
        try {
            HttpURLConnection conn = (HttpURLConnection) getServerUrl().openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);


            String jsonRequest = getMapper().writeValueAsString(request);

            try (var os = conn.getOutputStream()) {
                byte[] input = jsonRequest.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            if (conn.getResponseCode() >= 400) {
                throw new RuntimeException("Request failed: HTTP " + conn.getResponseCode());
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String response = br.lines().collect(Collectors.joining());
                return getMapper().readValue(response, responseType);
            }

        } catch (IOException e) {
            throw new RuntimeException("Request failed", e);
        }
    }

    CommonClientResponse remoteMethodCall(String query);
    CommonClientResponse remoteMethodCall(String remoteMethodName,String query);
}
