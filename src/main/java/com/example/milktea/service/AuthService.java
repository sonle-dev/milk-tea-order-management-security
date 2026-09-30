package com.example.milktea.service;

import com.example.milktea.dto.AuthResponse;
import com.example.milktea.dto.LoginRequest;
import com.example.milktea.dto.RegisterRequest;
import com.example.milktea.entity.User;

public interface AuthService {
    User register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}