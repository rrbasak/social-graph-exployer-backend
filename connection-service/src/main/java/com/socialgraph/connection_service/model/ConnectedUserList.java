package com.socialgraph.connection_service.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectedUserList {
	private UUID userId;
    private String fname;
    private String lname;
    private String username;
    private String bio;
    private String profilePicture;
    private String coverPhoto;
    private LocalDate dateOfBirth;
    private String gender;
    private String location;
    private List<String> interests;
    private LocalDateTime createdAt;
}
