package io.github.vishalmysore.a2a.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.common.Agent;
import io.github.vishalmysore.common.AgentInfo;
import io.github.vishalmysore.common.CommonClientRequest;
import io.github.vishalmysore.common.CommonClientResponse;
import io.github.vishalmysore.mcp.domain.JSONRPCResponse;
import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Log
public class A2AAgent implements Agent {

    private AgentCard agentCard;
    private ObjectMapper mapper ;
    private URL serverUrl;
    public static final String WELL_KNOWN_PATH = ".well-known";
    public static final String AGENT_PATH ="/agent.json";

    private String type ="a2a";


    public A2AAgent() {
        this.agentCard = null;
        this.serverUrl = null;
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public CommonClientResponse remoteMethodCall(String query) {
      return null;
    }

    @Override
    public CommonClientResponse remoteMethodCall(String remoteMethodName, String query) {
      return null;
    }

    @Override
    public void connect(String url, String token) {
        if(!url.endsWith(".json")) {
            if (url.endsWith("/")) {
                url += WELL_KNOWN_PATH + AGENT_PATH;
            } else {
                url += "/"+WELL_KNOWN_PATH + AGENT_PATH;
            }
        } else {
            if (!url.contains(WELL_KNOWN_PATH)) {
                throw new IllegalArgumentException("URL must end with " + WELL_KNOWN_PATH + AGENT_PATH);
            }
        }
        try {
            serverUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed to connect: HTTP " + conn.getResponseCode());
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }


                this.agentCard = mapper.readValue(response.toString(), AgentCard.class);
            }

        } catch (IOException e) {
            log.severe("Failed to connect to agent server: " + e.getMessage());
            throw new RuntimeException("Connection failed", e);
        }
    }

    @Override
    public void disconnect() {
        this.agentCard = null;
        this.serverUrl = null;

    }

    @Override
    public AgentInfo getInfo() {
        return agentCard;
    }

    @Override
    public boolean isConnected() {
        return agentCard != null && agentCard.getName() != null;
    }

    @Override
    public URL getServerUrl() {
        return serverUrl;
    }


    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
