package com.socialgraph.user_service.service;

import org.springframework.http.ResponseEntity;

import com.socialgraph.user_service.dto.requests.LoginRequest;
import com.socialgraph.user_service.dto.requests.RefreshTokenRequest;

public interface AuthService {
	ResponseEntity<?> login(LoginRequest loginRequest);
    ResponseEntity<?> refreshToken(RefreshTokenRequest request);
}
