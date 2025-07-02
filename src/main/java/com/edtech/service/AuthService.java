package com.edtech.service;

import com.edtech.dto.AuthRequest;
import com.edtech.dto.AuthResponse;
import com.edtech.dto.RegisterRequest;

public interface AuthService {
	String register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest request);
}
