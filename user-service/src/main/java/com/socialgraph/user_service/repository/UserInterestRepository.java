package com.socialgraph.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.socialgraph.user_service.model.UserInterest;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
}
