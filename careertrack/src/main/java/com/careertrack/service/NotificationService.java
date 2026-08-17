package com.careertrack.service;

import com.careertrack.model.Notification;
import com.careertrack.model.User;

import java.util.List;

public interface NotificationService {

    Notification createNotification(
            User user,
            String title,
            String message,
            String type);

    List<Notification> getAllNotifications(User user);

    List<Notification> getUnreadNotifications(User user);

    Long getUnreadCount(User user);

    void markAsRead(Long id, User user);

    void markAllAsRead(User user);
}