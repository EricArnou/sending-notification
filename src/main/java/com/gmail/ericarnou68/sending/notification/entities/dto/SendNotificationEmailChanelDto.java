package com.gmail.ericarnou68.sending.notification.entities.dto;

import com.gmail.ericarnou68.sending.notification.entities.Notification;

import java.util.UUID;

public record SendNotificationEmailChanelDto(UUID notificationId, String email, String message) {

    public SendNotificationEmailChanelDto(Notification notification){
        this(notification.getId(), notification.getRecipient(), notification.getMessage());
    }
}
