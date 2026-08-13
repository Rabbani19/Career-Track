package com.careertrack.controller;

import com.careertrack.model.Interview;
import com.careertrack.model.User;
import com.careertrack.service.InterviewService;
import com.careertrack.service.JobApplicationService;
import com.careertrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private InterviewService interviewService;

    @GetMapping("/dashboard")
    public String dashboard(
            Authentication authentication,
            Model model) {

        User user = userService.getCurrentUser(
                authentication.getName());

        // Application Stats
        var stats = jobApplicationService
                .getApplicationStats(user);

        // Recent Applications
        var recentApplications = jobApplicationService
                .getAllApplications(user)
                .stream()
                .limit(5)
                .toList();

        // Upcoming Interviews
        List<Interview> upcomingInterviews =
                interviewService
                        .getUpcomingInterviews(user);

        // Total Interviews
        Long totalInterviews =
                interviewService
                        .getTotalInterviews(user);

        model.addAttribute("user", user);
        model.addAttribute("stats", stats);
        model.addAttribute("recentApplications",
                recentApplications);
        model.addAttribute("upcomingInterviews",
                upcomingInterviews);
        model.addAttribute("totalInterviews",
                totalInterviews);

        return "dashboard";
    }
}