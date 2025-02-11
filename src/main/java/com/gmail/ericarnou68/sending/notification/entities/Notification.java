package com.gmail.ericarnou68.sending.notification.entities;

import com.gmail.ericarnou68.sending.notification.entities.dto.ScheduleNotificationDto;
import com.gmail.ericarnou68.sending.notification.infra.exceptions.ErrorMessage;
import com.gmail.ericarnou68.sending.notification.infra.exceptions.SendNotificationException;
import com.gmail.ericarnou68.sending.notification.service.NotificationService;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private Chanel chanel;

    @Enumerated(EnumType.STRING)
    private Status status;

    private static final Logger logger = LoggerFactory.getLogger(Notification.class);

    public Notification(){

    }

    public Notification(ScheduleNotificationDto scheduleNotificationDto){
        validNotification(scheduleNotificationDto);

        setRecipient(scheduleNotificationDto.recipient());
        setMessage(scheduleNotificationDto.message());
        setChanel(Chanel.valueOf(scheduleNotificationDto.chanel()));
        setScheduling(scheduleNotificationDto.scheduling());
        setStatus(Status.PENDING);
    }

    public Notification(String recipient, String message, LocalDateTime scheduling, String chanel) {
        setRecipient(recipient);
        setMessage(message);
        setChanel(Chanel.valueOf(chanel));
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        scheduling.format(formatter);
    }

    public Chanel getChanel() {
        return chanel;
    }

    public void setChanel(Chanel chanel) {
        this.chanel = chanel;
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

    private static void validNotification(ScheduleNotificationDto scheduleNotificationDto) {
        boolean validEmail;
        boolean validPhoneNumber;
        boolean validPushToken;

        try{
            Chanel.valueOf(scheduleNotificationDto.chanel());
        } catch (IllegalArgumentException e) {
            logger.error("Chanel not found");
            throw new SendNotificationException(ErrorMessage.CHANEL_NOT_FOUND);
        }

        validEmail = scheduleNotificationDto.recipient().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        validPhoneNumber = scheduleNotificationDto.recipient().matches("^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}[-]?\\d{4}$");
        validPushToken = scheduleNotificationDto.recipient().matches("^[a-zA-Z0-9\\-_\\.]{16,128}$");

        if((Chanel.valueOf(scheduleNotificationDto.chanel()) == Chanel.EMAIL) && !validEmail) {
            logger.error("Email not valid");
            throw new SendNotificationException(ErrorMessage.INVALID_EMAIL_CHANEL);
        }

        if(((Chanel.valueOf(scheduleNotificationDto.chanel()) == Chanel.SMS || Chanel.valueOf(scheduleNotificationDto.chanel()) == Chanel.WHATSAPP) && !validPhoneNumber)) {
            logger.error("Phone number not valid");
            throw new SendNotificationException(ErrorMessage.INVALID_PHONE_NUMBER_FOR_CHANEL);
        }

        if((Chanel.valueOf(scheduleNotificationDto.chanel()) == Chanel.PUSH) && !validPushToken) {
            logger.error("Push token not valid");
            throw new SendNotificationException(ErrorMessage.INVALID_PUSH_CHANEL);
        }
    }
}
