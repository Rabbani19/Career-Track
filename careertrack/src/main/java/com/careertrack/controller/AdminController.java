package com.careertrack.controller;

import com.careertrack.enums.Role;
import com.careertrack.model.User;
import com.careertrack.service.AdminService;
import com.careertrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("stats",
                adminService.getSystemStats());
        return "admin/admin-dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users",
                adminService.getAllUsers());
        model.addAttribute("roles", Role.values());
        return "admin/admin-users";
    }

    @PostMapping("/users/{id}/toggle-active")
    public String toggleActive(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User currentUser = userService.getCurrentUser(
                authentication.getName());

        if (currentUser.getId().equals(id)) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "You cannot deactivate your own account!");
            return "redirect:/admin/users";
        }

        adminService.toggleUserActive(id);
        redirectAttributes.addFlashAttribute(
                "successMessage", "User status updated!");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String updateRole(
            @PathVariable Long id,
            @RequestParam Role role,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User currentUser = userService.getCurrentUser(
                authentication.getName());

        if (currentUser.getId().equals(id)) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "You cannot change your own role!");
            return "redirect:/admin/users";
        }

        adminService.updateUserRole(id, role);
        redirectAttributes.addFlashAttribute(
                "successMessage", "User role updated!");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User currentUser = userService.getCurrentUser(
                authentication.getName());

        if (currentUser.getId().equals(id)) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "You cannot delete your own account!");
            return "redirect:/admin/users";
        }

        adminService.deleteUser(id);
        redirectAttributes.addFlashAttribute(
                "successMessage", "User deleted!");
        return "redirect:/admin/users";
    }
}