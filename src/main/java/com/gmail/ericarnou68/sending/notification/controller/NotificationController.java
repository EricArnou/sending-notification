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
        var createdNotificationDto = notificationService.scheduleNotificationService(scheduleNotificationDto);
        
        var uri = uriComponentsBuilder.path("api/v1/notifications/{id}").buildAndExpand(createdNotificationDto.notificationId()).toUri();
        return ResponseEntity.created(uri).body(createdNotificationDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationStatusDto> getNotificationStatus(@PathVariable("id") UUID notificationId) {
        var notificationStatusDto = notificationService.getSchedulingStatus(notificationId);

        return ResponseEntity.ok(notificationStatusDto);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<NotificationStatusDto> cancelNotification(@PathVariable("id") UUID notificationId) {

        var notificationStatusDto = notificationService.cancelNotificationService(notificationId);
        return ResponseEntity.ok(notificationStatusDto);
    }
}
