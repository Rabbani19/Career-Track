package com.careertrack.service;

import com.careertrack.enums.Role;
import com.careertrack.model.User;
import com.careertrack.repository.DocumentRepository;
import com.careertrack.repository.InterviewRepository;
import com.careertrack.repository.JobApplicationRepository;
import com.careertrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found!"));
    }

    @Override
    public void toggleUserActive(Long id) {
        User user = getUserById(id);
        user.setActive(!user.getActive());
        userRepository.save(user);
    }

    @Override
    public void updateUserRole(Long id, Role role) {
        User user = getUserById(id);
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    public Map<String, Object> getSystemStats() {

        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsers", userRepository.count());
        stats.put("activeUsers",
                userRepository.countByActive(true));
        stats.put("inactiveUsers",
                userRepository.countByActive(false));
        stats.put("adminCount",
                userRepository.countByRole(Role.ADMIN));

        stats.put("totalApplications",
                jobApplicationRepository.count());
        stats.put("totalInterviews",
                interviewRepository.count());
        stats.put("totalDocuments",
                documentRepository.count());

        return stats;
    }
}