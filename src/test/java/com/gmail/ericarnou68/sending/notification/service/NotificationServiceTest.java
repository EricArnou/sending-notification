package com.gmail.ericarnou68.sending.notification.service;

import com.gmail.ericarnou68.sending.notification.entities.Channel;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.gmail.ericarnou68.sending.notification.TestAssistant.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private SqsTemplate sqsTemplate;

    @Mock
    private Notification notification;

    @InjectMocks
    private NotificationService notificationService;

    
    
    @Nested
    class testScheduleNotificationService{
        
        @Test
        @DisplayName("When schedule information is right expect success")
        void whenScheduleInformationIsCorrectExpectSuccess() throws Exception {
            //given
            var ScheduleNotificationDto = new ScheduleNotificationDto(EMAIL_RECIPIENT, MESSAGE, FUTURE_DATE, Channel.EMAIL.toString());
            var mockUriComponentsBuilder = UriComponentsBuilder.newInstance();
            var mockNotification = mock(Notification.class);
            
            when(notificationRepository.save(any(Notification.class))).thenReturn(mockNotification);

            //when
            ResponseEntity<CreatedNotificationDto> response = notificationService.scheduleNotificationService(ScheduleNotificationDto, mockUriComponentsBuilder);

            //then
            assertNotNull(response);
            assertTrue(response.getStatusCode().isSameCodeAs(HttpStatus.CREATED));
            verify(notificationRepository, times(1)).save(any(Notification.class));
        }

        @Test
        @DisplayName("When schedule information is right expect success")
        void whenScheduleInformormationIsNotCorrectExpectException() {
            //given
            var scheduleNotificationDto = new ScheduleNotificationDto(PHONE_RECIPIENT, MESSAGE, FUTURE_DATE, Channel.EMAIL.toString());
            var uriComponentsBuilder = UriComponentsBuilder.newInstance();

            //when
            SendNotificationException exception = assertThrows(SendNotificationException.class,
            () -> notificationService.scheduleNotificationService(scheduleNotificationDto, uriComponentsBuilder));


            //then
            assertEquals(ErrorMessage.INVALID_EMAIL_CHANNEL.label, exception.getMessage());
            verify(notificationRepository, times(0)).save(any(Notification.class));
        }
    }

    @Nested
    class testGetSchedulingStatus{
        
        @Test
        @DisplayName("When notification is found expect success")
        void whenNotificationIsFoundExpectSuccess() {
            //given
            var notificationId = UUID.randomUUID();
            var mockNotification = mock(Notification.class);
            when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(mockNotification));

            //when
            ResponseEntity<NotificationStatusDto> response = notificationService.getSchedulingStatus(notificationId);

            //then
            assertNotNull(response);
            assertTrue(response.getStatusCode().isSameCodeAs(HttpStatus.OK));
            verify(notificationRepository, times(1)).findById(notificationId);
        }

        @Test
        @DisplayName("When notification is not found expect exception")
        void whenNotificationIsNotFoundExpectException() {
            //given
            var notificationId = UUID.randomUUID();
            when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

            //when
            SendNotificationException exception = assertThrows(SendNotificationException.class,
            () -> notificationService.getSchedulingStatus(notificationId));

            //then
            assertEquals(ErrorMessage.NOTIFICATION_NOT_FOUND.label, exception.getMessage());
            verify(notificationRepository, times(1)).findById(notificationId);
        }
    }

    @Nested
    class cancelNotificationService{
        
        @Test
        @DisplayName("When notification is found and is not in the past expect success")
        void whenNotificationIsFoundAndIsNotInThePastExpectSuccess() {
            //given
            var notificationId = UUID.randomUUID();
            var mockNotification = mock(Notification.class);
            when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(mockNotification));
            when(mockNotification.getScheduling()).thenReturn(FUTURE_DATE);

            //when
            ResponseEntity<NotificationStatusDto> response = notificationService.cancelNotificationService(notificationId);

            //then
            assertNotNull(response);
            assertTrue(response.getStatusCode().isSameCodeAs(HttpStatus.OK));
            verify(notificationRepository, times(1)).findById(notificationId);
            verify(mockNotification, times(1)).setStatus(Status.CANCELLED);
        }

        @Test
        @DisplayName("When notification is found and is in the past expect exception")
        void whenNotificationIsFoundAndIsInThePastExpectException() {
            //given
            var notificationId = UUID.randomUUID();
            var mockNotification = mock(Notification.class);
            when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(mockNotification));
            when(mockNotification.getScheduling()).thenReturn(PAST_DATE);

            //when
            SendNotificationException exception = assertThrows(SendNotificationException.class,
            () -> notificationService.cancelNotificationService(notificationId));

            //then
            assertEquals(ErrorMessage.FAILED_CANCEL_NOTIFICATION.label, exception.getMessage());
            verify(notificationRepository, times(1)).findById(notificationId);
            verify(mockNotification, times(0)).setStatus(Status.CANCELLED);
        }

        @Test
        @DisplayName("When notification is not found expect exception")
        void whenNotificationIsNotFoundExpectException() {
            //given
            var notificationId = UUID.randomUUID();
            when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

            //when
            SendNotificationException exception = assertThrows(SendNotificationException.class,
            () -> notificationService.cancelNotificationService(notificationId));

            //then
            assertEquals(ErrorMessage.NOTIFICATION_NOT_FOUND.label, exception.getMessage());
            verify(notificationRepository, times(1)).findById(notificationId);
        }
    }
    
    @Nested
    class changeToWaitingSentNotifications{

        @Test
        @DisplayName("When notifications are pending expect success")
        void whenNotificationsArePendingExpectSuccess() {
            //given
            var mockNotification = mock(Notification.class);
            var pendingNotifications = List.of(mockNotification, mockNotification);

            //when
            notificationService.changeToWaitingSentNotifications(pendingNotifications);

            //then
            verify(mockNotification, times(2)).setStatus(Status.WAITING);
        }
    }

    @Nested
    class updateStatus{

        @Test
        @DisplayName("When notifications are pending expect success")
        void whenNotificationsArePendingExpectSuccess() {
            //given
            var notificationId = UUID.randomUUID();
            var notification = mock(Notification.class);

            when(notificationRepository.getReferenceById(notificationId)).thenReturn(notification);
        
            //when
            notificationService.updateStatus(notificationId, Status.FAILED);

            //then
            verify(notificationRepository, times(1)).getReferenceById(notificationId);
            verify(notification, times(1)).setStatus(Status.FAILED);
        }
    }
}