package com.gmail.ericarnou68.sending.notification.controller;

import com.gmail.ericarnou68.sending.notification.entities.dto.ScheduleNotificationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/notifications")
public class NotificationController {

    @PostMapping()
    public void scheduleNotification(@RequestBody ScheduleNotificationDto scheduleNotificationDto) {}

    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationStatus(@PathVariable("id") UUID notificationId) {
        return ResponseEntity.ok(notificationId);
    }

    @PutMapping("/{id}")
    public void cancelNotification(@PathVariable("id") UUID notificationId) {}
}
