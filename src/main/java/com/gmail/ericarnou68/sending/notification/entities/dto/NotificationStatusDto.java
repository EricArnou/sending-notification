package com.gmail.ericarnou68.sending.notification.entities.dto;

import com.gmail.ericarnou68.sending.notification.entities.Notification;
import com.gmail.ericarnou68.sending.notification.entities.Status;

import java.util.UUID;

public record NotificationStatusDto(UUID notificationId, Status status) {

    public NotificationStatusDto(Notification notification){
        this(notification.getId(), notification.getStatus());
    }
}
