package com.taskmanagementsystem.service;

import com.taskmanagementsystem.dto.LoginRequest;
import com.taskmanagementsystem.dto.RegisterRequest;
import com.taskmanagementsystem.dto.ResetPasswordRequest;

import java.util.Map;

public interface AuthService {

    String register(RegisterRequest request);

    Map<String, String> login(
            LoginRequest request
    );

    String resetPassword(
            ResetPasswordRequest request
    );
}