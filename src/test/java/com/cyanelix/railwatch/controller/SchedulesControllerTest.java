package com.cyanelix.railwatch.controller;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.Schedule;
import com.cyanelix.railwatch.domain.Station;
import com.cyanelix.railwatch.service.ScheduleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SchedulesController.class)
@RunWith(SpringRunner.class)
public class SchedulesControllerTest {
    @MockBean
    private ScheduleService mockScheduleService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void putNewTimesRequest_success() throws Exception {
        mockMvc.perform(
                put("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"startTime\":\"07:00\", \"endTime\":\"09:00\"}"))
                .andExpect(status().isCreated());

        ArgumentCaptor<Schedule> scheduleArgumentCaptor = ArgumentCaptor.forClass(Schedule.class);
        verify(mockScheduleService).createSchedule(scheduleArgumentCaptor.capture());

        Schedule schedule = scheduleArgumentCaptor.getValue();
        assertThat(schedule.getStartTime().toString(), is("07:00"));
        assertThat(schedule.getEndTime().toString(), is("09:00"));
    }

    @Test
    public void getAllSchedules_success() throws Exception {
        given(mockScheduleService.getSchedules())
                .willReturn(Collections.singleton(Schedule.of(
                        LocalTime.of(7, 0),
                        LocalTime.of(8, 0),
                        Station.of("FOO"),
                        Station.of("BAR"),
                        NotificationTarget.of("notification-to"))));

        mockMvc.perform(get("/schedules"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"startTime\":\"07:00\", \"endTime\":\"08:00\", \"fromStation\":\"FOO\", \"toStation\": \"BAR\", \"notificationTarget\":\"notification-to\"}]"));
    }
}
