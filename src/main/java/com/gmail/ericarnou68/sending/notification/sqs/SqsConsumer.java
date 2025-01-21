package com.gmail.ericarnou68.sending.notification.sqs;

import com.gmail.ericarnou68.sending.notification.entities.dto.NotificationStatusDto;
import com.gmail.ericarnou68.sending.notification.service.NotificationService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SqsConsumer {

    private final NotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(SqsConsumer.class);

    public SqsConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @SqsListener("update-status")
    public void updateStatus(NotificationStatusDto notificationStatusDto){
        logger.info("Receiving update Status");
        notificationService.updateStatus(notificationStatusDto.notificationId(), notificationStatusDto.status());
    }
}
