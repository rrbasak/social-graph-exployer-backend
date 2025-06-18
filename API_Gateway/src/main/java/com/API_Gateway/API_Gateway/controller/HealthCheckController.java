package com.API_Gateway.API_Gateway.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v0/health")
public class HealthCheckController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<Map<String, String>> checkAllServices() {
        Map<String, String> statusMap = new HashMap<>();

        try {
            restTemplate.getForEntity("http://USER-SERVICE/api/v0/users/check", String.class);
            statusMap.put("userService", "UP");
        } catch (Exception e) {
            statusMap.put("userService", "DOWN");
        }

        try {
            restTemplate.getForEntity("http://GRAPH-SERVICE/api/v0/graph/check", String.class);
            statusMap.put("graphService", "UP");
        } catch (Exception e) {
            statusMap.put("graphService", "DOWN");
        }

        try {
            restTemplate.getForEntity("http://CONNECTION-SERVICE/api/v0/connection/check", String.class);
            statusMap.put("connectionService", "UP");
        } catch (Exception e) {
            statusMap.put("connectionService", "DOWN");
        }

        return ResponseEntity.ok(statusMap);
    }
}

