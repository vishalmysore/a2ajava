package io.github.vishalmysore.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vishalmysore.Message;
import io.github.vishalmysore.Task;
import io.github.vishalmysore.TaskSendParams;
import io.github.vishalmysore.TextPart;
import lombok.extern.java.Log;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.UUID;

@Log
public class TaskClient {

    private static final String BASE_URL = "http://localhost:8080"; // Replace with your server's URL
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws JsonProcessingException {
        // Send a task
        String taskId = UUID.randomUUID().toString();
        Message message = new Message();
        message.setRole("user");
        TextPart textPart = new TextPart();
        textPart.setText("Book a flight from New York to Los Angeles on 2024-05-10");
        message.setParts(Collections.singletonList(textPart));

        TaskSendParams sendParams = new TaskSendParams();
        sendParams.setId(taskId);
        sendParams.setMessage(message);

        Task sentTask = sendTask(sendParams);
        log.info("Sent Task: " + objectMapper.writeValueAsString(sentTask));

        // Get the task
        Task retrievedTask = getTask(taskId, 0);
        log.info("Retrieved Task: " + objectMapper.writeValueAsString(retrievedTask));

        // Send another task to check the input required
        String taskId2 = UUID.randomUUID().toString();
        Message message2 = new Message();
        message2.setRole("user");
        TextPart textPart2 = new TextPart();
        textPart2.setText("Change the date of the ticket");
        message2.setParts(Collections.singletonList(textPart2));

        TaskSendParams sendParams2 = new TaskSendParams();
        sendParams2.setId(taskId2);
        sendParams2.setMessage(message2);

        Task sentTask2 = sendTask(sendParams2);
        log.info("Sent Task: " + objectMapper.writeValueAsString(sentTask2));

        Task retrievedTask2 = getTask(taskId2, 0);
        log.info("Retrieved Task: " + objectMapper.writeValueAsString(retrievedTask2));

        //send the new date
        Message newMessage = new Message();
        newMessage.setRole("user");
        TextPart newTextPart = new TextPart();
        newTextPart.setText("2024-05-12");
        newMessage.setParts(Collections.singletonList(newTextPart));
        TaskSendParams updateParams = new TaskSendParams();
        updateParams.setId(taskId2);
        updateParams.setMessage(newMessage);
        Task updatedTask = sendTask(updateParams);
        log.info("Updated task " + objectMapper.writeValueAsString(updatedTask));

        //get the task
        Task getUpdatedTask = getTask(taskId2, 0);
        log.info("Get updated Task: " + objectMapper.writeValueAsString(getUpdatedTask));

    }

    public static Task sendTask(TaskSendParams sendParams) throws JsonProcessingException {
        String url = BASE_URL + "/tasks/send";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = objectMapper.writeValueAsString(sendParams);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
        try {
            ResponseEntity<Task> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Task.class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            log.severe("Error sending task: " + e.getResponseBodyAsString());
            throw e; // Re-throw the exception to be handled by the caller
        }
    }

    public static Task getTask(String taskId, int historyLength) throws JsonProcessingException {
        String url = BASE_URL + "/tasks/get";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("id", taskId)
                .queryParam("historyLength", historyLength);
        try {
            ResponseEntity<Task> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    null, // No body for GET request
                    Task.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            log.severe("Error getting task: " + e.getResponseBodyAsString());
            throw e; // Re-throw the exception to be handled by the caller
        }
    }
}