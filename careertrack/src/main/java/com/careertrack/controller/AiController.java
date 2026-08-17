package com.careertrack.controller;

import com.careertrack.dto.AiRequestDTO;
import com.careertrack.model.User;
import com.careertrack.service.AiService;
import com.careertrack.service.JobApplicationService;
import com.careertrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    @Autowired
    private UserService userService;

    @Autowired
    private JobApplicationService jobApplicationService;

    @GetMapping
    public String aiToolsPage(
            Authentication authentication, Model model) {

        User user = userService.getCurrentUser(
                authentication.getName());

        model.addAttribute("applications",
                jobApplicationService.getAllApplications(user));

        return "ai/ai-tools";
    }

    @PostMapping("/tips")
    @ResponseBody
    public ResponseEntity<?> getTips(
            @RequestBody AiRequestDTO dto) {

        try {
            List<String> tips = aiService.getInterviewTips(
                    dto.getJobRole(), dto.getCompanyName());

            return ResponseEntity.ok(
                    Map.of("success", true, "data", tips));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("success", false,
                            "message", e.getMessage()));
        }
    }

    @PostMapping("/questions")
    @ResponseBody
    public ResponseEntity<?> getQuestions(
            @RequestBody AiRequestDTO dto) {

        try {
            List<String> questions =
                    aiService.generateMockQuestions(
                            dto.getJobRole(), dto.getCompanyName());

            return ResponseEntity.ok(
                    Map.of("success", true, "data", questions));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("success", false,
                            "message", e.getMessage()));
        }
    }
}