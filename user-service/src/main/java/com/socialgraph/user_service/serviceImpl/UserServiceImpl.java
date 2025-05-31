package com.socialgraph.user_service.serviceImpl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.socialgraph.user_service.dto.requests.UserUpdateRequest;
import com.socialgraph.user_service.model.User;
import com.socialgraph.user_service.model.UserInterest;
import com.socialgraph.user_service.repository.UserInterestRepository;
import com.socialgraph.user_service.repository.UserRepository;
import com.socialgraph.user_service.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RestTemplate restTemplate;

	
//	@Value("${graph.service.url}")
//	private String graphServiceUrl;
	
	private final String GRAPH_SERVICE_URL = "http://GRAPH-SERVICE/api/v0/graph/";
	
	private final String CONNECTION_SERVICE_URL = "http://CONNECTION-SERVICE/api/v0/connection/";
	
	
	public User createUser(User user) {
		String hashedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(hashedPassword);
		User savedUser = userRepository.save(user);

	
	    try {
	        restTemplate.postForObject(
	        		GRAPH_SERVICE_URL + "create" + "?userId=" + savedUser.getUserId(),
	            null,
	            Void.class
	        );
	    } catch (Exception e) {
	        
	        System.err.println("Failed to create user node in graph service: " + e.getMessage());
	    }

	    return savedUser;
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserById(UUID id) {
//		UserWithMutulaFriends userM = restTemplate.delete(CONNECTION_SERVICE_URL + "/cleanup/user/" + id);
		return userRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + id));
	}
	
	public User deleteUserById(UUID id) {
		User user = userRepository.findById(id)
	            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + id));
	    
	    userRepository.deleteById(id);  
	    
	    try {
	        restTemplate.delete(CONNECTION_SERVICE_URL + "/cleanup/user/" + id);
	        restTemplate.delete(GRAPH_SERVICE_URL + "/delete-node/" + id);
	    } catch (Exception e) {
	        
	        e.printStackTrace();
	    }

	    
	    return user; 
	}
	
	public User updateUser(UUID userId, UserUpdateRequest updateRequest) {
	    User user = userRepository.findById(userId)
	        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + userId));

	    if (updateRequest.getFname() != null) user.setFname(updateRequest.getFname());
	    if (updateRequest.getLname() != null) user.setLname(updateRequest.getLname());
	    if (updateRequest.getEmail() != null) user.setEmail(updateRequest.getEmail());
	    if (updateRequest.getBio() != null) user.setBio(updateRequest.getBio());
	    if (updateRequest.getProfilePicture() != null) user.setProfilePicture(updateRequest.getProfilePicture());
	    if (updateRequest.getCoverPhoto() != null) user.setCoverPhoto(updateRequest.getCoverPhoto());
	    if (updateRequest.getDateOfBirth() != null) user.setDateOfBirth(updateRequest.getDateOfBirth());
	    if (updateRequest.getGender() != null) user.setGender(updateRequest.getGender());
	    if (updateRequest.getLocation() != null) user.setLocation(updateRequest.getLocation());
	    if (updateRequest.getInterests() != null) user.setInterests(updateRequest.getInterests());

	    return userRepository.save(user);
	}
	
	public List<User> searchUsers(String query, UUID userId) {
		return userRepository.searchUsersByName(query, userId);
	}
	
	public List<User> searchUsers(String name, List<UUID> excludeIds) {
        return userRepository.searchUsersByNameExcludingIds(name, excludeIds);
    }

}
