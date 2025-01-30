package com.gmail.ericarnou68.sending.notification.infra.exceptions;

import com.gmail.ericarnou68.sending.notification.infra.exceptions.dto.ErrorMessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity notValidArgs(MethodArgumentNotValidException exception){
        return ResponseEntity.badRequest().body(exception.getFieldErrors()
                .stream()
                .map(ErrorMessageDto::new));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity notValidIdType(MethodArgumentTypeMismatchException exception){
        return ResponseEntity.badRequest().body(new ErrorMessageDto(ErrorMessage.NOTIFICATION_ID_MUST_TO_BE_UUID.label));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity notValidDate(DateTimeParseException exception){
        return ResponseEntity.badRequest().body(new ErrorMessageDto(ErrorMessage.DATE_FORMAT_MUST_TO_BE.label));
    }

    @ExceptionHandler(SendNotificationException.class)
    public ResponseEntity SendNotificationBusinessRules(SendNotificationException exception){
        return ResponseEntity.badRequest().body(new ErrorMessageDto(exception.getMessage()));
    }
}
