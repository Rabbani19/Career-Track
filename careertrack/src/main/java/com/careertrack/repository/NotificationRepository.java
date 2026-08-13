package com.careertrack.repository;

import com.careertrack.model.Notification;
import com.careertrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(
            User user);

    List<Notification> findByUserAndIsRead(
            User user, Boolean isRead);

    Long countByUserAndIsRead(
            User user, Boolean isRead);
}