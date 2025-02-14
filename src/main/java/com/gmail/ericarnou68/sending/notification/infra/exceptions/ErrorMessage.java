package com.gmail.ericarnou68.sending.notification.infra.exceptions;

public enum ErrorMessage {
    NOTIFICATION_ID_MUST_TO_BE_UUID("notification id must to be UUID"),
    DATE_FORMAT_MUST_TO_BE("Date format must to be dd/MM/yyyy HH:m"),
    NOTIFICATION_NOT_FOUND("notification id was not found"),
    CHANEL_NOT_FOUND("Chanel must be EMAIL, SMS, WHATSAPP OR PUSH"),
    INVALID_EMAIL_CHANEL("Email is not valid"),
    INVALID_PHONE_NUMBER_FOR_CHANEL("Phone number is not valid"),
    INVALID_WHATSAPP_CHANEL("Phone number is not valid"),
    INVALID_PUSH_CHANEL("Push token is not valid"),
    FAILED_CANCEL_NOTIFICATION("Notifications only can be canceled if now is before the send date");

    public final String label;

    ErrorMessage(String label) {
        this.label = label;
    }
}
