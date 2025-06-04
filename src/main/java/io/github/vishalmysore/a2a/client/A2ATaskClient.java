package io.github.vishalmysore.a2a.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vishalmysore.a2a.domain.*;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * A client for interacting with the A2A server. This might be only needed when you wnat to
 * "infuse" Ai into your application in the sense call the A2A server from your application
 * and get the response back.
 * For normal client server application you can use client built in angular or react or any other web framework
 */
@Log
@Getter

public class A2ATaskClient {
    private static final String DEFAULT_BASE_URL = "http://localhost:8080/rpc";
    private final String baseUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final List<Task> pendingTasks;
    private final List<Task> completedTasks;

    public A2ATaskClient() {
        this(DEFAULT_BASE_URL);
    }

    public A2ATaskClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.pendingTasks = Collections.synchronizedList(new ArrayList<>());
        this.completedTasks = Collections.synchronizedList(new ArrayList<>());
    }

    private JsonRpcRequest createRequest(String method, Object params) {
        return new JsonRpcRequest("2.0", method, params, UUID.randomUUID().toString());
    }

    public SendTaskResponse sendTask(String prompt) {
        try {
            Message message = new Message();
            TextPart textPart = new TextPart();
            textPart.setText(prompt);
            message.setParts(Collections.singletonList(textPart));

            TaskSendParams params = new TaskSendParams();
            params.setMessage(message);
            params.setId(String.valueOf(UUID.randomUUID()));
            JsonRpcRequest request = createRequest("tasks/send", params);
            ResponseEntity<SendTaskResponse> response = restTemplate.postForEntity(
                    baseUrl,
                    request,
                    SendTaskResponse.class
            );

            SendTaskResponse task = response.getBody();
            if (task != null) {
                pendingTasks.add(task.getResult());
            }
            return task;
        } catch (HttpClientErrorException e) {
            log.severe("Error sending task: " + e.getMessage());
            throw e;
        }
    }

    public Task getTask(String taskId, Integer historyLength) {
        try {
            TaskQueryParams params = new TaskQueryParams();
            params.setId(taskId);
            params.setHistoryLength(historyLength);

            JsonRpcRequest request = createRequest("tasks/get", params);
            ResponseEntity<Task> response = restTemplate.postForEntity(
                    baseUrl,
                    request,
                    Task.class
            );

            Task task = response.getBody();
            if (task != null && task.getStatus().equals("completed")) {
                pendingTasks.removeIf(t -> t.getId().equals(taskId));
                completedTasks.add(task);
            }
            return task;
        } catch (HttpClientErrorException e) {
            log.severe("Error getting task: " + e.getMessage());
            throw e;
        }
    }

    public boolean cancelTask(String taskId) {
        try {
            TaskCancelParams params = new TaskCancelParams();
            params.setId(taskId);

            JsonRpcRequest request = createRequest("tasks/cancel", params);
            restTemplate.postForEntity(baseUrl, request, Object.class);

            pendingTasks.removeIf(t -> t.getId().equals(taskId));
            return true;
        } catch (HttpClientErrorException e) {
            log.severe("Error cancelling task: " + e.getMessage());
            throw e;
        }
    }

    public List<Task> getPendingTasks() {
        return new ArrayList<>(pendingTasks);
    }

    public List<Task> getCompletedTasks() {
        return new ArrayList<>(completedTasks);
    }
}