package com.careertrack.service;

import com.careertrack.enums.Role;
import com.careertrack.model.User;

import java.util.List;
import java.util.Map;

public interface AdminService {

    List<User> getAllUsers();

    User getUserById(Long id);

    void toggleUserActive(Long id);

    void updateUserRole(Long id, Role role);

    void deleteUser(Long id);

    Map<String, Object> getSystemStats();
}