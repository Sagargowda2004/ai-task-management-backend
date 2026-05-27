package com.taskmanagementsystem.service.impl;

import com.taskmanagementsystem.entity.Task;
import com.taskmanagementsystem.entity.User;
import com.taskmanagementsystem.repository.TaskRepository;
import com.taskmanagementsystem.repository.UserRepository;
import com.taskmanagementsystem.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Value("${openai.api.key}")
    private String openAiKey;

    // TEMP IN-MEMORY CONVERSATION STORE
    // Later move to DB/Redis if needed
    private final List<Map<String, String>> conversationHistory =
            new ArrayList<>();

    @Override
    public String generateProductivityInsights(
            String userMessage
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<Task> tasks =
                taskRepository.findByUser(user);

        // BUILD TASK CONTEXT
        StringBuilder taskSummary =
                new StringBuilder();

        for (Task task : tasks) {

            taskSummary.append("""
                    
                    Task:
                    Title: %s
                    Description: %s
                    Status: %s
                    Priority: %s
                    Due Date: %s
                    
                    """.formatted(
                    task.getTitle(),
                    task.getDescription(),
                    task.getStatus(),
                    task.getPriority(),
                    task.getDueDate()
            ));
        }
        String systemPrompt = """
                
                You are TaskFlow AI — a modern AI productivity and task management assistant.
                
                Your job is to help users:
                - create tasks
                - break down goals into actionable steps
                - generate subtasks
                - prioritize work
                - improve productivity
                - organize projects
                - manage deadlines
                - identify blockers
                - suggest next actions
                - plan software projects
                - structure study/work plans
                
                You behave like a modern productivity copilot similar to:
                - Notion AI
                - Linear AI
                - Motion AI
                
                RESPONSE STYLE:
                
                - Keep responses concise and clean
                - Use short bullet points when helpful
                - Focus on actionable insights
                - Sound professional, intelligent, and modern
                - Avoid long reports
                - Avoid excessive spacing
                - Maximum 5-8 lines unless user explicitly asks for details
                - Keep responses easy to scan
                - Avoid repeating obvious task information
                - Never dump the full task list back to the user
                
                TASK CREATION BEHAVIOR:
                
                - If user provides a topic, goal, or requirement:
                  → generate clear actionable tasks
                
                - Help users break large goals into smaller tasks
                
                - Suggest realistic implementation steps
                
                - Generate practical subtasks for:
                  - software projects
                  - assignments
                  - study plans
                  - productivity workflows
                  - deployment tasks
                  - interview preparation
                
                - When possible:
                  → suggest priorities and deadlines
                
                - If user asks vague questions:
                  → guide them with structured next steps
                
                - If user asks how to create a task:
                  → explain briefly and provide a practical example
                
                - If user provides only a topic:
                  → generate:
                    - task title
                    - short description
                    - suggested priority
                    - suggested deadline
                
                TASK ANALYSIS BEHAVIOR:
                
                - Analyze user tasks intelligently
                - Identify bottlenecks and blockers
                - Detect duplicated or risky tasks
                - Recommend priorities
                - Suggest productivity improvements
                - Highlight overdue risks
                
                EMPTY TASK LIST BEHAVIOR:
                
                - If the user has no tasks and asks about:
                  - current tasks
                  - task status
                  - productivity insights
                  - pending work
                
                  → politely explain that no tasks exist yet
                  → encourage the user to create tasks
                  → suggest a few useful starter tasks
                
                - Do NOT repeatedly mention empty task lists
                  for unrelated questions
                
                IMPORTANT RULES:
                
                - If tasks are already visible:
                  → do NOT rewrite them completely
                
                - Focus on insights instead of repeating information
                
                - Avoid large paragraphs and excessive formatting
                
                - Do NOT behave like a generic chatbot
                
                - Politely reject unrelated topics such as:
                  politics, sports, celebrities, random trivia, entertainment
                
                GOOD RESPONSE EXAMPLE:
                
                • Prioritize AI integration first
                • Split frontend work into smaller milestones
                • Reserve testing time before deployment
                • No major blockers detected currently
                
                BAD RESPONSE EXAMPLE:
                
                Task:
                Description:
                Priority:
                Due Date:
                
                TASK ACCESS RULE:
                
                - You already have access to the user's current tasks below
                - Use those tasks to answer status, productivity, and planning questions
                - If user asks about current tasks:
                  → analyze the provided task list directly
                - Do NOT say:
                  "I don't have access to your tasks"
                - Do NOT ask user to provide tasks again
                - Always use the available task context intelligently
                - Avoid unnecessary blank lines
                - Keep bullet points compact
                - Use tight spacing between sections
                - Prefer short grouped responses
                - Avoid markdown headers unless necessary
                
                Current User Tasks:
                
                """ + taskSummary;

        // ADD USER MESSAGE TO MEMORY
        conversationHistory.add(
                Map.of(
                        "role", "user",
                        "content", userMessage
                )
        );

        // BUILD FINAL MESSAGES
        List<Map<String, String>> messages =
                new ArrayList<>();

        // SYSTEM MESSAGE
        messages.add(
                Map.of(
                        "role", "system",
                        "content", systemPrompt
                )
        );

        // LAST FEW CONVERSATIONS
        int start =
                Math.max(
                        0,
                        conversationHistory.size() - 10
                );

        messages.addAll(
                conversationHistory.subList(
                        start,
                        conversationHistory.size()
                )
        );

        WebClient webClient = WebClient.builder()

                .baseUrl("https://openrouter.ai/api/v1")

                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + openAiKey
                )

                .defaultHeader(
                        "HTTP-Referer",
                        "http://localhost:5173"
                )

                .defaultHeader(
                        "X-Title",
                        "TaskFlow AI Assistant"
                )

                .build();

        Map<String, Object> requestBody =
                new HashMap<>();

        requestBody.put(
                "model",
                "openai/gpt-4o-mini"
        );

        requestBody.put(
                "messages",
                messages
        );

        requestBody.put(
                "temperature",
                0.7
        );

        requestBody.put(
                "max_tokens",
                400
        );

        try {

            Map response = webClient.post()

                    .uri("/chat/completions")

                    .contentType(MediaType.APPLICATION_JSON)

                    .bodyValue(requestBody)

                    .retrieve()

                    .bodyToMono(Map.class)

                    .block();

            if (response == null) {

                return "AI service is currently unavailable.";
            }

            List choices =
                    (List) response.get("choices");

            if (choices == null || choices.isEmpty()) {

                return "No AI response generated.";
            }

            Map choice =
                    (Map) choices.get(0);

            Map message =
                    (Map) choice.get("message");

            String aiResponse =
                    message.get("content")
                            .toString();

            // SAVE AI RESPONSE TO MEMORY
            conversationHistory.add(
                    Map.of(
                            "role", "assistant",
                            "content", aiResponse
                    )
            );

            return aiResponse;

        } catch (Exception e) {

            e.printStackTrace();

            return """
                    AI service is temporarily unavailable.
                    Please try again later.
                    """;
        }
    }
}