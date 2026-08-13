package com.careertrack.service;

import com.careertrack.dto.InterviewDTO;
import com.careertrack.enums.InterviewStatus;
import com.careertrack.model.Interview;
import com.careertrack.model.JobApplication;
import com.careertrack.model.User;
import com.careertrack.repository.InterviewRepository;
import com.careertrack.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterviewServiceImpl
        implements InterviewService {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private JobApplicationRepository
            jobApplicationRepository;

    @Override
    public Interview scheduleInterview(
            InterviewDTO dto, User user) {

        // Get Job Application
        JobApplication application =
                jobApplicationRepository
                        .findByIdAndUser(
                                dto.getJobApplicationId(),
                                user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found!"));

        Interview interview = Interview.builder()
                .roundName(dto.getRoundName())
                .roundType(dto.getRoundType())
                .interviewDate(dto.getInterviewDate())
                .interviewTime(dto.getInterviewTime())
                .mode(dto.getMode())
                .meetingLink(dto.getMeetingLink())
                .interviewLocation(
                        dto.getInterviewLocation())
                .interviewerName(dto.getInterviewerName())
                .interviewerEmail(
                        dto.getInterviewerEmail())
                .durationMinutes(dto.getDurationMinutes())
                .notes(dto.getNotes())
                .feedback(dto.getFeedback())
                .status(dto.getStatus() != null ?
                        dto.getStatus() :
                        InterviewStatus.SCHEDULED)
                .jobApplication(application)
                .build();

        return interviewRepository.save(interview);
    }

    @Override
    public List<Interview> getAllInterviews(User user) {
        return interviewRepository
                .findAllByUserId(user.getId());
    }

    @Override
    public Interview getInterviewById(
            Long id, User user) {
        return interviewRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Interview not found!"));
    }

    @Override
    public List<Interview> getInterviewsByApplication(
            Long applicationId, User user) {

        JobApplication application =
                jobApplicationRepository
                        .findByIdAndUser(
                                applicationId, user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found!"));

        return interviewRepository
                .findByJobApplication(application);
    }

    @Override
    public Interview updateInterview(
            Long id, InterviewDTO dto, User user) {

        Interview interview =
                getInterviewById(id, user);

        interview.setRoundName(dto.getRoundName());
        interview.setRoundType(dto.getRoundType());
        interview.setInterviewDate(dto.getInterviewDate());
        interview.setInterviewTime(dto.getInterviewTime());
        interview.setMode(dto.getMode());
        interview.setMeetingLink(dto.getMeetingLink());
        interview.setInterviewLocation(
                dto.getInterviewLocation());
        interview.setInterviewerName(
                dto.getInterviewerName());
        interview.setInterviewerEmail(
                dto.getInterviewerEmail());
        interview.setDurationMinutes(
                dto.getDurationMinutes());
        interview.setNotes(dto.getNotes());
        interview.setFeedback(dto.getFeedback());
        interview.setStatus(dto.getStatus());

        return interviewRepository.save(interview);
    }

    @Override
    public Interview updateInterviewStatus(
            Long id,
            InterviewStatus status,
            User user) {

        Interview interview =
                getInterviewById(id, user);
        interview.setStatus(status);
        return interviewRepository.save(interview);
    }

    @Override
    public void deleteInterview(Long id, User user) {
        Interview interview =
                getInterviewById(id, user);
        interviewRepository.delete(interview);
    }

    @Override
    public Long getTotalInterviews(User user) {
        return interviewRepository
                .countByUserId(user.getId());
    }

    @Override
    public List<Interview> getUpcomingInterviews(
            User user) {
        return getAllInterviews(user)
                .stream()
                .filter(i -> i.getInterviewDate() != null
                        && !i.getInterviewDate()
                        .isBefore(LocalDate.now())
                        && i.getStatus() ==
                        InterviewStatus.SCHEDULED)
                .sorted((a, b) ->
                        a.getInterviewDate()
                                .compareTo(
                                        b.getInterviewDate()))
                .limit(5)
                .collect(Collectors.toList());
    }
}