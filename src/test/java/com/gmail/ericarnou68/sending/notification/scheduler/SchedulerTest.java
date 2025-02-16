package com.gmail.ericarnou68.sending.notification.scheduler;

import com.gmail.ericarnou68.sending.notification.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SchedulerTest {

    @Mock
    NotificationService notificationService;

    @InjectMocks
    Scheduler scheduler;

    @Test
    @DisplayName("Should has correct date information")
    void whenSchedulerSendNotificationCalledExpectCorrectDateInformation(){
        //given
        doNothing().when(notificationService).sendNotification(any(LocalDateTime.class));

        //when
        scheduler.sendNotification();

        //then
        verify(notificationService, times(1)).sendNotification(any(LocalDateTime.class));
    }
}