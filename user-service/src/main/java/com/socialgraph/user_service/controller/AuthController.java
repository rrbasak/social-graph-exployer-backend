package com.socialgraph.user_service.controller;


import com.socialgraph.user_service.serviceImpl.AuthServiceImpl;

import com.socialgraph.user_service.dto.requests.LoginRequest;
import com.socialgraph.user_service.dto.requests.RefreshTokenRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v0/auth")
public class AuthController {
	
	@Autowired
	private AuthServiceImpl authService;


	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
		 return authService.login(loginRequest);
	}
	
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
	    return authService.refreshToken(request);
	}
	
	
}
