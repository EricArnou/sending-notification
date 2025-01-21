package com.gmail.ericarnou68.sending.notification.entities.dto;

import com.gmail.ericarnou68.sending.notification.entities.Notification;

import java.util.UUID;

public record SendNotificationEmailChanel(UUID notificationId, String email, String message) {

    public SendNotificationEmailChanel(Notification notification){
        this(notification.getId(), notification.getRecipient(), notification.getMessage());
    }
}
