package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.dto.RegisterRequest;
import com.taskmanagementsystem.dto.ResetPasswordRequest;
import com.taskmanagementsystem.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.taskmanagementsystem.dto.LoginRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/regist" +
            "er")
    public String register(@Valid @RequestBody RegisterRequest request) {

        return authService.register(request);
    }

    @PostMapping("/login")
    public Map<String, String> login(
            @Valid @RequestBody LoginRequest request
    ) {

        return authService.login(request);
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestBody ResetPasswordRequest request
    ) {

        return authService.resetPassword(request);
    }
}