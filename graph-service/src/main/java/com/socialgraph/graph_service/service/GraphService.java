package com.socialgraph.graph_service.service;

import java.util.List;
import java.util.UUID;

import com.socialgraph.graph_service.model.ConnectionWithUserDTO;

public interface GraphService {
	void addFriend(UUID user1, UUID user2);
	void removeFriend(UUID user1, UUID user2);
	List<UUID> getMutualFriends(UUID user1, UUID user2);
	Integer getShortestPath(UUID user1, UUID user2);
	public List<ConnectionWithUserDTO> suggestFriends(UUID userId);
	void createNodeIfNotExists(UUID id);
	void createUserNode(UUID id);
	void deleteUserNode(UUID id);
}
