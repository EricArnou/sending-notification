package com.gmail.ericarnou68.sending.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SendingNotification {

	public static void main(String[] args) {
		SpringApplication.run(SendingNotification.class, args);
	}
}
