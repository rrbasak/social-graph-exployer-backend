package com.socialgraph.connection_service.serviceImple;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.socialgraph.connection_service.dto.responses.ConnectedUserResponse;
import com.socialgraph.connection_service.dto.responses.UserResponse;
import com.socialgraph.connection_service.model.ConnectedUserList;
import com.socialgraph.connection_service.model.Connection;
import com.socialgraph.connection_service.model.ConnectionStatus;
import com.socialgraph.connection_service.model.ConnectionWithUserDTO;
import com.socialgraph.connection_service.model.UserDTO;
import com.socialgraph.connection_service.repository.ConnectionRepository;
import com.socialgraph.connection_service.service.ConnectionService;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ConnectionServiceImpl implements ConnectionService {

	@Autowired
	ConnectionRepository connectionRepository;

	@Autowired
	private RestTemplate restTemplate;

	private final String USER_SERVICE_URL = "http://USER-SERVICE/api/v0/users/public/get";
	private final String USER_GET_SERVICE_URL = "http://USER-SERVICE/api/v0/users/get";
	
	private final String GRAPH_SERVICE_URL = "http://GRAPH-SERVICE/api/v0/graph/sync";
	
//	@Value("${graph.service.url}")
//    private String graphServiceUrl;
	
	
	
	public void validateUserExists(UUID userId) {
		try {
			ResponseEntity<UserResponse> response = restTemplate.getForEntity(USER_SERVICE_URL + "/" + userId,
					UserResponse.class);

			if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
				throw new RuntimeException("User not found: " + userId);
			}

		} catch (HttpClientErrorException e) {
			throw new RuntimeException("User not found: " + userId);
		}
	}
	
	

	@Override
	public String sendRequest(UUID requesterId, UUID receiverId) {
		validateUserExists(requesterId);
		validateUserExists(receiverId);
		if (requesterId.equals(receiverId)) {
			return "Cannot send request to yourself.";
		}

		// Check existing connection
		Optional<Connection> existing = connectionRepository.findByRequesterIdAndReceiverIdOrReceiverIdAndRequesterId(
				requesterId, receiverId, requesterId, receiverId);

		if (existing.isPresent()) {
			Connection conn = existing.get();
			if (conn.getStatus() == ConnectionStatus.ACCEPTED)
				return "You are already connected.";
			if (conn.getStatus() == ConnectionStatus.PENDING)
				return "Friend request already sent.";
			if (conn.getStatus() == ConnectionStatus.REJECTED) {
				conn.setStatus(ConnectionStatus.PENDING);
				connectionRepository.save(conn);
				return "Friend request re-sent.";
			}
		}

		Connection newConn = new Connection();
		newConn.setRequesterId(requesterId);
		newConn.setReceiverId(receiverId);
		newConn.setStatus(ConnectionStatus.PENDING);
		connectionRepository.save(newConn);

		return "Friend request sent.";
	}

	@Override
	public String acceptRequest(UUID requesterId, UUID receiverId) {
		validateUserExists(requesterId);
		validateUserExists(receiverId);
		Optional<Connection> conn = connectionRepository.findByRequesterIdAndReceiverId(requesterId, receiverId);
		if (conn.isEmpty())
			return "Request not found.";

		Connection c = conn.get();
		if (c.getStatus() == ConnectionStatus.REJECTED) {
			return "This connection request was already rejected and cannot be accepted.";
		}

		if (c.getStatus() == ConnectionStatus.ACCEPTED) {
			return "This connection request has already been accepted.";
		}

		c.setStatus(ConnectionStatus.ACCEPTED);
		connectionRepository.save(c);
		Map<String, String> payload = Map.of(
                "requesterId", requesterId.toString(),
                "receiverId", receiverId.toString(),
                "action", "CONNECT"
            );
		 try {
             restTemplate.postForEntity(GRAPH_SERVICE_URL, payload, String.class);
         } catch (Exception e) {
             e.printStackTrace(); 
         }
		return "Request accepted.";
	}

	@Override
	public String rejectRequest(UUID requesterId, UUID receiverId) {
		validateUserExists(requesterId);
		validateUserExists(receiverId);
		Optional<Connection> conn = connectionRepository.findByRequesterIdAndReceiverId(requesterId, receiverId);
		if (conn.isEmpty())
			return "Request not found.";

		Connection c = conn.get();
		c.setStatus(ConnectionStatus.REJECTED);
		connectionRepository.save(c);
		return "Request rejected.";
	}

	@Override
	public List<ConnectedUserList> getConnections(UUID userId) {
		validateUserExists(userId);
		List<Connection> connections = connectionRepository.findAllByStatusAndRequesterIdOrStatusAndReceiverId(ConnectionStatus.ACCEPTED,userId, ConnectionStatus.ACCEPTED,userId);
		List<UUID> connectedIds =  connections.stream().map(conn -> {
			return conn.getRequesterId().equals(userId) ? conn.getReceiverId() : conn.getRequesterId();
		}).toList();
		
		List<ConnectedUserList> result = connectedIds.stream().map(id -> {
	        ConnectedUserResponse requester = restTemplate.getForObject(USER_GET_SERVICE_URL + "/" + id,ConnectedUserResponse.class);
	        log.info("here"+requester);
	        ConnectedUserList dto = new ConnectedUserList();
	        
	        dto.setFname(requester.getFname());
	        dto.setLname(requester.getLname());
	        dto.setUsername(requester.getUsername());
	        dto.setBio(requester.getBio());
	        dto.setProfilePicture(requester.getProfilePicture());
	        dto.setCoverPhoto(requester.getCoverPhoto());
	        dto.setGender(requester.getGender());
	        dto.setInterests(requester.getInterests());
	        dto.setDateOfBirth(requester.getDateOfBirth());
	        dto.setLocation(requester.getLocation());
	        dto.setCreatedAt(requester.getCreatedAt());
	        dto.setUserId(requester.getUserId());
	        return dto;
	    }).collect(Collectors.toList());
		
		ConnectedUserResponse requester = restTemplate.getForObject(USER_GET_SERVICE_URL + "/" + userId,ConnectedUserResponse.class);
        log.info("here"+requester);
        ConnectedUserList dto = new ConnectedUserList();
        
        dto.setFname(requester.getFname());
        dto.setLname(requester.getLname());
        dto.setUsername(requester.getUsername());
        dto.setBio(requester.getBio());
        dto.setProfilePicture(requester.getProfilePicture());
        dto.setCoverPhoto(requester.getCoverPhoto());
        dto.setGender(requester.getGender());
        dto.setInterests(requester.getInterests());
        dto.setDateOfBirth(requester.getDateOfBirth());
        dto.setLocation(requester.getLocation());
        dto.setCreatedAt(requester.getCreatedAt());
        dto.setUserId(requester.getUserId());
        
        result.add(dto);
		return result;
	}

	
	
	@Override
	public List<ConnectionWithUserDTO> getPendingReceived(UUID userId) {
	    validateUserExists(userId);
	    List<Connection> pendingConnections =  connectionRepository.findByReceiverIdAndStatus(userId, ConnectionStatus.PENDING);
	    List<ConnectionWithUserDTO> result = pendingConnections.stream().map(conn -> {
//	        UserDTO requester = restTemplate.getForObject(USER_GET_SERVICE_URL+ "/" + conn.getRequesterId(), UserDTO.class);
	        UserResponse requester = restTemplate.getForObject(USER_SERVICE_URL + "/" + conn.getRequesterId(),
					UserResponse.class);
	        log.info("here"+requester);
	        ConnectionWithUserDTO dto = new ConnectionWithUserDTO();
	        BeanUtils.copyProperties(conn, dto);  
	        
	        
	        dto.setFname(requester.getFname());
	        dto.setLname(requester.getLname());
	        dto.setUsername(requester.getUsername());
	        dto.setBio(requester.getBio());
	        dto.setProfilePicture(requester.getProfilePicture());
	        dto.setCoverPhoto(requester.getCoverPhoto());
	        dto.setGender(requester.getGender());
	        dto.setInterests(requester.getInterests());
	        dto.setStatus(requester.getStatus());
	        return dto;
	    }).collect(Collectors.toList());

	    return result;
	}

	@Override
	public List<ConnectionWithUserDTO> getPendingSent(UUID userId) {
	    validateUserExists(userId);
	    List<Connection> pendingConnections = connectionRepository.findByRequesterIdAndStatus(userId, ConnectionStatus.PENDING);
	    log.info("pendingConnections"+pendingConnections);
	    List<ConnectionWithUserDTO> result = pendingConnections.stream().map(conn -> {
	        UserResponse requester = restTemplate.getForObject(USER_SERVICE_URL + "/" + conn.getReceiverId(),
					UserResponse.class);
	        log.info("requester"+requester);
	        ConnectionWithUserDTO dto = new ConnectionWithUserDTO();
	        BeanUtils.copyProperties(conn, dto);  
	        
	        
	        dto.setFname(requester.getFname());
	        dto.setLname(requester.getLname());
	        dto.setUsername(requester.getUsername());
	        dto.setBio(requester.getBio());
	        dto.setProfilePicture(requester.getProfilePicture());
	        dto.setCoverPhoto(requester.getCoverPhoto());
	        dto.setGender(requester.getGender());
	        dto.setInterests(requester.getInterests());
	        dto.setStatus(requester.getStatus());
	        return dto;
	    }).collect(Collectors.toList());

	    return result;
	}

	@Override
	public String removeConnection(UUID user1, UUID user2) {
	    validateUserExists(user1);
	    validateUserExists(user2);

	    Optional<Connection> connOpt = connectionRepository.findByRequesterIdAndReceiverIdOrReceiverIdAndRequesterId(
	            user1, user2, user1, user2
	    );

	    if (connOpt.isEmpty()) {
	        return "Connection not found.";
	    }

	    Connection conn = connOpt.get();
	    if (conn.getStatus() != ConnectionStatus.ACCEPTED) {
	        return "Connection is not accepted, so cannot be removed.";
	    }

	    connectionRepository.delete(conn);
	    Map<String, String> payload = Map.of(
	            "requesterId", user1.toString(),
	            "receiverId", user2.toString(),
	            "action", "DISCONNECT"
	        );

	        try {
	            restTemplate.postForEntity(GRAPH_SERVICE_URL, payload, String.class);
	        } catch (Exception e) {
	            e.printStackTrace(); 
	        }
	    return "Removed connection successfully.";
	}

	@Override
	public String getConnectionStatus(UUID user1, UUID user2) {
	    validateUserExists(user1);
	    validateUserExists(user2);
	    if (user1.equals(user2)) {
	        return "OWN";
	    }
	    Optional<Connection> connOpt = connectionRepository.findByRequesterIdAndReceiverIdOrReceiverIdAndRequesterId(
	            user1, user2, user1, user2
	    );

	    return connOpt.map(conn -> conn.getStatus().name()).orElse("NONE");
	}
	
	@Override
	public List<UUID> getMutualFriends(UUID user1, UUID user2) {
		validateUserExists(user1);
		validateUserExists(user2);
	    // get all accepted connections for user1
	    Set<UUID> user1Connections = getAcceptedConnections(user1);

	    // get all accepted connections for user2
	    Set<UUID> user2Connections = getAcceptedConnections(user2);


	    user1Connections.retainAll(user2Connections); // intersection
	    
	    return new ArrayList<>(user1Connections);
	}

	private Set<UUID> getAcceptedConnections(UUID userId) {
		validateUserExists(userId);
	    List<Connection> connections = connectionRepository
	            .findAllByStatusAndRequesterIdOrStatusAndReceiverId(ConnectionStatus.ACCEPTED,userId, ConnectionStatus.ACCEPTED,userId);

	    Set<UUID> connectedUsers = new HashSet<>();
	    for (Connection conn : connections) {
	        if (conn.getRequesterId().equals(userId)) {
	            connectedUsers.add(conn.getReceiverId());
	        } else {
	            connectedUsers.add(conn.getRequesterId());
	        }
	    }
	    return connectedUsers;
	}
	
	public int getPendingReceivedCount(UUID userId) {
	    validateUserExists(userId);
	    return connectionRepository.countByReceiverIdAndStatus(userId, ConnectionStatus.PENDING);
	}
	
	public String blockUser(UUID requesterId, UUID receiverId) {
	    validateUserExists(requesterId);
	    validateUserExists(receiverId);

	    Optional<Connection> existing = connectionRepository.findByRequesterIdAndReceiverId(requesterId, receiverId);

	    if (existing.isPresent()) {
	        Connection c = existing.get();
	        c.setStatus(ConnectionStatus.BLOCKED);
	        connectionRepository.save(c);
	        return "User blocked.";
	    }

	    // If no connection, create new with BLOCKED status
	    Connection newBlock = new Connection();
	    
	    newBlock.setRequesterId(requesterId);
	    newBlock.setReceiverId(receiverId);
	    newBlock.setStatus(ConnectionStatus.BLOCKED);


	    connectionRepository.save(newBlock);
	    Map<String, String> payload = Map.of(
	            "requesterId", requesterId.toString(),
	            "receiverId", receiverId.toString(),
	            "action", "DISCONNECT"
	        );

	        try {
	            restTemplate.postForEntity(GRAPH_SERVICE_URL, payload, String.class);
	        } catch (Exception e) {
	            e.printStackTrace(); 
	        }

	    return "User blocked.";
	}
	
	public String unblockUser(UUID requesterId, UUID receiverId) {
		validateUserExists(requesterId);
	    validateUserExists(receiverId);
	    Optional<Connection> connection = connectionRepository
	        .findByRequesterIdAndReceiverIdAndStatus(requesterId, receiverId, ConnectionStatus.BLOCKED);

	    if (connection.isEmpty()) {
	        throw new NotFoundException("No block found between the users.");
	    }

	    connectionRepository.delete(connection.get());
	    return "User unblocked successfully.";
	}
	
	@Transactional
	public void deleteAllByUserId(UUID userId) {
		connectionRepository.deleteAllByUserId(userId);
	}

	
	

	public Optional<Connection> getConnectionBetweenUsers(UUID userId1, UUID userId2) {
        return connectionRepository.findByRequesterIdAndReceiverId(userId1, userId2)
                .or(() -> connectionRepository.findByRequesterIdAndReceiverId(userId2, userId1));
    }



}
