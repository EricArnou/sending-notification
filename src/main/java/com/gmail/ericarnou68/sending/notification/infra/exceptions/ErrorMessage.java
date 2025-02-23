package com.gmail.ericarnou68.sending.notification.infra.exceptions;

public enum ErrorMessage {
    NOTIFICATION_ID_MUST_TO_BE_UUID("notification id must to be UUID"),
    DATE_FORMAT_MUST_TO_BE("Date format must to be dd/MM/yyyy HH:m"),
    NOTIFICATION_NOT_FOUND("notification id was not found"),
    CHANNEL_NOT_FOUND("Channel must be EMAIL, SMS, WHATSAPP OR PUSH"),
    INVALID_EMAIL_CHANNEL("Email is not valid"),
    INVALID_PHONE_NUMBER_FOR_CHANNEL("Phone number is not valid"),
    INVALID_WHATSAPP_CHANNEL("Phone number is not valid"),
    INVALID_PUSH_CHANNEL("Push token is not valid"),
    DATE_FORMAT_MUST_TO_BE_IN_THE_FUTURE("Date must be in the future"),
    RECIPIENT_EMPTY("Recipient is empty"),
    MESSAGE_EMPTY("Message is empty"),
    FAILED_CANCEL_NOTIFICATION("Notifications only can be canceled if now is before the send date");

    public final String label;

    ErrorMessage(String label) {
        this.label = label;
    }
}
