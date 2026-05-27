package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.dto.CreateTaskRequest;
import com.taskmanagementsystem.dto.UpdateTaskRequest;
import com.taskmanagementsystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.taskmanagementsystem.entity.Task;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public String createTask(
            @RequestBody CreateTaskRequest request
    ) {

        return taskService.createTask(request);
    }

    @GetMapping
    public List<Task> getMyTasks() {

        return taskService.getMyTasks();
    }

    @PutMapping("/{id}")
    public String updateTask(
            @PathVariable Long id,
            @RequestBody UpdateTaskRequest request
    ) {

        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteTask(
            @PathVariable Long id
    ) {

        return taskService.deleteTask(id);
    }
}