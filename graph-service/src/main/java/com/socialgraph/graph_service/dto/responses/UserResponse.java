package com.socialgraph.graph_service.dto.responses;

import lombok.Data;

import java.util.*;

@Data
public class UserResponse {
    private UUID userId;
    private String fname;
    private String lname;
    private String username;
    private String profilePicture;
    private String bio;
    private String coverPhoto;
    private String gender;
    private List<String> interests;
    private String Status;
}