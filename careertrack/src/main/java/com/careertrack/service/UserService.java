package com.careertrack.service;

import com.careertrack.dto.UserDTO;
import com.careertrack.model.User;

public interface UserService {

    // Auth methods
    void registerUser(String firstName, String lastName,
                      String username, String email,
                      String password);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // Profile methods
    User getCurrentUser(String username);

    void updateProfile(Long userId, UserDTO userDTO);
}