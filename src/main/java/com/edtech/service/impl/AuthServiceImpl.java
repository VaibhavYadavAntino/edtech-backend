package com.edtech.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edtech.dto.AuthRequest;
import com.edtech.dto.AuthResponse;
import com.edtech.dto.RegisterRequest;
import com.edtech.entity.User;
import com.edtech.enums.UserRole;
import com.edtech.repository.UserRepository;
import com.edtech.service.AuthService;
import com.edtech.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Override
	public String register(RegisterRequest request) {
	    if (userRepository.findByEmailOrPhoneNumber(request.getEmail(), request.getEmail()).isPresent()) {
	        throw new RuntimeException("User with email " + request.getEmail() + " already exists.");
	    }

	    User user = User.builder()
	            .name(request.getFullName())
	            .email(request.getEmail())
	            .phoneNumber(request.getPhoneNumber())
	            .password(passwordEncoder.encode(request.getPassword()))
	            .role(UserRole.STUDENT) // Default to STUDENT
	            .build();
	    
	    userRepository.save(user);	
	    return "User registered successfully";
	}

	@Override
	public AuthResponse authenticate(AuthRequest request) {
		User user = userRepository.findByEmailOrPhoneNumber(request.getLogin(), request.getLogin())
				.orElseThrow(() -> new RuntimeException("Invalid credentials"));
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}
		String token = jwtUtil.generateToken(user.getEmail());
		return new AuthResponse(token, user.getRole().name());
	}
}
