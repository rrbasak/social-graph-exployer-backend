package com.socialgraph.user_service.dto.requests;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
@Data	
public class UserUpdateRequest {
    private String fname;
    private String lname;
    private String email;
    private String bio;
    private String profilePicture;
    private String coverPhoto;
    private LocalDate dateOfBirth;
    private String gender;
    private String location;
    private List<String> interests;

    
}
