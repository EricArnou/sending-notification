package com.gmail.ericarnou68.sending.notification.infra.exceptions.dto;

import org.springframework.validation.FieldError;

public record FieldErrorsDto(String field, String message) {

    public FieldErrorsDto(FieldError fieldError){
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
