package com.careertrack.service;

import com.careertrack.dto.JobApplicationDTO;
import com.careertrack.enums.ApplicationStatus;
import com.careertrack.model.JobApplication;
import com.careertrack.model.User;
import com.careertrack.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class JobApplicationServiceImpl
        implements JobApplicationService {

    @Autowired
    private JobApplicationRepository
            jobApplicationRepository;

    // ================================
    // CREATE
    // ================================
    @Override
    public JobApplication addApplication(
            JobApplicationDTO dto,
            User user) {

        JobApplication application =
                JobApplication.builder()
                        .companyName(
                                dto.getCompanyName())
                        .jobRole(
                                dto.getJobRole())
                        .jobDescription(
                                dto.getJobDescription())
                        .jobUrl(
                                dto.getJobUrl())
                        .companyWebsite(
                                dto.getCompanyWebsite())
                        .companyLocation(
                                dto.getCompanyLocation())
                        .salaryRange(
                                dto.getSalaryRange())
                        .appliedDate(
                                dto.getAppliedDate())
                        .status(
                                dto.getStatus() != null ?
                                        dto.getStatus() :
                                        ApplicationStatus.APPLIED)
                        .notes(
                                dto.getNotes())
                        .referralName(
                                dto.getReferralName())
                        .isRemote(
                                dto.getIsRemote() != null ?
                                        dto.getIsRemote() : false)
                        .user(user)
                        .build();

        return jobApplicationRepository.save(application);
    }

    // ================================
    // READ
    // ================================
    @Override
    public List<JobApplication> getAllApplications(
            User user) {

        return jobApplicationRepository
                .findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public List<JobApplication> getApplicationsByStatus(
            User user,
            ApplicationStatus status) {

        return jobApplicationRepository
                .findByUserAndStatus(user, status);
    }

    @Override
    public JobApplication getApplicationById(
            Long id,
            User user) {

        return jobApplicationRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Application not found " +
                                        "or access denied!"));
    }

    // ================================
    // UPDATE
    // ================================
    @Override
    public JobApplication updateApplication(
            Long id,
            JobApplicationDTO dto,
            User user) {

        JobApplication application =
                getApplicationById(id, user);

        application.setCompanyName(
                dto.getCompanyName());
        application.setJobRole(
                dto.getJobRole());
        application.setJobDescription(
                dto.getJobDescription());
        application.setJobUrl(
                dto.getJobUrl());
        application.setCompanyWebsite(
                dto.getCompanyWebsite());
        application.setCompanyLocation(
                dto.getCompanyLocation());
        application.setSalaryRange(
                dto.getSalaryRange());
        application.setAppliedDate(
                dto.getAppliedDate());
        application.setStatus(
                dto.getStatus());
        application.setNotes(
                dto.getNotes());
        application.setReferralName(
                dto.getReferralName());
        application.setIsRemote(
                dto.getIsRemote());

        return jobApplicationRepository
                .save(application);
    }

    @Override
    public JobApplication updateStatus(
            Long id,
            ApplicationStatus status,
            User user) {

        JobApplication application =
                getApplicationById(id, user);

        application.setStatus(status);

        return jobApplicationRepository
                .save(application);
    }

    // ================================
    // DELETE
    // ================================
    @Override
    public void deleteApplication(
            Long id,
            User user) {

        JobApplication application =
                getApplicationById(id, user);

        jobApplicationRepository.delete(application);
    }

    // ================================
    // STATS
    // ================================
    @Override
    public Map<String, Long> getApplicationStats(
            User user) {

        Map<String, Long> stats = new HashMap<>();

        stats.put("total",
                jobApplicationRepository
                        .countByUser(user));

        stats.put("applied",
                jobApplicationRepository
                        .countByUserAndStatus(
                                user,
                                ApplicationStatus.APPLIED));

        stats.put("inReview",
                jobApplicationRepository
                        .countByUserAndStatus(
                                user,
                                ApplicationStatus.IN_REVIEW));

        stats.put("shortlisted",
                jobApplicationRepository
                        .countByUserAndStatus(
                                user,
                                ApplicationStatus.SHORTLISTED));

        stats.put("offered",
                jobApplicationRepository
                        .countByUserAndStatus(
                                user,
                                ApplicationStatus.OFFERED));

        stats.put("rejected",
                jobApplicationRepository
                        .countByUserAndStatus(
                                user,
                                ApplicationStatus.REJECTED));

        return stats;
    }

    @Override
    public Long getTotalApplications(User user) {
        return jobApplicationRepository
                .countByUser(user);
    }

    // ================================
    // TIMELINE (Stage 6 - Analytics)
    // ================================
    @Override
    public Map<String, Long> getMonthlyTimeline(User user) {

        List<JobApplication> applications =
                jobApplicationRepository.findByUser(user);

        Map<String, Long> timeline = new LinkedHashMap<>();

        // Pre-fill last 6 months so empty months still show as 0
        YearMonth current = YearMonth.now();
        for (int i = 5; i >= 0; i--) {
            YearMonth month = current.minusMonths(i);
            String label = month.getMonth()
                    .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                    + " " + month.getYear();
            timeline.put(label, 0L);
        }

        YearMonth cutoff = current.minusMonths(5);

        for (JobApplication app : applications) {
            if (app.getCreatedAt() == null) continue;

            YearMonth appMonth = YearMonth.from(app.getCreatedAt());
            if (appMonth.isBefore(cutoff)) continue;

            String label = appMonth.getMonth()
                    .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                    + " " + appMonth.getYear();

            timeline.merge(label, 1L, Long::sum);
        }

        return timeline;
    }
}