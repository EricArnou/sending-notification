package com.gmail.ericarnou68.sending.notification.infra.exceptions;

import com.gmail.ericarnou68.sending.notification.infra.exceptions.dto.ErrorMessageDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionsHandler {

    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorMessageDto> notValidArgs(MethodArgumentNotValidException exception){
        return exception.getFieldErrors().stream()
                .map(error -> new ErrorMessageDto(error))
                .collect(Collectors.toList());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageDto notValidIdType(MethodArgumentTypeMismatchException exception){
        return new ErrorMessageDto(ErrorMessage.NOTIFICATION_ID_MUST_TO_BE_UUID.label);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageDto notValidDate(DateTimeParseException exception){
        return new ErrorMessageDto(ErrorMessage.DATE_FORMAT_MUST_TO_BE.label);
    }

    @ExceptionHandler(SendNotificationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageDto SendNotificationBusinessRules(SendNotificationException exception){
        return new ErrorMessageDto(exception.getMessage());
    }
}
