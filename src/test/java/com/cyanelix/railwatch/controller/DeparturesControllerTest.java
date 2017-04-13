package com.cyanelix.railwatch.controller;

import com.cyanelix.railwatch.domain.Schedule;
import com.cyanelix.railwatch.domain.Station;
import com.cyanelix.railwatch.domain.TrainTime;
import com.cyanelix.railwatch.service.ScheduleService;
import com.cyanelix.railwatch.service.TrainTimesService;
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
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeparturesController.class)
@RunWith(SpringRunner.class)
public class DeparturesControllerTest {
    @MockBean
    private TrainTimesService mockTrainTimesService;

    @MockBean
    private ScheduleService mockScheduleService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void oneOnTimeServiceReturned_success() throws Exception {
        List<TrainTime> singleTime = Collections
                .singletonList(TrainTime.of(LocalTime.of(10, 0), Optional.of(LocalTime.of(10, 0)), ""));
        given(mockTrainTimesService.lookupTrainTimes(Station.of("AAA"), Station.of("BBB"))).willReturn(singleTime);

        mockMvc.perform(get("/departures?from=AAA&to=BBB"))
            .andExpect(status().isOk())
            .andExpect(content().json("[{'scheduledDepartureTime':'10:00', 'expectedDepartureTime':'10:00', 'message':''}]"));
    }

    @Test
    public void noServicesReturned_successWithEmptyArray() throws Exception {
        List<TrainTime> noTimes = Collections.emptyList();
        given(mockTrainTimesService.lookupTrainTimes(Station.of("AAA"), Station.of("BBB"))).willReturn(noTimes);

        mockMvc.perform(get("/departures?from=AAA&to=BBB"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void putNewTimesRequest_success() throws Exception {
        mockMvc.perform(
                put("/departures/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"startTime\":\"07:00\", \"endTime\":\"09:00\"}"))
                .andExpect(status().isCreated());

        ArgumentCaptor<Schedule> scheduleArgumentCaptor = ArgumentCaptor.forClass(Schedule.class);
        verify(mockScheduleService).createSchedule(scheduleArgumentCaptor.capture());

        Schedule schedule = scheduleArgumentCaptor.getValue();
        assertThat(schedule.getStartTime().toString(), is("07:00"));
        assertThat(schedule.getEndTime().toString(), is("09:00"));
    }
}
