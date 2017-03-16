package com.cyanelix.railwatch.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.cyanelix.railwatch.client.service.TrainTimesService;
import com.cyanelix.railwatch.domain.Station;
import com.cyanelix.railwatch.domain.TrainTime;

@WebMvcTest(DeparturesController.class)
@RunWith(SpringRunner.class)
public class DeparturesControllerTest {
    @MockBean
    private TrainTimesService mockTrainTimesService;

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
}
