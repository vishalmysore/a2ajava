package io.github.vishalmysore.a2a.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vishalmysore.a2a.domain.*;
import io.github.vishalmysore.common.Agent;
import io.github.vishalmysore.common.AgentInfo;

import io.github.vishalmysore.common.CommonClientResponse;

import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.UUID;
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
        try {
            Message message = new Message();
            TextPart textPart = new TextPart();
            textPart.setText(query);
            message.setParts(Collections.singletonList(textPart));

            TaskSendParams params = new TaskSendParams();
            params.setMessage(message);

            params.setId(String.valueOf(UUID.randomUUID()));
            JsonRpcRequest request = createRequest("tasks/send", params);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SendTaskResponse> response = restTemplate.postForEntity(
                    getServerUrl().toURI().toString(),
                    request,
                    SendTaskResponse.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.severe("Error sending task: " + e.getMessage());
            throw e;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private JsonRpcRequest createRequest(String method, Object params) {
        return new JsonRpcRequest("2.0", method, params, UUID.randomUUID().toString());
    }

    @Override
    public CommonClientResponse remoteMethodCall(String remoteMethodName, String query) {
      return remoteMethodCall(query+" - possible action for this query is "+remoteMethodName);
    }

    @Override
    public void connect(String url, String token) {
        try {
        serverUrl = new URL(url);
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

            URL agentCardUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) agentCardUrl.openConnection();
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
