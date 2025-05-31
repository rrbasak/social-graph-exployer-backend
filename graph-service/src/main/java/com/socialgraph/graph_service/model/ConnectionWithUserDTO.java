package com.socialgraph.graph_service.model;

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

