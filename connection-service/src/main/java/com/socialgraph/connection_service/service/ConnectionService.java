package com.socialgraph.connection_service.service;

import java.util.*;

import com.socialgraph.connection_service.model.ConnectedUserList;
import com.socialgraph.connection_service.model.Connection;
import com.socialgraph.connection_service.model.ConnectionWithUserDTO;

public interface ConnectionService {
	String sendRequest(UUID requesterId, UUID receiverId);
    String acceptRequest(UUID requesterId, UUID receiverId);
    String rejectRequest(UUID requesterId, UUID receiverId);
    List<ConnectedUserList> getConnections(UUID userId); 
    void validateUserExists(UUID userId);
    List<ConnectionWithUserDTO> getPendingReceived(UUID userId);
    List<ConnectionWithUserDTO> getPendingSent(UUID userId);
    String removeConnection(UUID user1, UUID user2);
    String getConnectionStatus(UUID user1, UUID user2);
    List<UUID> getMutualFriends(UUID user1, UUID user2);
    int getPendingReceivedCount(UUID userId);
    String blockUser(UUID requesterId, UUID receiverId) ;
    String unblockUser(UUID requesterId, UUID receiverId);
    void deleteAllByUserId(UUID userId);
    
}
