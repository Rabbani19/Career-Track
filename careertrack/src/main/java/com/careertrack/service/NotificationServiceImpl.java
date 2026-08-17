package com.careertrack.service;

import com.careertrack.model.Notification;
import com.careertrack.model.User;
import com.careertrack.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl
        implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification createNotification(
            User user,
            String title,
            String message,
            String type) {

        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .user(user)
                .build();

        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getAllNotifications(
            User user) {
        return notificationRepository
                .findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public List<Notification> getUnreadNotifications(
            User user) {
        return notificationRepository
                .findByUserAndIsRead(user, false);
    }

    @Override
    public Long getUnreadCount(User user) {
        return notificationRepository
                .countByUserAndIsRead(user, false);
    }

    @Override
    public void markAsRead(Long id, User user) {

        Notification notification = notificationRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Notification not found!"));

        if (!notification.getUser().getId()
                .equals(user.getId())) {
            throw new RuntimeException(
                    "Access denied!");
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead(User user) {

        List<Notification> unread = notificationRepository
                .findByUserAndIsRead(user, false);

        unread.forEach(n -> n.setIsRead(true));

        notificationRepository.saveAll(unread);
    }
}