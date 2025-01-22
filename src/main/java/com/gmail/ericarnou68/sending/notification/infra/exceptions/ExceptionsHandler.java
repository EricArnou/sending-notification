package com.gmail.ericarnou68.sending.notification.infra.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(SendNotificationException.class)
    public ResponseEntity SendNotificationBusinessRules(SendNotificationException sendNotificationException){
        return ResponseEntity.badRequest().body(new ErroMessageDto(sendNotificationException.getMessage()));
    }
}
