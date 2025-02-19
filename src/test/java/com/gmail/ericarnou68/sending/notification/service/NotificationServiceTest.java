package com.gmail.ericarnou68.sending.notification.service;

import com.gmail.ericarnou68.sending.notification.entities.Chanel;
import com.gmail.ericarnou68.sending.notification.entities.Notification;
import com.gmail.ericarnou68.sending.notification.entities.Status;
import com.gmail.ericarnou68.sending.notification.entities.dto.CreatedNotificationDto;
import com.gmail.ericarnou68.sending.notification.entities.dto.ScheduleNotificationDto;
import com.gmail.ericarnou68.sending.notification.entities.dto.NotificationStatusDto;
import com.gmail.ericarnou68.sending.notification.infra.exceptions.ErrorMessage;
import com.gmail.ericarnou68.sending.notification.infra.exceptions.SendNotificationException;
import com.gmail.ericarnou68.sending.notification.repository.NotificationRepository;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;
import com.gmail.ericarnou68.sending.notification.infra.exceptions.ErrorMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static com.gmail.ericarnou68.sending.notification.TestAssistant.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private SqsTemplate sqsTemplate;

    @InjectMocks
    private NotificationService notificationService;

    
    
    @Nested
    class testScheduleNotificationService{
        
        @Test
        @DisplayName("When schedule information is right expect success")
        void whenScheduleInformationIsCorrectExpectSuccess() {
            //given
            ScheduleNotificationDto scheduleNotificationDto = new ScheduleNotificationDto(EMAIL_RECIPIENT, MESSAGE, FUTURE_DATE, Chanel.EMAIL.toString());
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
            Notification notification = new Notification(scheduleNotificationDto);
            
            when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
            //when
            ResponseEntity<CreatedNotificationDto> response = notificationService.scheduleNotificationService(scheduleNotificationDto, uriComponentsBuilder);

            //then
            assertNotNull(response);
            assertTrue(response.getStatusCode().isSameCodeAs(HttpStatus.CREATED));
            verify(notificationRepository, times(1)).save(any(Notification.class));
        }
    }
}