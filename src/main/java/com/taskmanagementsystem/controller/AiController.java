package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.dto.AiRequest;
import com.taskmanagementsystem.service.AiService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/chat")
    public String chatWithAi(
            @RequestBody AiRequest request
    ) {

        return aiService.generateProductivityInsights(
                request.getMessage()
        );
    }
}