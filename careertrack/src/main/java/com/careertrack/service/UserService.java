package com.careertrack.service;

import com.careertrack.dto.UserDTO;
import com.careertrack.model.User;

public interface UserService {

    // ✅ Register
    void registerUser(
            String firstName,
            String lastName,
            String username,
            String email,
            String password
    );

    // ✅ Checks
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // ✅ Profile
    User getCurrentUser(String username);
    void updateProfile(Long userId, UserDTO userDTO);
}