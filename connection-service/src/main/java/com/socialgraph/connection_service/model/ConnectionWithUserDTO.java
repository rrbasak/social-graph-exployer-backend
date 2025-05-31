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
public class ConnectionWithUserDTO {
    private UUID connectionId;
    private UUID requesterId;
    private UUID receiverId;
    private String status;
    private LocalDateTime requestedAt;
    private LocalDateTime updatedAt;

    private String fname;
    private String lname;
    private String username;
    private String bio;
    private String profilePicture;
    private String coverPhoto;
//    private LocalDate dateOfBirth;
    private String gender;
//    private String location;
    private List<String> interests;
//    private LocalDateTime createdAt;
}

