package com.cyanelix.railwatch.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

    @Test
    public void createSchedule_listAllSchedules_scheduleIsReturned() throws Exception {
        String uuid = UUID.randomUUID().toString();

        mockMvc.perform(
                put("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildRequest(uuid)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/schedules"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse(uuid)));
    }

    private String buildRequest(String uuid) {
        return "{\n" +
                "  \"startTime\": \"10:00\",\n" +
                "  \"endTime\": \"11:00\",\n" +
                "  \"days\": [\"Monday\"],\n" +
                "  \"fromStation\": \"PAD\",\n" +
                "  \"toStation\": \"BRI\",\n" +
                "  \"notificationTarget\": \"" + uuid + "\"\n" +
                "}";
    }

    private String expectedResponse(String uuid) {
        return "[{\n" +
                "  \"startTime\": \"10:00\",\n" +
                "  \"endTime\": \"11:00\",\n" +
                "  \"days\": [\"Monday\"],\n" +
                "  \"fromStation\": \"PAD\",\n" +
                "  \"toStation\": \"BRI\",\n" +
                "  \"notificationTarget\": \"" + uuid + "\"\n" +
                "}]";
    }
}
