package com.cyanelix.railwatch.firebase.client;

import com.cyanelix.railwatch.firebase.client.entity.NotificationRequest;
import com.cyanelix.railwatch.firebase.client.entity.NotificationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

@Component
public class FirebaseClient {
    private final RestTemplate restTemplate;
    private final URI baseUrl;
    private final HttpHeaders httpHeaders;

    public FirebaseClient(RestTemplateBuilder restTemplateBuilder, @Value("${firebase.url}") String baseUrl, @Value("${firebase.authorization.key}") String authorizationKey) throws URISyntaxException {
        this.restTemplate = restTemplateBuilder.build();
        this.baseUrl = new URI(baseUrl);

        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, String.format("key=%s", authorizationKey));
    }

    public boolean sendNotification(NotificationRequest notificationRequest) {
        RequestEntity<NotificationRequest> requestEntity = new RequestEntity(notificationRequest, httpHeaders, HttpMethod.POST, baseUrl);

        ResponseEntity<NotificationResponse> notificationResponse = restTemplate.exchange(requestEntity, NotificationResponse.class);

        return notificationResponse.getStatusCode().is2xxSuccessful()
                && notificationResponse.getBody().getSuccess() == 1;
    }
}
