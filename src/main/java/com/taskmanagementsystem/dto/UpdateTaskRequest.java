package com.taskmanagementsystem.dto;

import com.taskmanagementsystem.entity.Priority;
import com.taskmanagementsystem.entity.TaskStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateTaskRequest {

    private String title;

    private String description;

    private Priority priority;

    private LocalDate dueDate;

    private TaskStatus status;
}