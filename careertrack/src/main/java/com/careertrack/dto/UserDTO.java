package com.careertrack.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String location;
    private String currentCompany;
    private String currentRole;
    private Integer experienceYears;
    private String skills;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
}