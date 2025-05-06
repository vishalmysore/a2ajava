package io.github.vishalmysore.a2a.server;

import io.github.vishalmysore.a2a.domain.SendTaskResponse;
import io.github.vishalmysore.a2a.domain.Task;
import io.github.vishalmysore.a2a.domain.TaskSendParams;
import io.github.vishalmysore.a2a.repository.TaskRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.t4a.detect.ActionCallback;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import io.github.vishalmysore.a2a.domain.Task;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
@Log
@Service
@Qualifier(TaskControllerQualifiers.DBBASED_TASK_CONTROLLER)
@ConditionalOnProperty(name = "a2a.persistence", havingValue = "database")
public class DatabaseTaskController extends DyanamicTaskContoller {

    @Autowired
    private TaskRepository taskRepository; // Assume a JPA repository for Task

    @PostConstruct
    public void loadTasks() {
        List<Task> loadedTasks = taskRepository.findAll();
        loadedTasks.forEach(task -> tasks.put(task.getId(), task));
        log.info("Tasks loaded from database.");
    }

    public void saveTask(Task task) {
        taskRepository.save(task);
        log.info("Task saved to database: " + task.getId());
    }

    @Override
    public String cancelTask(String taskId) {
        String result = super.cancelTask(taskId);
        Optional.ofNullable(tasks.get(taskId)).ifPresent(this::saveTask);
        return result;
    }

    @Override
    public SendTaskResponse sendTask(TaskSendParams taskSendParams, ActionCallback actionCallback) {
        SendTaskResponse response = super.sendTask(taskSendParams, actionCallback);
        saveTask(tasks.get(taskSendParams.getId()));
        return response;
    }
}