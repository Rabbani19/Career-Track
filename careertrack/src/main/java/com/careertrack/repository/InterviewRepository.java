package com.careertrack.repository;

import com.careertrack.enums.InterviewStatus;
import com.careertrack.model.Interview;
import com.careertrack.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InterviewRepository
        extends JpaRepository<Interview, Long> {

    List<Interview> findByJobApplication(
            JobApplication jobApplication);

    @Query("SELECT i FROM Interview i " +
            "WHERE i.jobApplication.user.id = :userId")
    List<Interview> findAllByUserId(Long userId);

    @Query("SELECT i FROM Interview i " +
            "WHERE i.jobApplication.user.id = :userId " +
            "AND i.interviewDate = :date")
    List<Interview> findByUserIdAndDate(
            Long userId, LocalDate date);

    @Query("SELECT COUNT(i) FROM Interview i " +
            "WHERE i.jobApplication.user.id = :userId")
    Long countByUserId(Long userId);

    // Used by the reminder scheduler
    List<Interview> findByInterviewDateAndStatus(
            LocalDate interviewDate,
            InterviewStatus status);
}