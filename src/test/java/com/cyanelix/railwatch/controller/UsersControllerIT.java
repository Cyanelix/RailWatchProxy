package com.cyanelix.railwatch.controller;

import com.cyanelix.railwatch.domain.User;
import com.cyanelix.railwatch.entity.UserEntity;
import com.cyanelix.railwatch.repository.UserRepository;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("it")
public class UsersControllerIT {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void createUserWithNoNotificationTarget_returns400() throws Exception {
        mockMvc.perform(post("/users"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createValidUser_returnsLocationWithUUID_userIsPersisted() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                post("/users")
                        .content("{ \"notificationTarget\": \"foo\" }")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String locationHeader = mvcResult.getResponse().getHeader("Location");
        assertThat(locationHeader, is(notNullValue()));

        String userId = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);

        List<UserEntity> users = userRepository.findAll();
        assertThat(users, hasSize(1));
        assertThat(users.get(0).getUserId(), is(userId));
    }
}
