package com.socialgraph.user_service.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.socialgraph.user_service.dto.requests.UserUpdateRequest;
import com.socialgraph.user_service.dto.responses.PublicUserResponse;
import com.socialgraph.user_service.model.User;
import com.socialgraph.user_service.model.UserInterest;
import com.socialgraph.user_service.serviceImpl.UserServiceImpl;

@RestController
@RequestMapping("/api/v0/users")
public class UserController {

	@Autowired
	UserServiceImpl userService;

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		return ResponseEntity.ok(userService.createUser(user));
	}

	@GetMapping("/get")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<User>> getAll() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@GetMapping("/get/{id}")
//	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<User> getById(@PathVariable UUID id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@GetMapping("/public/get/{id}")
    public ResponseEntity<PublicUserResponse> getPublicById(@PathVariable UUID id) {
    	User user = userService.getUserById(id);
        return ResponseEntity.ok(new PublicUserResponse(
            user.getUserId(), user.getFname(),user.getLname(), user.getUsername(), user.getProfilePicture(),user.getBio(),user.getCoverPhoto(),user.getGender(),user.getInterests()
        ));
    }

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> deleteById(@PathVariable UUID id) {
		userService.deleteUserById(id);
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "User deleted successfully"));
	}

	@PutMapping("/update/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> updateUser(@PathVariable("id") UUID id, @RequestBody UserUpdateRequest updateRequest) {
		User updatedUser = userService.updateUser(id, updateRequest);
		return ResponseEntity.ok(Map.of("message", "User updated successfully", "user", updatedUser));
	}

	@GetMapping("/search")
	public ResponseEntity<List<User>> searchUsers(@RequestParam("name") String query, @RequestParam("id") UUID userId) {
		List<User> users = userService.searchUsers(query, userId);
		return ResponseEntity.ok(users);
	}

	@GetMapping("/searchByName")
	public List<User> searchUsers(@RequestParam String name, @RequestParam(required = false) List<UUID> excludeIds) {
		if (excludeIds == null)
			excludeIds = new ArrayList<>();
		return userService.searchUsers(name, excludeIds);
	}

}
