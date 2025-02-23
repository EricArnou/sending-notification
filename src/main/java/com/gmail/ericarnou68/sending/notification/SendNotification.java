package com.gmail.ericarnou68.sending.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SendNotification {

	public static void main(String[] args) {
		SpringApplication.run(SendNotification.class, args);
	}
}
