package com.socialgraph.user_service.service;

import com.socialgraph.user_service.dto.requests.UserUpdateRequest;
import com.socialgraph.user_service.model.User;
import java.util.*;

public interface UserService {
	User createUser(User user);
    List<User> getAllUsers();
    User getUserById(UUID id);
    User updateUser(UUID userId, UserUpdateRequest updateRequest);
    List<User> searchUsers(String name, List<UUID> excludeIds);
    boolean isUsernameAvailable(String username);
}
