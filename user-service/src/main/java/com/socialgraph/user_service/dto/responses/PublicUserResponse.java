package com.socialgraph.user_service.dto.responses;

import java.util.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicUserResponse {
	private UUID userId;
    private String fname;
    private String lname;
    private String username;
    private String profilePicture;
    private String bio;
    private String coverPhoto;
    private String gender;
    private List<String> interests;
//    private String status;
}
