package com.gmail.ericarnou68.sending.notification.controller;

import com.gmail.ericarnou68.sending.notification.entities.dto.CreatedNotificationDto;
import com.gmail.ericarnou68.sending.notification.entities.dto.NotificationStatusDto;
import com.gmail.ericarnou68.sending.notification.entities.dto.ScheduleNotificationDto;
import com.gmail.ericarnou68.sending.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping()
    public ResponseEntity<CreatedNotificationDto> scheduleNotification(@RequestBody @Valid ScheduleNotificationDto scheduleNotificationDto, UriComponentsBuilder uriComponentsBuilder) {
        return notificationService.scheduleNotificationService(scheduleNotificationDto, uriComponentsBuilder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationStatusDto> getNotificationStatus(@PathVariable("id") UUID notificationId) {
        return notificationService.getSchedulingStatus(notificationId);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<NotificationStatusDto> cancelNotification(@PathVariable("id") UUID notificationId) {
        return notificationService.cancelNotificationService(notificationId);
    }
}
