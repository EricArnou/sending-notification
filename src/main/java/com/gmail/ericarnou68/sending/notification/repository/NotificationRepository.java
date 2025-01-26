package com.gmail.ericarnou68.sending.notification.repository;

import com.gmail.ericarnou68.sending.notification.entities.Notification;
import com.gmail.ericarnou68.sending.notification.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findNotificationsByStatusAndSchedulingBefore(Status status, LocalDateTime scheduling);
}
