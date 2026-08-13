package com.careertrack.service;

import com.careertrack.dto.JobApplicationDTO;
import com.careertrack.enums.ApplicationStatus;
import com.careertrack.model.JobApplication;
import com.careertrack.model.User;

import java.util.List;
import java.util.Map;

public interface JobApplicationService {

    // ================================
    // CREATE
    // ================================
    JobApplication addApplication(
            JobApplicationDTO dto,
            User user);

    // ================================
    // READ
    // ================================
    List<JobApplication> getAllApplications(
            User user);

    List<JobApplication> getApplicationsByStatus(
            User user,
            ApplicationStatus status);

    JobApplication getApplicationById(
            Long id,
            User user);

    // ================================
    // UPDATE
    // ================================
    JobApplication updateApplication(
            Long id,
            JobApplicationDTO dto,
            User user);

    JobApplication updateStatus(
            Long id,
            ApplicationStatus status,
            User user);

    // ================================
    // DELETE
    // ================================
    void deleteApplication(
            Long id,
            User user);

    // ================================
    // STATS
    // ================================
    Map<String, Long> getApplicationStats(
            User user);

    Long getTotalApplications(
            User user);
}