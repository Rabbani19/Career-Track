package com.careertrack.dto;

import com.careertrack.enums.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JobApplicationDTO {

    private Long id;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Job role is required")
    private String jobRole;

    private String jobDescription;
    private String jobUrl;
    private String companyWebsite;
    private String companyLocation;
    private String salaryRange;
    private LocalDate appliedDate;
    private ApplicationStatus status;
    private String notes;
    private String referralName;
    private Boolean isRemote;
}