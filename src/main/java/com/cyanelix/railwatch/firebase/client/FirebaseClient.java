package com.cyanelix.railwatch.firebase.client;

import com.cyanelix.railwatch.firebase.client.entity.NotificationRequest;
import com.cyanelix.railwatch.firebase.client.entity.NotificationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

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
        RequestEntity<NotificationRequest> requestEntity = new RequestEntity<>(notificationRequest, httpHeaders, HttpMethod.POST, baseUrl);

        ResponseEntity<NotificationResponse> notificationResponse = restTemplate.exchange(requestEntity, NotificationResponse.class);

        return isSuccess(notificationResponse);
    }

    private boolean isSuccess(ResponseEntity<NotificationResponse> notificationResponse) {
        return notificationResponse.getStatusCode().is2xxSuccessful()
                && notificationResponse.getBody().getSuccess() == 1;
    }
}
