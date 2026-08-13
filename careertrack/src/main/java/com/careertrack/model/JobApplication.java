package com.careertrack.model;

import com.careertrack.enums.ApplicationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Company name is required")
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @NotBlank(message = "Job role is required")
    @Column(name = "job_role", nullable = false)
    private String jobRole;

    @Column(name = "job_description",
            columnDefinition = "TEXT")
    private String jobDescription;

    @Column(name = "job_url")
    private String jobUrl;

    @Column(name = "company_website")
    private String companyWebsite;

    @Column(name = "company_location")
    private String companyLocation;

    @Column(name = "salary_range")
    private String salaryRange;

    @Column(name = "applied_date")
    private LocalDate appliedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ApplicationStatus status
            = ApplicationStatus.APPLIED;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "referral_name")
    private String referralName;

    @Column(name = "is_remote")
    @Builder.Default
    private Boolean isRemote = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            nullable = false)
    private User user;

    @OneToMany(mappedBy = "jobApplication",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Interview> interviews;
}