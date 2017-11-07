package com.cyanelix.railwatch.controller;

import com.cyanelix.railwatch.entity.HeartbeatEntity;
import com.cyanelix.railwatch.repository.HeartbeatRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("it")
public class HeartbeatControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HeartbeatRepository heartbeatRepository;

    @Before
    public void setup() {
        heartbeatRepository.deleteAll();
    }

    @Test
    public void createHeartbeat_queryDatabase_heartbeatIsStored() throws Exception {
        mockMvc.perform(put("/heartbeat/device-id"))
                .andExpect(status().isCreated());

        List<HeartbeatEntity> heartbeatEntities = heartbeatRepository.findAll();
        assertThat(heartbeatEntities.size(), is(1));
    }
}
