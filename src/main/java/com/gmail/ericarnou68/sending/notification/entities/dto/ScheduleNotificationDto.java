package com.gmail.ericarnou68.sending.notification.entities.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmail.ericarnou68.sending.notification.entities.Chanel;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record ScheduleNotificationDto(

        @NotBlank
        String recipient,
        
        @NotNull
        String message,
        
        @Future
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        @NotNull
        LocalDateTime scheduling,
        
        @NotNull
        Chanel chanel) {
}
