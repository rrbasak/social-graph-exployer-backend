package com.socialgraph.user_service.serviceImpl;

import org.springframework.stereotype.Service;

import com.socialgraph.user_service.dto.requests.LoginRequest;
import com.socialgraph.user_service.dto.requests.RefreshTokenRequest;
import com.socialgraph.user_service.dto.responses.LoginResponse;
import com.socialgraph.user_service.dto.responses.TokenRefreshResponse;
import com.socialgraph.user_service.model.User;
import com.socialgraph.user_service.security.jwt.JwtUtils;
import com.socialgraph.user_service.service.AuthService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService{
	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtutils;

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
    	Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		} catch (AuthenticationException exception) {
			log.error("Authentication error: {}", exception);
			Map<String, Object> map = new HashMap<>();
			map.put("message", "Bad credentials");
			map.put("status", false);
			return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
		}

//      set the authentication
		SecurityContextHolder.getContext().setAuthentication(authentication);

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

//		String accessToken = jwtutils.generateTokenFromUsername(userDetails.getUsername(), userDetails.getId().toString());
//		String refreshToken = jwtutils.generateRefreshToken(userDetails.getUsername(), userDetails.getId().toString());
		
		User user = userDetails.getUser(); // Add a getter in CustomUserDetails

		String accessToken = jwtutils.generateTokenFromUsername(user.getUsername(), user.getUserId().toString());
		String refreshToken = jwtutils.generateRefreshToken(user.getUsername(), user.getUserId().toString());

		LoginResponse response = new LoginResponse();
		response.setId(user.getUserId());
		response.setUsername(user.getUsername());
		response.setEmail(user.getEmail());
		response.setAccessToken(accessToken);
		response.setRefreshToken(refreshToken);
		response.setFname(user.getFname());
		response.setLname(user.getLname());
		response.setBio(user.getBio());
		response.setProfilePicture(user.getProfilePicture());
		response.setCoverPhoto(user.getCoverPhoto());
		response.setDateOfBirth(user.getDateOfBirth());
		response.setGender(user.getGender());
		response.setLocation(user.getLocation());
		response.setInterests(user.getInterests());
		response.setCreatedAt(user.getCreatedAt());
		response.setUpdatedAt(user.getUpdatedAt());

		return ResponseEntity.ok(response);


    }

    @Override
    public ResponseEntity<?> refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (refreshToken == null || !jwtutils.validateJwtToken(refreshToken)) {
        	log.error("Invalid or expired refresh token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or expired refresh token"));
        }
        log.info("refresh token"+request.getRefreshToken());
        String username = jwtutils.getUserNameFromJwtToken(refreshToken);
        String uuid = jwtutils.getUUIDFromJwtToken(refreshToken);

        String newAccessToken = jwtutils.generateTokenFromUsername(username, uuid);
        String newRefreshToken = jwtutils.generateRefreshToken(username, uuid);

        TokenRefreshResponse response = new TokenRefreshResponse(
                newAccessToken,
                newRefreshToken,
                "Bearer"
        );

        return ResponseEntity.ok(response);
    }
}
