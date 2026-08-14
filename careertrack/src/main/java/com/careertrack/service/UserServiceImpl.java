package com.careertrack.service;

import com.careertrack.dto.UserDTO;
import com.careertrack.enums.Role;
import com.careertrack.model.User;
import com.careertrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Register New User
    @Override
    public void registerUser(
            String firstName,
            String lastName,
            String username,
            String email,
            String password) {

        User user = new User();

        // ✅ Matches updated User.java fields
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setFullName(firstName + " " + lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        user.setActive(true);  // ✅ active not isActive

        userRepository.save(user);
    }

    // ✅ Check Username Exists
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // ✅ Check Email Exists
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // ✅ Get Current User by username
    @Override
    public User getCurrentUser(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found: " + username));
    }

    // ✅ Update Profile
    @Override
    public void updateProfile(Long userId, UserDTO userDTO) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (userDTO.getFullName() != null)
            user.setFullName(userDTO.getFullName());

        if (userDTO.getPhone() != null)
            user.setPhone(userDTO.getPhone());

        if (userDTO.getLocation() != null)
            user.setLocation(userDTO.getLocation());

        if (userDTO.getCurrentCompany() != null)
            user.setCurrentCompany(
                    userDTO.getCurrentCompany());

        if (userDTO.getCurrentRole() != null)
            user.setCurrentRole(userDTO.getCurrentRole());

        if (userDTO.getExperienceYears() != null)
            user.setExperienceYears(
                    userDTO.getExperienceYears());

        if (userDTO.getSkills() != null)
            user.setSkills(userDTO.getSkills());

        if (userDTO.getLinkedinUrl() != null)
            user.setLinkedinUrl(userDTO.getLinkedinUrl());

        if (userDTO.getGithubUrl() != null)
            user.setGithubUrl(userDTO.getGithubUrl());

        if (userDTO.getPortfolioUrl() != null)
            user.setPortfolioUrl(userDTO.getPortfolioUrl());

        userRepository.save(user);
    }
}