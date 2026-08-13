package com.careertrack.controller;

import com.careertrack.dto.InterviewDTO;
import com.careertrack.enums.InterviewMode;
import com.careertrack.enums.InterviewStatus;
import com.careertrack.enums.RoundType;
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
@RequestMapping("/interviews")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private UserService userService;

    // View All Interviews
    @GetMapping
    public String getAllInterviews(
            Authentication authentication,
            Model model) {

        User user = userService.getCurrentUser(
                authentication.getName());

        List<Interview> interviews =
                interviewService.getAllInterviews(user);

        model.addAttribute("interviews", interviews);
        model.addAttribute("user", user);
        model.addAttribute("totalInterviews",
                interviewService.getTotalInterviews(user));

        return "interviews/interviews";
    }

    // Schedule Interview Form
    @GetMapping("/add")
    public String addInterviewForm(
            @RequestParam(required = false)
            Long applicationId,
            Authentication authentication,
            Model model) {

        User user = userService.getCurrentUser(
                authentication.getName());

        // Get all applications for dropdown
        List<JobApplication> applications =
                jobApplicationService
                        .getAllApplications(user);

        InterviewDTO dto = new InterviewDTO();

        // Pre select application if provided
        if (applicationId != null) {
            dto.setJobApplicationId(applicationId);
        }

        model.addAttribute("interviewDTO", dto);
        model.addAttribute("applications", applications);
        model.addAttribute("roundTypes",
                RoundType.values());
        model.addAttribute("modes",
                InterviewMode.values());
        model.addAttribute("statuses",
                InterviewStatus.values());
        model.addAttribute("user", user);

        return "interviews/add-interview";
    }

    // Schedule Interview Submit
    @PostMapping("/add")
    public String addInterviewSubmit(
            @Valid @ModelAttribute("interviewDTO")
            InterviewDTO dto,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model) {

        User user = userService.getCurrentUser(
                authentication.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("applications",
                    jobApplicationService
                            .getAllApplications(user));
            model.addAttribute("roundTypes",
                    RoundType.values());
            model.addAttribute("modes",
                    InterviewMode.values());
            model.addAttribute("statuses",
                    InterviewStatus.values());
            return "interviews/add-interview";
        }

        try {
            interviewService.scheduleInterview(dto, user);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Interview scheduled successfully! ✅");
            return "redirect:/interviews";

        } catch (Exception e) {
            model.addAttribute("errorMessage",
                    e.getMessage());
            model.addAttribute("applications",
                    jobApplicationService
                            .getAllApplications(user));
            model.addAttribute("roundTypes",
                    RoundType.values());
            model.addAttribute("modes",
                    InterviewMode.values());
            model.addAttribute("statuses",
                    InterviewStatus.values());
            return "interviews/add-interview";
        }
    }

    // View Single Interview
    @GetMapping("/{id}")
    public String viewInterview(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        User user = userService.getCurrentUser(
                authentication.getName());

        Interview interview =
                interviewService
                        .getInterviewById(id, user);

        model.addAttribute("interview", interview);
        model.addAttribute("statuses",
                InterviewStatus.values());
        model.addAttribute("user", user);

        return "interviews/view-interview";
    }

    // Edit Interview Form
    @GetMapping("/{id}/edit")
    public String editInterviewForm(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        User user = userService.getCurrentUser(
                authentication.getName());

        Interview interview =
                interviewService
                        .getInterviewById(id, user);

        List<JobApplication> applications =
                jobApplicationService
                        .getAllApplications(user);

        InterviewDTO dto = new InterviewDTO();
        dto.setJobApplicationId(
                interview.getJobApplication().getId());
        dto.setRoundName(interview.getRoundName());
        dto.setRoundType(interview.getRoundType());
        dto.setInterviewDate(interview.getInterviewDate());
        dto.setInterviewTime(interview.getInterviewTime());
        dto.setMode(interview.getMode());
        dto.setMeetingLink(interview.getMeetingLink());
        dto.setInterviewLocation(
                interview.getInterviewLocation());
        dto.setInterviewerName(
                interview.getInterviewerName());
        dto.setInterviewerEmail(
                interview.getInterviewerEmail());
        dto.setDurationMinutes(
                interview.getDurationMinutes());
        dto.setNotes(interview.getNotes());
        dto.setFeedback(interview.getFeedback());
        dto.setStatus(interview.getStatus());

        model.addAttribute("interviewDTO", dto);
        model.addAttribute("interviewId", id);
        model.addAttribute("applications", applications);
        model.addAttribute("roundTypes",
                RoundType.values());
        model.addAttribute("modes",
                InterviewMode.values());
        model.addAttribute("statuses",
                InterviewStatus.values());
        model.addAttribute("user", user);

        return "interviews/edit-interview";
    }

    // Edit Interview Submit
    @PostMapping("/{id}/edit")
    public String editInterviewSubmit(
            @PathVariable Long id,
            @Valid @ModelAttribute("interviewDTO")
            InterviewDTO dto,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model) {

        User user = userService.getCurrentUser(
                authentication.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("applications",
                    jobApplicationService
                            .getAllApplications(user));
            model.addAttribute("roundTypes",
                    RoundType.values());
            model.addAttribute("modes",
                    InterviewMode.values());
            model.addAttribute("statuses",
                    InterviewStatus.values());
            model.addAttribute("interviewId", id);
            return "interviews/edit-interview";
        }

        try {
            interviewService.updateInterview(
                    id, dto, user);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Interview updated successfully! ✅");
            return "redirect:/interviews/" + id;

        } catch (Exception e) {
            model.addAttribute("errorMessage",
                    e.getMessage());
            return "interviews/edit-interview";
        }
    }

    // Update Interview Status
    @PostMapping("/{id}/status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam InterviewStatus status,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User user = userService.getCurrentUser(
                authentication.getName());

        interviewService.updateInterviewStatus(
                id, status, user);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Interview status updated! ✅");

        return "redirect:/interviews/" + id;
    }

    // Delete Interview
    @PostMapping("/{id}/delete")
    public String deleteInterview(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User user = userService.getCurrentUser(
                authentication.getName());

        interviewService.deleteInterview(id, user);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Interview deleted successfully! ✅");

        return "redirect:/interviews";
    }
}