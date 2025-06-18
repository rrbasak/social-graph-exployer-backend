package com.socialgraph.connection_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.socialgraph.connection_service.dto.requests.ConnectionRequest;
import com.socialgraph.connection_service.dto.responses.ConnectionResponse;
import com.socialgraph.connection_service.dto.responses.UserConnectionsResponse;
import com.socialgraph.connection_service.model.ConnectedUserList;
import com.socialgraph.connection_service.model.Connection;
import com.socialgraph.connection_service.model.ConnectionWithUserDTO;
import com.socialgraph.connection_service.serviceImple.ConnectionServiceImpl;

import java.util.*;

@RestController
@RequestMapping("/api/v0/connection")
public class ConnectionController {

	@Autowired
	ConnectionServiceImpl connectionService;

	@PostMapping("/send")
	public ResponseEntity<ConnectionResponse> sendRequest(@RequestBody ConnectionRequest request) {
		String message = connectionService.sendRequest(request.getRequesterId(), request.getReceiverId());
		return ResponseEntity.ok(new ConnectionResponse(message, true));
	}

	@PostMapping("/accept")
	public ResponseEntity<ConnectionResponse> acceptRequest(@RequestBody ConnectionRequest request) {
		String message = connectionService.acceptRequest(request.getRequesterId(), request.getReceiverId());
		return ResponseEntity.ok(new ConnectionResponse(message, true));
	}
	@GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Connection service is up");
    }

	@PostMapping("/reject")
	public ResponseEntity<ConnectionResponse> rejectRequest(@RequestBody ConnectionRequest request) {
		String message = connectionService.rejectRequest(request.getRequesterId(), request.getReceiverId());
		return ResponseEntity.ok(new ConnectionResponse(message, true));
	}

	@GetMapping("/{userId}/connections")
	public ResponseEntity<UserConnectionsResponse> getConnections(@PathVariable UUID userId) {
		List<ConnectedUserList> connections = connectionService.getConnections(userId);
		return ResponseEntity.ok(new UserConnectionsResponse(userId, connections));
	}

	@GetMapping("/{userId}/pending/received")
	public ResponseEntity<List<ConnectionWithUserDTO>> getPendingReceived(@PathVariable UUID userId) {
		return ResponseEntity.ok(connectionService.getPendingReceived(userId));
	}

	@GetMapping("/{userId}/pending/sent")
	public ResponseEntity<List<ConnectionWithUserDTO>> getPendingSent(@PathVariable UUID userId) {
		return ResponseEntity.ok(connectionService.getPendingSent(userId));
	}

	@DeleteMapping("/remove")
	public ResponseEntity<String> removeConnection(@RequestBody ConnectionRequest request) {
		return ResponseEntity.ok(connectionService.removeConnection(request.getRequesterId(), request.getReceiverId()));
	}

	@GetMapping("/status")
	public ResponseEntity<String> getConnectionStatus(@RequestParam UUID user1, @RequestParam UUID user2) {
		return ResponseEntity.ok(connectionService.getConnectionStatus(user1, user2));
	}

	@GetMapping("/mutual")
	public ResponseEntity<List<UUID>> getMutualFriends(@RequestParam UUID user1, @RequestParam UUID user2) {
		return ResponseEntity.ok(connectionService.getMutualFriends(user1, user2));
	}

	@GetMapping("/{userId}/pending/received/count")
	public ResponseEntity<Map<String, Object>> getPendingReceivedCount(@PathVariable UUID userId) {
		int count = connectionService.getPendingReceivedCount(userId);
		Map<String, Object> response = new HashMap<>();
		response.put("userId", userId);
		response.put("pendingCount", count);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/block")
	public ResponseEntity<ConnectionResponse> blockUser(@RequestBody ConnectionRequest request) {
		String message = connectionService.blockUser(request.getRequesterId(), request.getReceiverId());
		return ResponseEntity.ok(new ConnectionResponse(message, true));
	}

	@DeleteMapping("/unblock")
	public ResponseEntity<String> unblockUser(@RequestBody ConnectionRequest request) {
		String message = connectionService.unblockUser(request.getRequesterId(), request.getReceiverId());
		return ResponseEntity.ok(message);
	}

	@DeleteMapping("/cleanup/user/{userId}")
	public ResponseEntity<String> deleteAllConnections(@PathVariable UUID userId) {
		connectionService.deleteAllByUserId(userId);
		return ResponseEntity.ok("Connections deleted");
	}

	@GetMapping("/connection/check")
	public ResponseEntity<Connection> checkConnection(@RequestParam UUID userId1, @RequestParam UUID userId2) {
		Optional<Connection> connection = connectionService.getConnectionBetweenUsers(userId1, userId2);
		return connection.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
}
