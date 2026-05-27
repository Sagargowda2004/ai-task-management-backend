package com.taskmanagementsystem.dto;

import com.taskmanagementsystem.entity.Priority;
import com.taskmanagementsystem.entity.TaskStatus;
import lombok.Data;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateTaskRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    @NotNull(message = "Status is required")
    private TaskStatus status;
}