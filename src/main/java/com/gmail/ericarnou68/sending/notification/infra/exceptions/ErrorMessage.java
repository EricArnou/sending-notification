package com.gmail.ericarnou68.sending.notification.infra.exceptions;

public enum ErrorMessage {
    GENERIC_MESSAGE("Generic message");

    final String label;

    ErrorMessage(String label) {
        this.label = label;
    }
}
