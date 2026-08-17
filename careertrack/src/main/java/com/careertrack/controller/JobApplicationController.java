package com.careertrack.controller;

import com.careertrack.dto.JobApplicationDTO;
import com.careertrack.enums.ApplicationStatus;
import com.careertrack.model.Interview;
import com.careertrack.model.JobApplication;
import com.careertrack.model.User;
import com.careertrack.service.InterviewService;
import com.careertrack.service.JobApplicationService;
import com.careertrack.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/applications")
public class JobApplicationController {

    @Autowired
    private UserService userService;

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private InterviewService interviewService;

    @GetMapping
    public String listApplications(
            @RequestParam(required = false) ApplicationStatus status,
            Authentication authentication,
            Model model) {

        User user = userService.getCurrentUser(
                authentication.getName());

        List<JobApplication> applications = status != null
                ? jobApplicationService.getApplicationsByStatus(user, status)
                : jobApplicationService.getAllApplications(user);

        model.addAttribute("applications", applications);
        model.addAttribute("stats",
                jobApplicationService.getApplicationStats(user));
        model.addAttribute("statuses", ApplicationStatus.values());
        model.addAttribute("selectedStatus", status);

        return "applications/applications";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("applicationDTO", new JobApplicationDTO());
        model.addAttribute("statuses", ApplicationStatus.values());
        return "applications/add-application";
    }

    @PostMapping("/add")
    public String addApplication(
            @Valid @ModelAttribute("applicationDTO")
            JobApplicationDTO dto,
            BindingResult result,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("statuses", ApplicationStatus.values());
            return "applications/add-application";
        }

        User user = userService.getCurrentUser(
                authentication.getName());

        jobApplicationService.addApplication(dto, user);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Application added successfully!");

        return "redirect:/applications";
    }

    @GetMapping("/{id}")
    public String viewApplication(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        User user = userService.getCurrentUser(
                authentication.getName());

        JobApplication application =
                jobApplicationService.getApplicationById(id, user);

        List<Interview> interviews = interviewService
                .getInterviewsByApplication(id, user);

        model.addAttribute("application", application);
        model.addAttribute("interviews", interviews);

        return "applications/view-application";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        User user = userService.getCurrentUser(
                authentication.getName());

        JobApplication application =
                jobApplicationService.getApplicationById(id, user);

        JobApplicationDTO dto = new JobApplicationDTO();
        dto.setId(application.getId());
        dto.setCompanyName(application.getCompanyName());
        dto.setJobRole(application.getJobRole());
        dto.setJobDescription(application.getJobDescription());
        dto.setJobUrl(application.getJobUrl());
        dto.setCompanyWebsite(application.getCompanyWebsite());
        dto.setCompanyLocation(application.getCompanyLocation());
        dto.setSalaryRange(application.getSalaryRange());
        dto.setAppliedDate(application.getAppliedDate());
        dto.setStatus(application.getStatus());
        dto.setNotes(application.getNotes());
        dto.setReferralName(application.getReferralName());
        dto.setIsRemote(application.getIsRemote());

        model.addAttribute("applicationDTO", dto);
        model.addAttribute("statuses", ApplicationStatus.values());
        model.addAttribute("applicationId", id);

        return "applications/edit-application";
    }

    @PostMapping("/{id}/edit")
    public String updateApplication(
            @PathVariable Long id,
            @Valid @ModelAttribute("applicationDTO")
            JobApplicationDTO dto,
            BindingResult result,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("statuses", ApplicationStatus.values());
            model.addAttribute("applicationId", id);
            return "applications/edit-application";
        }

        User user = userService.getCurrentUser(
                authentication.getName());

        jobApplicationService.updateApplication(id, dto, user);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Application updated successfully!");

        return "redirect:/applications";
    }

    @PostMapping("/{id}/delete")
    public String deleteApplication(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User user = userService.getCurrentUser(
                authentication.getName());

        jobApplicationService.deleteApplication(id, user);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Application deleted!");

        return "redirect:/applications";
    }
}