package com.socialgraph.graph_service.controller;

import com.socialgraph.graph_service.model.ConnectionWithUserDTO;
import com.socialgraph.graph_service.serviceImpl.GraphServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v0/graph")
public class GraphController {

    @Autowired
    private GraphServiceImpl graphService;
    
    //use in user service
    @PostMapping("/create")
    public void createUserNode(@RequestParam UUID userId) {
    	graphService.createUserNode(userId);
    }
    
    //use in connection service
    @PostMapping("/sync")
    public String syncConnection(@RequestBody Map<String, String> payload) {
        UUID requesterId = UUID.fromString(payload.get("requesterId"));
        UUID receiverId = UUID.fromString(payload.get("receiverId"));
        String action = payload.get("action");  // CONNECT or DISCONNECT

        if ("CONNECT".equalsIgnoreCase(action)) {
            graphService.addFriend(requesterId, receiverId);
        } else if ("DISCONNECT".equalsIgnoreCase(action)) {
            graphService.removeFriend(requesterId, receiverId);
        }

        return "Sync successful";
    }

    @GetMapping("/mutual")
    public List<UUID> getMutual(@RequestParam UUID user1, @RequestParam UUID user2) {
        return graphService.getMutualFriends(user1, user2);
    }

    @GetMapping("/shortest-path")
    public Map<String, Integer> getShortestPath(@RequestParam UUID user1, @RequestParam UUID user2) {
        Integer hops = graphService.getShortestPath(user1, user2);
        return Map.of("hops", hops != null ? hops : -1);
    }

    @GetMapping("/suggestions")
    public List<ConnectionWithUserDTO> getSuggestions(@RequestParam UUID userId) {
        return graphService.suggestFriends(userId);
    }
    
    @PostMapping("/create-node")
    public ResponseEntity<String> createUserNode(@RequestBody Map<String, String> payload) {
        UUID userId = UUID.fromString(payload.get("id"));
        graphService.createNodeIfNotExists(userId);
        return ResponseEntity.ok("Node created or already exists.");
    }
    @DeleteMapping("/delete-node/{id}")
    public ResponseEntity<String> deleteUserNode(@PathVariable UUID id) {
        graphService.deleteUserNode(id);
        return ResponseEntity.ok("Node deleted");
    }
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Graph service is up");
    }
}