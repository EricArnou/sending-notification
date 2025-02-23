package com.gmail.ericarnou68.sending.notification.entities;

import com.gmail.ericarnou68.sending.notification.entities.dto.ScheduleNotificationDto;
import com.gmail.ericarnou68.sending.notification.infra.exceptions.ErrorMessage;
import com.gmail.ericarnou68.sending.notification.infra.exceptions.SendNotificationException;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String recipient;

    private String message;
    private LocalDateTime scheduling;
    @Enumerated(EnumType.STRING)
    private Channel channel;

    @Enumerated(EnumType.STRING)
    private Status status;

    private static final Logger logger = LoggerFactory.getLogger(Notification.class);

    public Notification(){

    }

    public Notification(ScheduleNotificationDto scheduleNotificationDto){
        validNotification(scheduleNotificationDto);

        setRecipient(scheduleNotificationDto.recipient());
        setMessage(scheduleNotificationDto.message());
        setchannel(Channel.valueOf(scheduleNotificationDto.channel()));
        setScheduling(scheduleNotificationDto.scheduling());
        setStatus(Status.PENDING);
    }

    public Notification(String recipient, String message, LocalDateTime scheduling, String channel) {
        setRecipient(recipient);
        setMessage(message);
        setchannel(Channel.valueOf(channel));
        setScheduling(scheduling);
        setStatus(Status.PENDING);
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getScheduling() {
        return scheduling;
    }

    public void setScheduling(LocalDateTime localDateTime) {
        this.scheduling = localDateTime;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setchannel(Channel channel) {
        this.channel = channel;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public UUID getId(){
        return this.id;
    }

    public static void validNotification(ScheduleNotificationDto scheduleNotificationDto) throws SendNotificationException {
        boolean validEmail;
        boolean validPhoneNumber;
        boolean validPushToken;

        if(scheduleNotificationDto.recipient() == null || scheduleNotificationDto.recipient().isEmpty()){
            logger.error("Recipient is empty");
            throw new SendNotificationException(ErrorMessage.RECIPIENT_EMPTY);
        }

        if(scheduleNotificationDto.message() == null){
            logger.error("Message is empty");
            throw new SendNotificationException(ErrorMessage.MESSAGE_EMPTY);
        }
        
        if(scheduleNotificationDto.scheduling() == null){
            logger.error("Scheduling is empty");
            throw new SendNotificationException(ErrorMessage.DATE_FORMAT_MUST_TO_BE);
        }

        if(scheduleNotificationDto.scheduling().isBefore(LocalDateTime.now())){
            logger.error("Scheduling must be in the future");
            throw new SendNotificationException(ErrorMessage.DATE_FORMAT_MUST_TO_BE_IN_THE_FUTURE);
        }

        try{
            Channel.valueOf(scheduleNotificationDto.channel());
        } catch (IllegalArgumentException e) {
            logger.error("channel not found");
            throw new SendNotificationException(ErrorMessage.CHANNEL_NOT_FOUND);
        } catch (NullPointerException e) {
            logger.error("channel is null");
            throw new SendNotificationException(ErrorMessage.CHANNEL_NOT_FOUND);
        }

        validEmail = scheduleNotificationDto.recipient().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        validPhoneNumber = scheduleNotificationDto.recipient().matches("^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}[-]?\\d{4}$");
        validPushToken = scheduleNotificationDto.recipient().matches("^[a-zA-Z0-9\\-_\\.]{16,128}$");

        if((Channel.valueOf(scheduleNotificationDto.channel()) == Channel.EMAIL) && !validEmail) {
            logger.error("Email not valid");
            throw new SendNotificationException(ErrorMessage.INVALID_EMAIL_CHANNEL);
        }

        if(((Channel.valueOf(scheduleNotificationDto.channel()) == Channel.SMS || Channel.valueOf(scheduleNotificationDto.channel()) == Channel.WHATSAPP) && !validPhoneNumber)) {
            logger.error("Phone number not valid");
            throw new SendNotificationException(ErrorMessage.INVALID_PHONE_NUMBER_FOR_CHANNEL);
        }

        if((Channel.valueOf(scheduleNotificationDto.channel()) == Channel.PUSH) && !validPushToken) {
            logger.error("Push token not valid");
            throw new SendNotificationException(ErrorMessage.INVALID_PUSH_CHANNEL);
        }
    }
}
