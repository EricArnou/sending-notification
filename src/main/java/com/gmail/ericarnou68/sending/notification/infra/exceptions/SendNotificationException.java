package com.gmail.ericarnou68.sending.notification.infra.exceptions;

public class SendNotificationException extends RuntimeException {
    public SendNotificationException(ErrorMessage errorMessage) {
        super(errorMessage.label);
    }
}
