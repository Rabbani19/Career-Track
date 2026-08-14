package com.careertrack.controller;

import com.careertrack.dto.UserDTO;
import com.careertrack.model.User;
import com.careertrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ View Profile
    @GetMapping
    public String viewProfile(
            Authentication authentication,
            Model model) {

        // ✅ authentication.getName() returns username
        User user = userService.getCurrentUser(
                authentication.getName());
        model.addAttribute("user", user);
        return "user/profile";
    }

    // ✅ Edit Profile
    @GetMapping("/edit")
    public String editProfile(
            Authentication authentication,
            Model model) {

        User user = userService.getCurrentUser(
                authentication.getName());

        UserDTO userDTO = new UserDTO();
        userDTO.setFullName(user.getFullName());
        userDTO.setPhone(user.getPhone());
        userDTO.setLocation(user.getLocation());
        userDTO.setCurrentCompany(user.getCurrentCompany());
        userDTO.setCurrentRole(user.getCurrentRole());
        userDTO.setExperienceYears(user.getExperienceYears());
        userDTO.setSkills(user.getSkills());
        userDTO.setLinkedinUrl(user.getLinkedinUrl());
        userDTO.setGithubUrl(user.getGithubUrl());
        userDTO.setPortfolioUrl(user.getPortfolioUrl());

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("user", user);
        return "user/edit-profile";
    }

    // ✅ Update Profile
    @PostMapping("/edit")
    public String updateProfile(
            @ModelAttribute UserDTO userDTO,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User user = userService.getCurrentUser(
                authentication.getName());

        userService.updateProfile(user.getId(), userDTO);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "✅ Profile updated successfully!");

        return "redirect:/profile";
    }
}