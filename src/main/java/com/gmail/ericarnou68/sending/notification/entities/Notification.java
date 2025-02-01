package com.gmail.ericarnou68.sending.notification.entities;

import com.gmail.ericarnou68.sending.notification.entities.dto.ScheduleNotificationDto;
import jakarta.persistence.*;
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

    public Notification(){

    }

    public Notification(ScheduleNotificationDto scheduleNotificationDto){
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
}
