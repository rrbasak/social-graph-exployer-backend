package com.socialgraph.connection_service.dto.responses;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class UserMutualResponse {
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
