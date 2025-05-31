package com.socialgraph.user_service.model;

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

@Entity
@Table(name = "users", schema = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@Column(name = "user_id")
	private UUID userId;

	@Column(name = "fname", nullable = false)
	private String fname;

	@Column(name = "lname", nullable = false)
	private String lname;

	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "bio")
	private String bio;

	@Column(name = "profile_picture")
	private String profilePicture;

	@Column(name = "cover_photo")
	private String coverPhoto;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Column(name = "gender")
	private String gender;

	@Column(name = "location")
	private String location;

//	@OneToMany(mappedBy = "user", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
//	private List<UserInterest> interests;
	
	@ElementCollection
	@Column(name = "interests")
	@CollectionTable(name = "user_interests", joinColumns = @JoinColumn(name = "user_id"), schema = "users")
//	@Cascade(CascadeType.ALL)
	private List<String> interests;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@PrePersist
	public void prePersist() {
		if (this.userId == null) {
			this.userId = UUID.randomUUID();
		}
	}
}
