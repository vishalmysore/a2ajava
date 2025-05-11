package io.github.vishalmysore.a2a.client;

import io.github.vishalmysore.a2a.domain.*;
import io.github.vishalmysore.a2a.server.JsonRpcController;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.util.*;

/**
 * A client for interacting with the A2A server. This might be only needed when you wnat to
 * "infuse" Ai into your application in the sense call the A2A server from your application
 * and get the response back.
 * For normal client server application you can use client built in angular or react or any other web framework
 */
@Log
@Service
public class LocalA2ATaskClient {

    @Setter
    @Autowired
    private JsonRpcController jrc ;


    public LocalA2ATaskClient() {

    }


    private JsonRpcRequest createRequest(String method, Object params) {
        return new JsonRpcRequest("2.0", method, params, UUID.randomUUID().toString());
    }

    public Task sendTask(String prompt) {
        try {
            Message message = new Message();
            TextPart textPart = new TextPart();
            textPart.setText(prompt);
            message.setParts(Collections.singletonList(textPart));

            TaskSendParams params = new TaskSendParams();
            params.setId(String.valueOf(UUID.randomUUID()));
            params.setMessage(message);

            JsonRpcRequest request = createRequest("tasks/send", params);
            Task task = ((SendTaskResponse)jrc.handleRpc(request)).getResult();

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
            ResponseEntity<Task> response = (ResponseEntity<Task>)jrc.handleRpc(request);

            Task task = response.getBody();

            return task;
        } catch (HttpClientErrorException e) {
            log.severe("Error getting task: " + e.getMessage());
            throw e;
        }
    }

    public Task sendFileTask(String filePath) {
        try {
            Message message = new Message();
            FilePart filePart = new FilePart();
            FileContent content = new FileContent();
            filePart.setFile(content);
            message.setParts(Collections.singletonList(filePart));

            TaskSendParams params = new TaskSendParams();
            params.setId(String.valueOf(UUID.randomUUID()));
            params.setMessage(message);

            JsonRpcRequest request = createRequest("tasks/send", params);
            Task task = ((SendTaskResponse)jrc.handleRpc(request)).getResult();

            return task;
        } catch (HttpClientErrorException e) {
            log.severe("Error sending file task: " + e.getMessage());
            throw e;
        }
    }

    public Task sendFileTask(FilePart filePart) {
        try {
            Message message = new Message();

            message.setParts(Collections.singletonList(filePart));

            TaskSendParams params = new TaskSendParams();
            params.setId(String.valueOf(UUID.randomUUID()));
            params.setMessage(message);

            JsonRpcRequest request = createRequest("tasks/send", params);
            Task task = ((SendTaskResponse)jrc.handleRpc(request)).getResult();

            return task;
        } catch (HttpClientErrorException e) {
            log.severe("Error sending file task: " + e.getMessage());
            throw e;
        }
    }

    public Task sendFileTask(TextPart textPart,FilePart filePart) {
        try {
            Message message = new Message();

            List<Part> partList = new ArrayList<>();
            partList.add(textPart);
            partList.add(filePart);
            message.setParts(partList);

            TaskSendParams params = new TaskSendParams();
            params.setId(String.valueOf(UUID.randomUUID()));
            params.setMessage(message);

            JsonRpcRequest request = createRequest("tasks/send", params);
            Task task = ((SendTaskResponse)jrc.handleRpc(request)).getResult();

            return task;
        } catch (HttpClientErrorException e) {
            log.severe("Error sending file task: " + e.getMessage());
            throw e;
        }
    }

    public Task sendDataTask(Map<String,Object> data) {
        try {
            Message message = new Message();
            DataPart dataPart = new DataPart();
            dataPart.setData(data);
            message.setParts(Collections.singletonList(dataPart));

            TaskSendParams params = new TaskSendParams();
            params.setId(String.valueOf(UUID.randomUUID()));
            params.setMessage(message);

            JsonRpcRequest request = createRequest("tasks/send", params);
            Task task = ((SendTaskResponse)jrc.handleRpc(request)).getResult();

            return task;
        } catch (HttpClientErrorException e) {
            log.severe("Error sending data task: " + e.getMessage());
            throw e;
        }
    }

    public Task sendMultiPartTask(List<Object> parts) {
        try {
            Message message = new Message();
            List<Object> messageParts = new ArrayList<>();

            for (Object part : parts) {
                if (part instanceof String) {
                    TextPart textPart = new TextPart();
                    textPart.setText((String) part);
                    messageParts.add(textPart);
                } else if (part instanceof File) {
                    FilePart filePart = new FilePart();
                    FileContent content = new FileContent();
                    filePart.setFile(content);
                    messageParts.add(filePart);
                } else {
                    DataPart dataPart = new DataPart();
                    // dataPart.setData(part);
                    messageParts.add(dataPart);
                }
            }

            //   message.setParts(messageParts);

            TaskSendParams params = new TaskSendParams();
            params.setId(String.valueOf(UUID.randomUUID()));
            params.setMessage(message);

            JsonRpcRequest request = createRequest("tasks/send", params);
            Task task = ((SendTaskResponse)jrc.handleRpc(request)).getResult();

            return task;
        } catch (HttpClientErrorException e) {
            log.severe("Error sending multi-part task: " + e.getMessage());
            throw e;
        }
    }
}
