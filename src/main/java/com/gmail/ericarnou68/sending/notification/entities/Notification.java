package com.gmail.ericarnou68.sending.notification.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    private String recipient;

    @Enumerated(EnumType.STRING)
    private Chanel chanel;

    @Enumerated(EnumType.STRING)
    private Status status;
}
