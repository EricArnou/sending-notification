package com.gmail.ericarnou68.sending.notification.service;

import com.gmail.ericarnou68.sending.notification.entities.Chanel;
import com.gmail.ericarnou68.sending.notification.entities.Notification;
import com.gmail.ericarnou68.sending.notification.entities.Status;
import com.gmail.ericarnou68.sending.notification.entities.dto.CreatedNotificationDto;
import com.gmail.ericarnou68.sending.notification.entities.dto.ScheduleNotificationDto;
import com.gmail.ericarnou68.sending.notification.entities.dto.NotificationStatusDto;
import com.gmail.ericarnou68.sending.notification.entities.dto.SendNotificationEmailChanelDto;
import com.gmail.ericarnou68.sending.notification.repository.NotificationRepository;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SqsTemplate sqsTemplate;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final String QueueUri = "https://localhost.localstack.cloud:4566/000000000000/sending-email-queue";

    public NotificationService(NotificationRepository notificationRepository, SqsTemplate sqsTemplate){
        this.notificationRepository = notificationRepository;
        this.sqsTemplate = sqsTemplate;
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

        sendEmailNotifications(pendingNotifications);
        sendWhatsAppNotifications(pendingNotifications);
        sendSmsNotifications(pendingNotifications);
        sendPushNotifications(pendingNotifications);
    }

    private void sendWhatsAppNotifications(List<Notification> pendingNotifications) {
        logger.info("Sending WhatsApp Notification");
    }

    private void sendSmsNotifications(List<Notification> pendingNotifications) {
        logger.info("Sending Sms Notification");
    }

    private void sendPushNotifications(List<Notification> pendingNotifications) {
        logger.info("Sending Push Notification");
    }

    private void sendEmailNotifications(List<Notification> pendingNotifications) {
        logger.info("Sending Email Notification");
        pendingNotifications.stream()
                .filter(notification -> notification.getChanel() == Chanel.EMAIL)
                .toList()
                .forEach(notification -> sqsTemplate.send(QueueUri, new SendNotificationEmailChanelDto(notification)));
    }

    @Transactional
    public void updateStatus(UUID notificationId, Status status) {
        var notification = notificationRepository.getReferenceById(notificationId);
        notification.setStatus(status);
        logger.info("updated status notification {}", notificationId);
    }
}
