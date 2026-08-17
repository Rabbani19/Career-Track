package com.careertrack.controller;

import com.careertrack.model.User;
import com.careertrack.service.NotificationService;
import com.careertrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public String viewNotifications(
            Authentication authentication,
            Model model) {

        User user = userService.getCurrentUser(
                authentication.getName());

        model.addAttribute("notifications",
                notificationService.getAllNotifications(user));

        return "notifications";
    }

    @PostMapping("/{id}/read")
    public String markAsRead(
            @PathVariable Long id,
            Authentication authentication) {

        User user = userService.getCurrentUser(
                authentication.getName());

        notificationService.markAsRead(id, user);

        return "redirect:/notifications";
    }

    @PostMapping("/read-all")
    public String markAllAsRead(
            Authentication authentication) {

        User user = userService.getCurrentUser(
                authentication.getName());

        notificationService.markAllAsRead(user);

        return "redirect:/notifications";
    }
}