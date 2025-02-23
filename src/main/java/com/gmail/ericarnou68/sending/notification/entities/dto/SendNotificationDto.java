package com.gmail.ericarnou68.sending.notification.entities.dto;

import com.gmail.ericarnou68.sending.notification.entities.Notification;

import java.util.UUID;

public record SendNotificationDto(UUID notificationId, String channel, String message) {

    public SendNotificationDto(Notification notification){
        this(notification.getId(), notification.getRecipient(), notification.getMessage());
    }
}
