package com.careertrack.config;

import com.careertrack.model.User;
import com.careertrack.service.NotificationService;
import com.careertrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @ModelAttribute
    public void addUnreadNotificationCount(Model model) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(
                authentication.getPrincipal())) {

            try {
                User user = userService.getCurrentUser(
                        authentication.getName());

                Long unreadCount = notificationService
                        .getUnreadCount(user);

                model.addAttribute(
                        "unreadNotificationCount", unreadCount);

            } catch (Exception e) {
                model.addAttribute(
                        "unreadNotificationCount", 0L);
            }
        }
    }
}