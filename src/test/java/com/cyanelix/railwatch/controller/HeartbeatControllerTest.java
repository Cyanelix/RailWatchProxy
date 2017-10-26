package com.cyanelix.railwatch.controller;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.service.HeartbeatService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HeartbeatController.class)
@RunWith(SpringRunner.class)
public class HeartbeatControllerTest {
    @MockBean
    private HeartbeatService heartbeatService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void putHeartbeat_success() throws Exception {
        mockMvc.perform(
                put("/heartbeat/notification-id")
                        .header(HttpHeaders.CONTENT_LENGTH, "0"))
                .andExpect(status().isCreated());

        verify(heartbeatService).recordHeartbeat(NotificationTarget.of("notification-id"));
    }
}
