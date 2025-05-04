package io.github.vishalmysore.a2a.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.t4a.detect.ActionCallback;

import io.github.vishalmysore.a2a.domain.Task;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import io.github.vishalmysore.a2a.domain.*;
import io.github.vishalmysore.a2a.domain.Task;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
@Log
@Service
public class FileBasedTaskController extends DyanamicTaskContoller {

    private static final String TASKS_FILE = "tasks.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void loadTasks() {
        File file = new File(TASKS_FILE);
        if (file.exists()) {
            try {
                Map<String, Task> loadedTasks = objectMapper.readValue(file, new TypeReference<>() {});
                tasks.putAll(loadedTasks);
                log.info("Tasks loaded from file.");
            } catch (IOException e) {
                log.severe("Failed to load tasks from file: " + e.getMessage());
            }
        }
    }

    public void saveTasks() {
        try {
            objectMapper.writeValue(new File(TASKS_FILE), tasks);
            log.info("Tasks saved to file.");
        } catch (IOException e) {
            log.severe("Failed to save tasks to file: " + e.getMessage());
        }
    }

    @Override
    public String cancelTask(String taskId) {
        String result = super.cancelTask(taskId);
        saveTasks();
        return result;
    }

    @Override
    public SendTaskResponse sendTask(TaskSendParams taskSendParams, ActionCallback actionCallback) {
        SendTaskResponse response = super.sendTask(taskSendParams, actionCallback);
        saveTasks();
        return response;
    }
}