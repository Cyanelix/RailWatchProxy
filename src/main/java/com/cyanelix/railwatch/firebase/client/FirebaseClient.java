package com.cyanelix.railwatch.firebase.client;

import com.cyanelix.railwatch.firebase.client.entity.NotificationRequest;
import com.cyanelix.railwatch.firebase.client.entity.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.HashSet;
import java.util.Set;

@Component
public class FirebaseClient {
    private static final Logger LOG = LoggerFactory.getLogger(FirebaseClient.class);

    private final RestTemplate restTemplate;
    private final URI baseUrl;
    private final HttpHeaders httpHeaders;

    final Set<NotificationRequest> sentRequests = new HashSet<>();

    public FirebaseClient(RestTemplateBuilder restTemplateBuilder, @Value("${firebase.url}") String baseUrl, @Value("${firebase.authorization.key}") String authorizationKey) throws URISyntaxException {
        this.restTemplate = restTemplateBuilder.build();
        this.baseUrl = new URI(baseUrl);

        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, String.format("key=%s", authorizationKey));
    }

    public boolean sendNotification(NotificationRequest notificationRequest) {
        if (isDuplicateRequest(notificationRequest)) {
            LOG.debug("Not sending duplicate notification.");
            return false;
        }

        RequestEntity<NotificationRequest> requestEntity = new RequestEntity<>(notificationRequest, httpHeaders, HttpMethod.POST, baseUrl);

        ResponseEntity<NotificationResponse> notificationResponse = restTemplate.exchange(requestEntity, NotificationResponse.class);

        if (isSuccess(notificationResponse)) {
            sentRequests.add(notificationRequest);
            return true;
        }

        return false;
    }

    private boolean isDuplicateRequest(NotificationRequest notificationRequest) {
        return sentRequests.contains(notificationRequest);
    }

    private boolean isSuccess(ResponseEntity<NotificationResponse> notificationResponse) {
        return notificationResponse.getStatusCode().is2xxSuccessful()
                && notificationResponse.hasBody()
                && notificationResponse.getBody().getSuccess() == 1;
    }
}
