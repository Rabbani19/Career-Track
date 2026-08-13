package com.careertrack.dto;

import com.careertrack.enums.InterviewMode;
import com.careertrack.enums.InterviewStatus;
import com.careertrack.enums.RoundType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class InterviewDTO {

    private Long id;

    @NotNull(message = "Job Application is required")
    private Long jobApplicationId;

    private String roundName;
    private RoundType roundType;

    @NotNull(message = "Interview date is required")
    private LocalDate interviewDate;

    private LocalTime interviewTime;
    private InterviewMode mode;
    private String meetingLink;
    private String interviewLocation;
    private String interviewerName;
    private String interviewerEmail;
    private Integer durationMinutes;
    private String notes;
    private String feedback;
    private InterviewStatus status;
}