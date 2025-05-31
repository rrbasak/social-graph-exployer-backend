package com.socialgraph.graph_service.serviceImpl;

import com.socialgraph.graph_service.dto.responses.UserResponse;
import com.socialgraph.graph_service.model.ConnectionWithUserDTO;
import com.socialgraph.graph_service.model.UserNode;
import com.socialgraph.graph_service.repository.UserNodeRepository;
import com.socialgraph.graph_service.service.GraphService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GraphServiceImpl implements GraphService {

	private final String USER_SERVICE_URL = "http://USER-SERVICE/api/v0/users/public/get";
	private final String CONNECTION_SERVICE_URL = "http://CONNECTION-SERVICE/api/v0/connection";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private UserNodeRepository userRepo;

	public void addFriend(UUID user1, UUID user2) {
		UserNode u1 = userRepo.findById(user1).orElse(new UserNode(user1));
		UserNode u2 = userRepo.findById(user2).orElse(new UserNode(user2));
		u1.addFriend(u2);
		u2.addFriend(u1);
		userRepo.save(u1);
		userRepo.save(u2);
	}

//    public void removeFriend(UUID user1, UUID user2) {
//        Optional<UserNode> optU1 = userRepo.findById(user1);
//        Optional<UserNode> optU2 = userRepo.findById(user2);
//        if (optU1.isPresent() && optU2.isPresent()) {
//            UserNode u1 = optU1.get();
//            UserNode u2 = optU2.get();
//            u1.removeFriend(u2);
//            u2.removeFriend(u1);
//            userRepo.save(u1);
//            userRepo.save(u2);
//        }
//    }

	public void removeFriend(UUID user1, UUID user2) {
		Optional<UserNode> optU1 = userRepo.findById(user1);
		Optional<UserNode> optU2 = userRepo.findById(user2);
		if (optU1.isPresent() && optU2.isPresent()) {
			userRepo.removeFriendship(user1, user2);
			userRepo.removeFriendship(user2, user1);
		}
	}

	public List<UUID> getMutualFriends(UUID user1, UUID user2) {
		return userRepo.findMutualFriends(user1, user2);
	}

	public Integer getShortestPath(UUID user1, UUID user2) {
		return userRepo.findShortestPathLength(user1, user2);
	}

	public List<ConnectionWithUserDTO> suggestFriends(UUID userId) {
		List<UUID> listOfIds = userRepo.suggestFriends(userId);
		List<ConnectionWithUserDTO> result = listOfIds.stream().filter(id -> {
			String status = restTemplate
					.getForObject(CONNECTION_SERVICE_URL + "/status?user1=" + userId + "&user2=" + id, String.class);
			return "NONE".equalsIgnoreCase(status);
		}).map(id -> {
			UserResponse requester = restTemplate.getForObject(USER_SERVICE_URL + "/" + id, UserResponse.class);
			ConnectionWithUserDTO dto = new ConnectionWithUserDTO();
			dto.setFname(requester.getFname());
			dto.setLname(requester.getLname());
			dto.setUsername(requester.getUsername());
			dto.setBio(requester.getBio());
			dto.setProfilePicture(requester.getProfilePicture());
			dto.setCoverPhoto(requester.getCoverPhoto());
			dto.setGender(requester.getGender());
			dto.setInterests(requester.getInterests());
			dto.setUserId(requester.getUserId());
			return dto;
		}).collect(Collectors.toList());
		return result;
	}

	public void createNodeIfNotExists(UUID id) {
		if (!userRepo.existsById(id)) {
			userRepo.save(new UserNode(id));
		}
	}

	public void createUserNode(UUID userId) {
		if (!userRepo.existsById(userId)) {
			UserNode userNode = new UserNode(userId);
			userRepo.save(userNode);
		}
	}

	public void deleteUserNode(UUID id) {
		userRepo.deleteUserNodeAndRelationships(id);
	}
}
