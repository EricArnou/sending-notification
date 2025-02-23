package com.gmail.ericarnou68.sending.notification.entities.dto;

import com.gmail.ericarnou68.sending.notification.entities.Channel;
import com.gmail.ericarnou68.sending.notification.entities.Notification;
import com.gmail.ericarnou68.sending.notification.entities.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreatedNotificationDto(UUID notificationId, String recipient, String message, LocalDateTime scheduling, Channel channel, Status status) {
    public CreatedNotificationDto(Notification notification){
        this(notification.getId(), notification.getRecipient(), notification.getMessage(), notification.getScheduling(), notification.getChannel(), notification.getStatus());
    }

}
