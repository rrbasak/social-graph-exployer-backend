package com.socialgraph.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.socialgraph.user_service.model.User;

import java.util.*;

public interface UserRepository extends JpaRepository<User, UUID> {
	public Optional<User> findByUsername(String username);

	@Query(value = "SELECT * FROM users " + "WHERE (LOWER(fname) LIKE LOWER(CONCAT('%', :query, '%')) "
			+ "OR LOWER(lname) LIKE LOWER(CONCAT('%', :query, '%'))) "
			+ "AND user_id != :currentUserId", nativeQuery = true)
	List<User> searchUsersByName(@Param("query") String query, @Param("currentUserId") UUID userId);

	@Query("SELECT u FROM user u WHERE " +
		       "(" +
		       "LOWER(u.fname) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
		       "LOWER(u.lname) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
		       "LOWER(u.username) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
		       "LOWER(CONCAT(u.fname, ' ', u.lname)) LIKE LOWER(CONCAT('%', :name, '%'))" +
		       ") AND u.userId NOT IN :excludeIds")
	public List<User> searchUsersByNameExcludingIds(String name, List<UUID> excludeIds);
	
	public boolean existsByUsername(String username);

}
