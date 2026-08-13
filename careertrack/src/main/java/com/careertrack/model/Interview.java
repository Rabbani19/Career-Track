package com.careertrack.model;

import com.careertrack.enums.InterviewMode;
import com.careertrack.enums.InterviewStatus;
import com.careertrack.enums.RoundType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "interviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "round_name")
    private String roundName;

    @Enumerated(EnumType.STRING)
    @Column(name = "round_type")
    private RoundType roundType;

    @Column(name = "interview_date")
    private LocalDate interviewDate;

    @Column(name = "interview_time")
    private LocalTime interviewTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode")
    private InterviewMode mode;

    @Column(name = "meeting_link")
    private String meetingLink;

    @Column(name = "interview_location")
    private String interviewLocation;

    @Column(name = "interviewer_name")
    private String interviewerName;

    @Column(name = "interviewer_email")
    private String interviewerEmail;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private InterviewStatus status
            = InterviewStatus.SCHEDULED;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id",
            nullable = false)
    private JobApplication jobApplication;
}