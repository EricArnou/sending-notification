package com.gmail.ericarnou68.sending.notification.service;

import com.gmail.ericarnou68.sending.notification.entities.Chanel;
import com.gmail.ericarnou68.sending.notification.entities.Notification;
import com.gmail.ericarnou68.sending.notification.entities.Status;
import com.gmail.ericarnou68.sending.notification.entities.dto.CreatedNotificationDto;
import com.gmail.ericarnou68.sending.notification.entities.dto.ScheduleNotificationDto;
import com.gmail.ericarnou68.sending.notification.entities.dto.NotificationStatusDto;
import com.gmail.ericarnou68.sending.notification.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public ResponseEntity<CreatedNotificationDto> scheduleNotificationService(ScheduleNotificationDto scheduleNotificationDto, UriComponentsBuilder uriComponentsBuilder){
        var notification = new Notification(scheduleNotificationDto);
        notificationRepository.save(notification);
        var uri = uriComponentsBuilder.path("api/v1/notifications/{id}").buildAndExpand(notification.getId()).toUri();

        return ResponseEntity.created(uri).body(new CreatedNotificationDto(notification));
    }

    public ResponseEntity<NotificationStatusDto> getSchedulingStatus(UUID notificationId){
        var notification = notificationRepository.findById(notificationId);
        if(notification.isEmpty()) return ResponseEntity.notFound().build();
        var status = new NotificationStatusDto(notification.get());

        return ResponseEntity.ok(status);
    }

    @Transactional
    public ResponseEntity<NotificationStatusDto> cancelNotificationService(UUID notificationId) {
        var notification = notificationRepository.findById(notificationId);
        if (notification.isEmpty()) return ResponseEntity.notFound().build();
        notification.get().setStatus(Status.CANCELLED);

        return ResponseEntity.ok(new NotificationStatusDto(notification.get()));
    }

    public void sendNotification(LocalDateTime now) {
        var pendingNotifications = notificationRepository.findNotificationsByStatusAndSchedulingBefore(Status.PENDING, now);
    }
}
