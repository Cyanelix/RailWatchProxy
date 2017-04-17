package com.cyanelix.railwatch.firebase.client;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.firebase.client.entity.NotificationRequest;
import com.cyanelix.railwatch.firebase.client.entity.NotificationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(FirebaseClient.class)
public class FirebaseClientTest {
    @Autowired
    private FirebaseClient firebaseClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${firebase.url}")
    private String firebaseUrl;

    @Before
    public void setup() {
        firebaseClient.sentRequests.clear();
    }

    @Test
    public void sendValidNotification_getSuccessResponse() throws JsonProcessingException {
        // Given...
        NotificationRequest request = new NotificationRequest(NotificationTarget.of("key"), "title", "body");

        mockServer.expect(requestTo(firebaseUrl))
                .andExpect(jsonPath("$.to", is("key")))
                .andExpect(jsonPath("$.notification.title", is("title")))
                .andExpect(jsonPath("$.notification.body", is("body")))
                .andRespond(withSuccess(objectMapper.writeValueAsString(successResponse()), MediaType.APPLICATION_JSON));

        // When...
        boolean success = firebaseClient.sendNotification(request);

        // Then...
        assertThat(success, is(true));
        mockServer.verify();
    }

    @Test
    public void doNotSendDuplicateNotification() throws JsonProcessingException {
        // Given...
        NotificationRequest request1 = new NotificationRequest(NotificationTarget.of("key"), "title", "body");
        NotificationRequest request2 = new NotificationRequest(NotificationTarget.of("key"), "title", "body");

        mockServer.expect(ExpectedCount.once(), requestTo(firebaseUrl))
                .andRespond(withSuccess(objectMapper.writeValueAsString(successResponse()), MediaType.APPLICATION_JSON));

        // When...
        firebaseClient.sendNotification(request1);
        firebaseClient.sendNotification(request2);

        // Then...
        mockServer.verify();
    }

    @Test
    public void sendTwoDifferentNotifications() throws JsonProcessingException {
        // Given...
        NotificationRequest request1 = new NotificationRequest(NotificationTarget.of("key"), "title", "body1");
        NotificationRequest request2 = new NotificationRequest(NotificationTarget.of("key"), "title", "body2");

        mockServer.expect(ExpectedCount.times(2), requestTo(firebaseUrl))
                .andRespond(withSuccess(objectMapper.writeValueAsString(successResponse()), MediaType.APPLICATION_JSON));

        // When...
        firebaseClient.sendNotification(request1);
        firebaseClient.sendNotification(request2);

        // Then...
        mockServer.verify();
    }

    private NotificationResponse successResponse() {
        NotificationResponse response = new NotificationResponse();
        response.setSuccess(1);
        return response;
    }
}
