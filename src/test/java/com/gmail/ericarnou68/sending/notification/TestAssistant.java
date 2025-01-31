package com.gmail.ericarnou68.sending.notification;

import java.time.LocalDateTime;

public class TestAssistant {

    public static String EMAIL_RECIPIENT = "email123@email.com";
    public static String PHONE_RECIPIENT = "99999999999";
    public static String PUSH_RECIPIENT = "pskaj1263564xscwdxd";
    public static String EMPTY_RECIPIENT = "";
    public static String NULL_RECIPIENT = null;
    public static LocalDateTime PAST_DATE = LocalDateTime.now().minusDays(1);
    public static LocalDateTime FUTURE_DATE = LocalDateTime.now().plusDays(1);
    public static String MESSAGE = "This is demo message";
    public static String EMPTY_MESSAGE = "";
    public static String NULL_MESSAGE = null;
    public static String INVALID_CHANEL = "FAX";

}
