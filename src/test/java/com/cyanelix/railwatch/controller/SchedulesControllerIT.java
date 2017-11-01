package com.cyanelix.railwatch.controller;

import com.cyanelix.railwatch.domain.ScheduleState;
import com.cyanelix.railwatch.repository.ScheduleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("it")
public class SchedulesControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Before
    public void setup() {
        scheduleRepository.deleteAll();
    }

    @Test
    public void createScheduleWithoutState_listAllSchedules_scheduleIsReturnedAsEnabled() throws Exception {
        // TODO: From v3.x null states no longer needs supporting as state will be required, so this test can be removed.
        String uuid = UUID.randomUUID().toString();

        mockMvc.perform(
                put("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildRequest(uuid, null)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/schedules"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse(uuid, ScheduleState.ENABLED)));
    }

    @Test
    public void createEnabledSchedule_listAllSchedules_scheduleIsReturnedAsEnabled() throws Exception {
        String uuid = UUID.randomUUID().toString();

        mockMvc.perform(
                put("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildRequest(uuid, ScheduleState.ENABLED)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/schedules"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse(uuid, ScheduleState.ENABLED)));
    }

    @Test
    public void createDisabledSchedule_listAllSchedules_scheduleIsReturnedAsDisabled() throws Exception {
        String uuid = UUID.randomUUID().toString();

        mockMvc.perform(
                put("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildRequest(uuid, ScheduleState.DISABLED)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/schedules"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse(uuid, ScheduleState.DISABLED)));
    }

    private String buildRequest(String uuid, ScheduleState scheduleState) {
        String state = "";

        if (scheduleState != null) {
            state = "  \"state\": \"" + scheduleState.name() + "\",\n";
        }

        return "{\n" +
                "  \"startTime\": \"10:00\",\n" +
                "  \"endTime\": \"11:00\",\n" +
                "  \"days\": [\"Monday\"],\n" +
                "  \"fromStation\": \"PAD\",\n" +
                "  \"toStation\": \"BRI\",\n" +
                state +
                "  \"notificationTarget\": \"" + uuid + "\"\n" +
                "}";
    }

    private String expectedResponse(String uuid, ScheduleState scheduleState) {
        return "[{\n" +
                "  \"startTime\": \"10:00\",\n" +
                "  \"endTime\": \"11:00\",\n" +
                "  \"days\": [\"Monday\"],\n" +
                "  \"fromStation\": \"PAD\",\n" +
                "  \"toStation\": \"BRI\",\n" +
                "  \"notificationTarget\": \"" + uuid + "\",\n" +
                "  \"state\": \"" + scheduleState.name() + "\"\n" +
                "}]";
    }

}
