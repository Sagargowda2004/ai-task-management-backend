package com.taskmanagementsystem.service;

import com.taskmanagementsystem.dto.CreateTaskRequest;
import com.taskmanagementsystem.dto.UpdateTaskRequest;
import com.taskmanagementsystem.entity.Task;

import java.util.List;

public interface TaskService {

    String createTask(CreateTaskRequest request);
    List<Task> getMyTasks();
    String updateTask(
            Long taskId,
            UpdateTaskRequest request
    );
    String deleteTask(Long taskId);
}