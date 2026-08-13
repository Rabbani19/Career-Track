package com.careertrack.repository;

import com.careertrack.enums.ApplicationStatus;
import com.careertrack.model.JobApplication;
import com.careertrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    // ✅ Find by ID and User (Security Fix)
    Optional<JobApplication> findByIdAndUser(
            Long id,
            User user);

    // Find all by User
    List<JobApplication> findByUser(
            User user);

    // Find by User and Status
    List<JobApplication> findByUserAndStatus(
            User user,
            ApplicationStatus status);

    // Find by User ordered by date
    List<JobApplication> findByUserOrderByCreatedAtDesc(
            User user);

    // Count by User
    Long countByUser(User user);

    // Count by User and Status
    Long countByUserAndStatus(
            User user,
            ApplicationStatus status);
}