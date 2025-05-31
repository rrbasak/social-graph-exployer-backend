package com.socialgraph.connection_service.model;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Type;
//import org.hibernate.annotations.CascadeType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.CascadeType;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private UUID userId;
	private String fname;
	private String lname;
	private String username;
	private String email;
	private String password;
	private String bio;
	private String profilePicture;
	private String coverPhoto;
	private LocalDate dateOfBirth;
	private String gender;
	private String location;
	private List<String> interests;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String status;
}
