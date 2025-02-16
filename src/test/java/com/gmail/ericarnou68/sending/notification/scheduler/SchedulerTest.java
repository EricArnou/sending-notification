package com.gmail.ericarnou68.sending.notification.scheduler;

import com.gmail.ericarnou68.sending.notification.service.NotificationService;
import org.hibernate.validator.constraints.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;

import static com.gmail.ericarnou68.sending.notification.TestAssistant.NOW;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SchedulerTest {

    @Mock
    NotificationService notificationService;

    @InjectMocks
    Scheduler scheduler;

    @Captor
    ArgumentCaptor<LocalDateTime> dateCaptor;

    @Test
    @DisplayName("Should has correct date information")
    void whenSchedulerSendNotificationCalledExpectCorrectDateInformation(){
        //given
        doNothing().when(notificationService).sendNotification(dateCaptor.capture());

        //when
        scheduler.sendNotification();

        //then
        verify(notificationService, times(1)).sendNotification(any(LocalDateTime.class));
        assertTrue(dateCaptor.getValue().isBefore(NOW));
    }
}