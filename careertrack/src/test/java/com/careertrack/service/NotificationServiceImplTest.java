package com.careertrack.service;

import com.careertrack.model.Notification;
import com.careertrack.model.User;
import com.careertrack.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl service;

    @Mock
    private User user;

    @Mock
    private User otherUser;

    @Test
    void createNotification_shouldCreateUnreadNotification() {

        when(notificationRepository.save(
                any(Notification.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        Notification result =
                service.createNotification(
                        user,
                        "Interview Reminder",
                        "Your interview is tomorrow",
                        "INTERVIEW"
                );

        assertEquals(
                "Interview Reminder",
                result.getTitle()
        );

        assertEquals(
                "Your interview is tomorrow",
                result.getMessage()
        );

        assertEquals(
                "INTERVIEW",
                result.getType()
        );

        assertFalse(result.getIsRead());

        assertSame(user, result.getUser());

        verify(notificationRepository)
                .save(any(Notification.class));
    }

    @Test
    void getAllNotifications_shouldReturnUserNotifications() {

        List<Notification> notifications =
                List.of(
                        Notification.builder()
                                .title("Test 1")
                                .build(),
                        Notification.builder()
                                .title("Test 2")
                                .build()
                );

        when(notificationRepository
                .findByUserOrderByCreatedAtDesc(user))
                .thenReturn(notifications);

        List<Notification> result =
                service.getAllNotifications(user);

        assertEquals(2, result.size());
        assertSame(notifications, result);
    }

    @Test
    void getUnreadNotifications_shouldReturnUnreadNotifications() {

        List<Notification> notifications =
                List.of(
                        Notification.builder()
                                .title("Unread")
                                .isRead(false)
                                .build()
                );

        when(notificationRepository
                .findByUserAndIsRead(user, false))
                .thenReturn(notifications);

        List<Notification> result =
                service.getUnreadNotifications(user);

        assertEquals(1, result.size());

        verify(notificationRepository)
                .findByUserAndIsRead(user, false);
    }

    @Test
    void getUnreadCount_shouldReturnRepositoryCount() {

        when(notificationRepository
                .countByUserAndIsRead(user, false))
                .thenReturn(5L);

        Long result =
                service.getUnreadCount(user);

        assertEquals(5L, result);

        verify(notificationRepository)
                .countByUserAndIsRead(user, false);
    }

    @Test
    void markAsRead_shouldMarkOwnNotificationAsRead() {

        when(user.getId()).thenReturn(1L);

        Notification notification =
                Notification.builder()
                        .title("Interview")
                        .isRead(false)
                        .user(user)
                        .build();

        when(notificationRepository.findById(10L))
                .thenReturn(Optional.of(notification));

        service.markAsRead(10L, user);

        assertTrue(notification.getIsRead());

        verify(notificationRepository)
                .save(notification);
    }

    @Test
    void markAsRead_shouldDenyAccessToAnotherUsersNotification() {

        when(user.getId()).thenReturn(1L);
        when(otherUser.getId()).thenReturn(2L);

        Notification notification =
                Notification.builder()
                        .title("Private notification")
                        .isRead(false)
                        .user(otherUser)
                        .build();

        when(notificationRepository.findById(10L))
                .thenReturn(Optional.of(notification));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> service.markAsRead(10L, user)
                );

        assertEquals(
                "Access denied!",
                exception.getMessage()
        );

        assertFalse(notification.getIsRead());

        verify(notificationRepository, never())
                .save(any(Notification.class));
    }

    @Test
    void markAllAsRead_shouldMarkAllUnreadNotifications() {

        Notification first =
                Notification.builder()
                        .title("First")
                        .isRead(false)
                        .user(user)
                        .build();

        Notification second =
                Notification.builder()
                        .title("Second")
                        .isRead(false)
                        .user(user)
                        .build();

        List<Notification> unread =
                List.of(first, second);

        when(notificationRepository
                .findByUserAndIsRead(user, false))
                .thenReturn(unread);

        service.markAllAsRead(user);

        assertTrue(first.getIsRead());
        assertTrue(second.getIsRead());

        verify(notificationRepository)
                .saveAll(unread);
    }
}