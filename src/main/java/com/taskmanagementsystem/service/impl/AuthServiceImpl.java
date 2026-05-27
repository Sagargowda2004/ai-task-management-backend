package com.taskmanagementsystem.service.impl;

import com.taskmanagementsystem.dto.LoginRequest;
import com.taskmanagementsystem.dto.RegisterRequest;
import com.taskmanagementsystem.entity.User;
import com.taskmanagementsystem.repository.UserRepository;
import com.taskmanagementsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.taskmanagementsystem.service.JwtService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Override
    public String register(RegisterRequest request) {

        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already exists";
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);

        return "User Registered Successfully";
    }

    @Override
    public String login(LoginRequest request) {

        Optional<User> optionalUser =
                userRepository.findByEmail(request.getEmail());

        if(optionalUser.isEmpty()) {
            return "User not found";
        }

        User user = optionalUser.get();

        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            return "Invalid Password";
        }

        return jwtService.generateToken(user.getEmail());
    }
}