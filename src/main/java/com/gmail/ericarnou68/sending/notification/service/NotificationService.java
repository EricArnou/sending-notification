package com.gmail.ericarnou68.sending.notification.service;

import com.gmail.ericarnou68.sending.notification.entities.Channel;
import com.gmail.ericarnou68.sending.notification.entities.Notification;
import com.gmail.ericarnou68.sending.notification.entities.Status;
import com.gmail.ericarnou68.sending.notification.entities.dto.CreatedNotificationDto;
import com.gmail.ericarnou68.sending.notification.entities.dto.ScheduleNotificationDto;
import com.gmail.ericarnou68.sending.notification.entities.dto.NotificationStatusDto;
import com.gmail.ericarnou68.sending.notification.entities.dto.SendNotificationDto;
import com.gmail.ericarnou68.sending.notification.infra.exceptions.ErrorMessage;
import com.gmail.ericarnou68.sending.notification.infra.exceptions.SendNotificationException;
import com.gmail.ericarnou68.sending.notification.repository.NotificationRepository;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SqsTemplate sqsTemplate;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Value("${aws.sqs.email.queue.uri}")
    private String emailQueueUri;

    @Value("${aws.sqs.sms.queue.uri}")
    private String smsQueueUri;
    
    @Value("${aws.sqs.push.queue.uri}")
    private String pushQueueUri;
    
    @Value("${aws.sqs.whatsapp.queue.uri}")
    private String whatsappQueueUri;

    private Map<Channel, String> queueUriMap = new HashMap<>();
    
    @PostConstruct
    public void init(){
        queueUriMap.put(Channel.EMAIL, emailQueueUri);
        queueUriMap.put(Channel.SMS, smsQueueUri);
        queueUriMap.put(Channel.PUSH, pushQueueUri);
        queueUriMap.put(Channel.WHATSAPP, whatsappQueueUri);
    }
    public NotificationService(NotificationRepository notificationRepository, SqsTemplate sqsTemplate){
        this.notificationRepository = notificationRepository;
        this.sqsTemplate = sqsTemplate;
    }

    @Transactional
    public ResponseEntity<CreatedNotificationDto> scheduleNotificationService(ScheduleNotificationDto scheduleNotificationDto, UriComponentsBuilder uriComponentsBuilder){

        var notification = new Notification(scheduleNotificationDto);
        notificationRepository.save(notification);
        logger.info("Notification {} was saved", notification.getId());

        var uri = uriComponentsBuilder.path("api/v1/notifications/{id}").buildAndExpand(notification.getId()).toUri();

        return ResponseEntity.created(uri).body(new CreatedNotificationDto(notification));
    }

    public ResponseEntity<NotificationStatusDto> getSchedulingStatus(UUID notificationId){
        var notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new SendNotificationException(ErrorMessage.NOTIFICATION_NOT_FOUND));

        var status = new NotificationStatusDto(notification);

        return ResponseEntity.ok(status);
    }

    @Transactional
    public ResponseEntity<NotificationStatusDto> cancelNotificationService(UUID notificationId) {
        var notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new SendNotificationException(ErrorMessage.NOTIFICATION_NOT_FOUND));

        if(notification.getScheduling().isBefore(LocalDateTime.now()))
            throw new SendNotificationException(ErrorMessage.FAILED_CANCEL_NOTIFICATION);

        notification.setStatus(Status.CANCELLED);
        logger.info("Notification {} was canceled", notification.getId());

        return ResponseEntity.ok(new NotificationStatusDto(notification));
    }

    @Transactional
    public void sendNotification(LocalDateTime now) {
        var pendingNotifications = notificationRepository.findNotificationsByStatusAndSchedulingBefore(Status.PENDING, now);

        pendingNotifications.stream()
                .peek(notification -> notification.setStatus(Status.WAITING))
                .forEach(notification -> sqsTemplate.send(queueUriMap.get(notification.getChannel()), new SendNotificationDto(notification)));
    }

    @Transactional
    public void changeToWaitingSentNotifications(List<Notification> pendingNotifications){
        pendingNotifications
                .forEach(notification -> notification.setStatus(Status.WAITING));
    }

    @Transactional
    public void updateStatus(UUID notificationId, Status status) {
        var notification = notificationRepository.getReferenceById(notificationId);
        notification.setStatus(status);
        logger.info("Updated status notification {}", notificationId);
    }
}

