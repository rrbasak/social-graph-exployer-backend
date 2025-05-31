package com.socialgraph.connection_service.dto.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectedUserResponse {
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
