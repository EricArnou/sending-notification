package com.gmail.ericarnou68.sending.notification.scheduler;

import com.gmail.ericarnou68.sending.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class Scheduler {

    private final NotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    public Scheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void sendNotification(){
        logger.info("Scheduler was called");
        var now = LocalDateTime.now();

        notificationService.sendNotification(now);
    }
}
