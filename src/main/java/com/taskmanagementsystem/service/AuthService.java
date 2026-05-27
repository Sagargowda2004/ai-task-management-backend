package com.taskmanagementsystem.service;

import com.taskmanagementsystem.dto.LoginRequest;
import com.taskmanagementsystem.dto.RegisterRequest;

import java.util.Map;

public interface AuthService {

    String register(RegisterRequest request);

    Map<String, String> login(
            LoginRequest request
    );
}