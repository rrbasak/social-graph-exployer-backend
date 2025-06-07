package com.socialgraph.user_service.model;


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

    private UUID userId;
    private String fname;
    private String lname;
    private String username;
    private String bio;
    private String profilePicture;
    private String coverPhoto;
    private String gender;
    private List<String> interests;
}

