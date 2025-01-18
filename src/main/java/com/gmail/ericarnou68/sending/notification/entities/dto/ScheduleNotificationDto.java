package com.gmail.ericarnou68.sending.notification.entities.dto;

import com.gmail.ericarnou68.sending.notification.entities.Chanel;
import java.time.LocalDateTime;

public record ScheduleNotificationDto(String recipient, String message, LocalDateTime scheduling, Chanel chanel) {
}
