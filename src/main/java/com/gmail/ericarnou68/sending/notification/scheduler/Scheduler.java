package com.gmail.ericarnou68.sending.notification.scheduler;

import com.gmail.ericarnou68.sending.notification.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class Scheduler {

    private final NotificationService notificationService;

    public Scheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void sendNotification(){
        var now = LocalDateTime.now();
        notificationService.sendNotification(now);
    }
}
