package com.taskmanagementsystem.service.impl;

import com.taskmanagementsystem.dto.CreateTaskRequest;
import com.taskmanagementsystem.entity.Task;
import com.taskmanagementsystem.entity.User;
import com.taskmanagementsystem.exception.ResourceNotFoundException;
import com.taskmanagementsystem.repository.TaskRepository;
import com.taskmanagementsystem.repository.UserRepository;
import com.taskmanagementsystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import com.taskmanagementsystem.dto.UpdateTaskRequest;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public String createTask(CreateTaskRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setStatus(request.getStatus());

        task.setCreatedAt(LocalDateTime.now());

        task.setUser(user);

        taskRepository.save(task);

        return "Task Created Successfully";
    }

    @Override
    public List<Task> getMyTasks() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        return taskRepository.findByUser(user);
    }

    @Override
    public String updateTask(
            Long taskId,
            UpdateTaskRequest request
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found"
                        ));

        // OWNERSHIP CHECK
        if (!task.getUser().getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "You cannot update this task"
            );
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setStatus(request.getStatus());

        taskRepository.save(task);

        return "Task Updated Successfully";
    }

    @Override
    public String deleteTask(Long taskId) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));

        // OWNERSHIP CHECK
        if (!task.getUser().getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "You cannot delete this task"
            );
        }

        taskRepository.delete(task);

        return "Task Deleted Successfully";
    }
}