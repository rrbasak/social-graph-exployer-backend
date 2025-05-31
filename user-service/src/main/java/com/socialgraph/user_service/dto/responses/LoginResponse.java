package com.socialgraph.user_service.dto.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
//	private String username;
//	private String accessToken;
//	private String refreshToken;
	private UUID id;
    private String username;
    private String email;
    private String accessToken;
    private String refreshToken;
    private String fname;
    private String lname;
    private String bio;
    private String profilePicture;
    private String coverPhoto;
    private LocalDate dateOfBirth;
    private String gender;
    private String location;
    private List<String> interests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
