package com.gmail.ericarnou68.sending.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gmail.ericarnou68.sending.notification.entities.Channel;
import com.gmail.ericarnou68.sending.notification.entities.Status;
import com.gmail.ericarnou68.sending.notification.entities.dto.CreatedNotificationDto;
import com.gmail.ericarnou68.sending.notification.entities.dto.ScheduleNotificationDto;
import com.gmail.ericarnou68.sending.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.ericarnou68.sending.notification.TestAssistant.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void whenScheduleNotificationIsCorrectExpectSuccess() throws Exception {
        // given
        var scheduleNotificationDto = new ScheduleNotificationDto(EMAIL_RECIPIENT, MESSAGE, FUTURE_DATE, Channel.EMAIL.toString());
        var createdNotificationDto = new CreatedNotificationDto(null, EMAIL_RECIPIENT, MESSAGE, FUTURE_DATE, Channel.EMAIL, Status.PENDING ); // Simulação de retorno válido

        // Simulando o comportamento do service mockado
        when(notificationService.scheduleNotificationService(any(ScheduleNotificationDto.class)))
            .thenReturn(createdNotificationDto);

        // when & then
        mockMvc.perform(post("/api/v1/notifications")
                .content(objectMapper.writeValueAsString(scheduleNotificationDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recipient").value(EMAIL_RECIPIENT))
                .andExpect(jsonPath("$.message").value(MESSAGE))
                .andExpect(jsonPath("$.channel").value(Channel.EMAIL.toString()))
                .andExpect(jsonPath("$.status").value(Status.PENDING.toString()));
        
        verify(notificationService, times (1)).scheduleNotificationService(any(ScheduleNotificationDto.class));
    }

    @Test
    void whenRecipientIsBlankExpectBadRequest() throws Exception {
        // given
        var scheduleNotificationDto = new ScheduleNotificationDto("", MESSAGE, FUTURE_DATE, Channel.EMAIL.toString());

        // when & then
        mockMvc.perform(post("/api/v1/notifications")
                .content(objectMapper.writeValueAsString(scheduleNotificationDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        
        verify(notificationService, times (0)).scheduleNotificationService(any(ScheduleNotificationDto.class));
    }

    @Test
    void whenMessageIsNullExpectBadRequest() throws Exception {
        // given
        var scheduleNotificationDto = new ScheduleNotificationDto(EMAIL_RECIPIENT, null, FUTURE_DATE, Channel.EMAIL.toString());

        // when & then
        mockMvc.perform(post("/api/v1/notifications")
                .content(objectMapper.writeValueAsString(scheduleNotificationDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        
        verify(notificationService, times (0)).scheduleNotificationService(any(ScheduleNotificationDto.class));
    }

    @Test
    void whenSchedulingIsNullExpectBadRequest() throws Exception {
        // given
        var scheduleNotificationDto = new ScheduleNotificationDto(EMAIL_RECIPIENT, MESSAGE, null, Channel.EMAIL.toString());

        // when & then
        mockMvc.perform(post("/api/v1/notifications")
                .content(objectMapper.writeValueAsString(scheduleNotificationDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        
        verify(notificationService, times (0)).scheduleNotificationService(any(ScheduleNotificationDto.class));
    }

    @Test
    void whenSchedulingIsinPastDateExpectBadRequest() throws Exception {
        // given
        var scheduleNotificationDto = new ScheduleNotificationDto(EMAIL_RECIPIENT, MESSAGE, PAST_DATE, Channel.EMAIL.toString());

        // when & then
        mockMvc.perform(post("/api/v1/notifications")
                .content(objectMapper.writeValueAsString(scheduleNotificationDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        
        verify(notificationService, times (0)).scheduleNotificationService(any(ScheduleNotificationDto.class));
    }

    @Test
    void whenChannelIsNullExpectBadRequest() throws Exception {
        // given
        var scheduleNotificationDto = new ScheduleNotificationDto(EMAIL_RECIPIENT, MESSAGE, FUTURE_DATE, null);

        // when & then
        mockMvc.perform(post("/api/v1/notifications")
                .content(objectMapper.writeValueAsString(scheduleNotificationDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        
        verify(notificationService, times (0)).scheduleNotificationService(any(ScheduleNotificationDto.class));
    }
}
