package com.cyanelix.railwatch.controller;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.ScheduleState;
import com.cyanelix.railwatch.domain.User;
import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.service.UsersService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Matchers.endsWith;
import static org.mockito.Matchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersController.class)
@RunWith(SpringRunner.class)
public class UsersControllerTest {
    @MockBean
    private UsersService usersService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createWithNoBody_returnsBadRequest() throws Exception {
        mockMvc.perform(
                post("/users"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createWithEmptyBody_returnsBadRequest() throws Exception {
        mockMvc.perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createWithEmptyNotificationTarget_returnsBadRequest() throws Exception {
        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"notificationTarget\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUser_returnsCreatedWithLocation() throws Exception {
        NotificationTarget notificationTarget = NotificationTarget.of("notification-target");
        String userUuid = UUID.randomUUID().toString();

        given(usersService.createUser(notificationTarget)).willReturn(new User(UserId.of(userUuid), notificationTarget, ScheduleState.ENABLED));

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"notificationTarget\":\"" + notificationTarget.getTargetAddress() + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/users/" + userUuid));
    }
}
