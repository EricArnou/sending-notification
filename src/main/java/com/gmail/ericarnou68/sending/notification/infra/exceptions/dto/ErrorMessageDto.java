package com.gmail.ericarnou68.sending.notification.infra.exceptions.dto;

import org.springframework.validation.FieldError;

public record ErrorMessageDto(String error) {

    public ErrorMessageDto(FieldError fieldError){
        this(fieldError.getDefaultMessage());
    }
}
