package com.gmail.ericarnou68.sending.notification.infra.exceptions;

public enum ErrorMessage {
    NOTIFICATION_ID_MUST_TO_BE_UUID("notification id must to be UUID"),
    DATE_FORMAT_MUST_TO_BE("Date format must to be dd/MM/yyyy HH:mm");

    final String label;

    ErrorMessage(String label) {
        this.label = label;
    }
}
