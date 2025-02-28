package com.gmail.ericarnou68.sending.notification.entities;

import com.gmail.ericarnou68.sending.notification.entities.dto.ScheduleNotificationDto;
import com.gmail.ericarnou68.sending.notification.infra.exceptions.ErrorMessage;
import com.gmail.ericarnou68.sending.notification.infra.exceptions.SendNotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.ActiveProfiles;

import static com.gmail.ericarnou68.sending.notification.TestAssistant.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class NotificationTest {

    @Test
    @DisplayName("Should create a Notification when every arguments are alright")
    void whenEveryArgumentsIsAlrightCreateNewNotification(){
        //given
        var notification = new ScheduleNotificationDto(EMAIL_RECIPIENT, MESSAGE, FUTURE_DATE, Channel.EMAIL.toString());

        //when
        var result = new Notification(notification);

        //then
        assertEquals(EMAIL_RECIPIENT, result.getRecipient());
        assertEquals(MESSAGE, result.getMessage());
        assertEquals(Channel.EMAIL, result.getChannel());
        assertEquals(FUTURE_DATE, result.getScheduling());
    }


    @ParameterizedTest
    @ValueSource(strings = {INVALID_CHANEL, ""})
    @DisplayName("Should thrown exception because channel does not exists or is empty")
    void whenChannelDoesNotExistsThrowException(String channel){
        //given
        var notification = new ScheduleNotificationDto(EMAIL_RECIPIENT, MESSAGE, FUTURE_DATE, channel);

        //when
        SendNotificationException exception = assertThrows(SendNotificationException.class,
                () -> new Notification(notification));
        //then
        assertEquals(ErrorMessage.CHANNEL_NOT_FOUND.label, exception.getMessage());
    }

    @Test
    @DisplayName("Should thrown exception because channel is null")
    void whenChannelIsNullExistsThrowException(){
        //given
        var notification = new ScheduleNotificationDto(EMAIL_RECIPIENT, MESSAGE, FUTURE_DATE, null);

        //when
        SendNotificationException exception = assertThrows(SendNotificationException.class,
                () -> new Notification(notification));
        //then
        assertEquals(ErrorMessage.CHANNEL_NOT_FOUND.label, exception.getMessage());
    }

    @Test
    @DisplayName("Should thrown exception because email recipient is not valid")
    void whenEmailRecipientIsNotValidThrowException(){
        //given
        var notification = new ScheduleNotificationDto(PHONE_RECIPIENT, MESSAGE, FUTURE_DATE, Channel.EMAIL.toString());

        //when
        SendNotificationException exception = assertThrows(SendNotificationException.class,
                () -> new Notification(notification));
        //then
        assertEquals(ErrorMessage.INVALID_EMAIL_CHANNEL.label, exception.getMessage());
    }

    @Test
    @DisplayName("Should thrown exception because phone recipient for whatsapp is not valid")
    void whenPhoneWhatsAppRecipientIsNotValidThrowException(){
        //given
        var notification = new ScheduleNotificationDto(EMAIL_RECIPIENT, MESSAGE, FUTURE_DATE, Channel.WHATSAPP.toString());

        //when
        SendNotificationException exception = assertThrows(SendNotificationException.class,
                () -> new Notification(notification));
        //then
        assertEquals(ErrorMessage.INVALID_PHONE_NUMBER_FOR_CHANNEL.label, exception.getMessage());
    }

    @Test
    @DisplayName("Should thrown exception because phone recipient for sms is not valid")
    void whenPhoneSmsRecipientIsNotValidThrowException(){
        //given
        var notification = new ScheduleNotificationDto(EMAIL_RECIPIENT, MESSAGE, FUTURE_DATE, Channel.SMS.toString());

        //when
        SendNotificationException exception = assertThrows(SendNotificationException.class,
                () -> new Notification(notification));
        //then
        assertEquals(ErrorMessage.INVALID_PHONE_NUMBER_FOR_CHANNEL.label, exception.getMessage());
    }

    @Test
    @DisplayName("Should thrown exception because push token is not valid")
    void whenPushTokenIsNotValidThrowException(){
        //given
        var notification = new ScheduleNotificationDto(EMAIL_RECIPIENT, MESSAGE, FUTURE_DATE, Channel.PUSH.toString());

        //when
        SendNotificationException exception = assertThrows(SendNotificationException.class,
                () -> new Notification(notification));
        //then
        assertEquals(ErrorMessage.INVALID_PUSH_CHANNEL.label, exception.getMessage());
    }

    @Test
    @DisplayName("Should thrown exception because scheduling is in the past")
    void whenSchedulingIsInThePastThrowException(){
        //given
        var notification = new ScheduleNotificationDto(EMAIL_RECIPIENT, MESSAGE, PAST_DATE, Channel.EMAIL.toString());

        //when
        SendNotificationException exception = assertThrows(SendNotificationException.class,
                () -> new Notification(notification));
        //then
        assertEquals(ErrorMessage.DATE_FORMAT_MUST_TO_BE_IN_THE_FUTURE.label, exception.getMessage());
    }

    @Test
    @DisplayName("Should thrown exception because scheduling is null")
    void whenSchedulingIsNullThrowException(){
        //given
        var notification = new ScheduleNotificationDto(EMAIL_RECIPIENT, MESSAGE, null, Channel.EMAIL.toString());

        //when
        SendNotificationException exception = assertThrows(SendNotificationException.class,
                () -> new Notification(notification));
        //then
        assertEquals(ErrorMessage.DATE_FORMAT_MUST_TO_BE.label, exception.getMessage());
    }
}