package com.careertrack.service;

import com.careertrack.dto.JobApplicationDTO;
import com.careertrack.enums.ApplicationStatus;
import com.careertrack.model.JobApplication;
import com.careertrack.model.User;
import com.careertrack.repository.JobApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceImplTest {

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @InjectMocks
    private JobApplicationServiceImpl service;

    @Mock
    private User user;

    @Mock
    private JobApplicationDTO dto;

    private JobApplication application;

    @BeforeEach
    void setUp() {
        application = JobApplication.builder()
                .companyName("Google")
                .jobRole("Java Developer")
                .jobDescription("Backend development")
                .jobUrl("https://example.com/job")
                .companyWebsite("https://google.com")
                .companyLocation("Bangalore")
                .salaryRange("8-12 LPA")
                .status(ApplicationStatus.APPLIED)
                .notes("Good opportunity")
                .referralName("John")
                .isRemote(false)
                .user(user)
                .build();
    }

    @Test
    void addApplication_shouldDefaultStatusToApplied() {

        when(dto.getCompanyName()).thenReturn("Google");
        when(dto.getJobRole()).thenReturn("Java Developer");
        when(dto.getStatus()).thenReturn(null);
        when(dto.getIsRemote()).thenReturn(null);
        when(jobApplicationRepository.save(any(JobApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        JobApplication result = service.addApplication(dto, user);

        assertEquals(ApplicationStatus.APPLIED, result.getStatus());
        assertFalse(result.getIsRemote());
        assertSame(user, result.getUser());

        verify(jobApplicationRepository).save(any(JobApplication.class));
    }

    @Test
    void addApplication_shouldRespectProvidedStatus() {

        when(dto.getCompanyName()).thenReturn("Microsoft");
        when(dto.getJobRole()).thenReturn("Software Engineer");
        when(dto.getStatus()).thenReturn(ApplicationStatus.SHORTLISTED);
        when(dto.getIsRemote()).thenReturn(true);

        when(jobApplicationRepository.save(any(JobApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        JobApplication result = service.addApplication(dto, user);

        assertEquals(ApplicationStatus.SHORTLISTED, result.getStatus());
        assertTrue(result.getIsRemote());
    }

    @Test
    void getApplicationById_shouldReturnApplicationWhenFound() {

        when(jobApplicationRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(application));

        JobApplication result = service.getApplicationById(1L, user);

        assertSame(application, result);

        verify(jobApplicationRepository)
                .findByIdAndUser(1L, user);
    }

    @Test
    void getApplicationById_shouldThrowExceptionWhenNotFound() {

        when(jobApplicationRepository.findByIdAndUser(99L, user))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.getApplicationById(99L, user)
        );

        assertTrue(exception.getMessage()
                .contains("Application not found"));

        verify(jobApplicationRepository)
                .findByIdAndUser(99L, user);
    }

    @Test
    void deleteApplication_shouldDeleteExistingApplication() {

        when(jobApplicationRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(application));

        service.deleteApplication(1L, user);

        verify(jobApplicationRepository).delete(application);
    }

    @Test
    void deleteApplication_shouldNotDeleteWhenApplicationDoesNotExist() {

        when(jobApplicationRepository.findByIdAndUser(99L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.deleteApplication(99L, user)
        );

        verify(jobApplicationRepository, never())
                .delete(any(JobApplication.class));
    }

    @Test
    void getApplicationStats_shouldReturnAllStatusCounts() {

        when(jobApplicationRepository.countByUser(user))
                .thenReturn(10L);

        when(jobApplicationRepository.countByUserAndStatus(
                user, ApplicationStatus.APPLIED))
                .thenReturn(4L);

        when(jobApplicationRepository.countByUserAndStatus(
                user, ApplicationStatus.IN_REVIEW))
                .thenReturn(2L);

        when(jobApplicationRepository.countByUserAndStatus(
                user, ApplicationStatus.SHORTLISTED))
                .thenReturn(1L);

        when(jobApplicationRepository.countByUserAndStatus(
                user, ApplicationStatus.OFFERED))
                .thenReturn(1L);

        when(jobApplicationRepository.countByUserAndStatus(
                user, ApplicationStatus.REJECTED))
                .thenReturn(2L);

        Map<String, Long> stats =
                service.getApplicationStats(user);

        assertEquals(10L, stats.get("total"));
        assertEquals(4L, stats.get("applied"));
        assertEquals(2L, stats.get("inReview"));
        assertEquals(1L, stats.get("shortlisted"));
        assertEquals(1L, stats.get("offered"));
        assertEquals(2L, stats.get("rejected"));
    }
}