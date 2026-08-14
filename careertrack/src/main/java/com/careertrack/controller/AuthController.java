package com.careertrack.controller;

import com.careertrack.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // ===== HOME =====
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // ===== LOGIN GET =====
    @GetMapping("/login")
    public String showLogin() {
        return "auth/login";
    }

    // ===== REGISTER GET =====
    @GetMapping("/register")
    public String showRegister() {
        return "auth/register";
    }

    // ===== REGISTER POST =====
    @PostMapping("/register")
    public String register(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            // Validate passwords
            if (!password.equals(confirmPassword)) {
                model.addAttribute("error",
                        "Passwords do not match!");
                return "auth/register";
            }

            // Check username
            if (userService.existsByUsername(username)) {
                model.addAttribute("error",
                        "Username '" + username + "' already taken!");
                return "auth/register";
            }

            // Check email
            if (userService.existsByEmail(email)) {
                model.addAttribute("error",
                        "Email '" + email + "' already registered!");
                return "auth/register";
            }

            // Register
            userService.registerUser(
                    firstName, lastName, username, email, password
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Account created successfully! Please login."
            );

            return "redirect:/login?registered";

        } catch (Exception e) {
            model.addAttribute("error",
                    "Registration failed: " + e.getMessage());
            return "auth/register";
        }
    }
}