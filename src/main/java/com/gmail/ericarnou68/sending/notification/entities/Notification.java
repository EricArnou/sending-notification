package com.gmail.ericarnou68.sending.notification.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb_notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String recipient;

    private String message;

    @Enumerated(EnumType.STRING)
    private Chanel chanel;

    @Enumerated(EnumType.STRING)
    private Status status;
}
