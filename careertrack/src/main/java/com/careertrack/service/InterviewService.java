package com.careertrack.service;

import com.careertrack.dto.InterviewDTO;
import com.careertrack.enums.InterviewStatus;
import com.careertrack.model.Interview;
import com.careertrack.model.User;

import java.util.List;

public interface InterviewService {

    // Create
    Interview scheduleInterview(
            InterviewDTO dto, User user);

    // Read
    List<Interview> getAllInterviews(User user);

    Interview getInterviewById(Long id, User user);

    List<Interview> getInterviewsByApplication(
            Long applicationId, User user);

    // Update
    Interview updateInterview(
            Long id, InterviewDTO dto, User user);

    Interview updateInterviewStatus(
            Long id, InterviewStatus status, User user);

    // Delete
    void deleteInterview(Long id, User user);

    // Stats
    Long getTotalInterviews(User user);

    List<Interview> getUpcomingInterviews(User user);
}