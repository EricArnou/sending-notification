package com.gmail.ericarnou68.sending.notification.repository;

import com.gmail.ericarnou68.sending.notification.entities.Channel;
import com.gmail.ericarnou68.sending.notification.entities.Notification;
import com.gmail.ericarnou68.sending.notification.entities.Status;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static com.gmail.ericarnou68.sending.notification.TestAssistant.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NotificationRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    NotificationRepository notificationRepository;

    @Test
    @DisplayName("Should return a notification")
    void whenExistsPendingNotificationAndBeforeDateExpectNotification() {
        //given
        var notification = new Notification(EMAIL_RECIPIENT, MESSAGE, PAST_DATE, Channel.EMAIL.toString());
        entityManager.persist(notification);

        //when
        List<Notification> notificationList = notificationRepository.findNotificationsByStatusAndSchedulingBefore(Status.PENDING, LocalDateTime.now());

        //then
        assertEquals(1, notificationList.size());
        var result = notificationList.getFirst();
        assertEquals(EMAIL_RECIPIENT, result.getRecipient());
        assertEquals(MESSAGE, result.getMessage());
        assertEquals(Channel.EMAIL, result.getChannel());
        assertEquals(Status.PENDING, result.getStatus());
    }

    @Test
    @DisplayName("Should not return a notification when scheduling is a future date")
    void whenExistsPendingNotificationAndAfterDateExpectNotification() {
        //given
        var notification = new Notification(EMAIL_RECIPIENT, MESSAGE, FUTURE_DATE, Channel.EMAIL.toString());
        entityManager.persist(notification);

        //when
        List<Notification> notificationList = notificationRepository.findNotificationsByStatusAndSchedulingBefore(Status.PENDING, LocalDateTime.now());

        //then
        assertEquals(0, notificationList.size());
    }

    @Test
    @DisplayName("Should not return a notification when status  is not pending")
    void whenNotExistsPendingNotificationAndBeforeDateExpectNotification(){
        //given
        var notification = new Notification(EMAIL_RECIPIENT, MESSAGE, PAST_DATE, Channel.EMAIL.toString());
        notification.setStatus(Status.SUCCESS);
        entityManager.persist(notification);

        //when
        List<Notification> notificationList = notificationRepository.findNotificationsByStatusAndSchedulingBefore(Status.PENDING, LocalDateTime.now());

        //then
        assertEquals(0, notificationList.size());
    }
}