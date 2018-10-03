package com.cyanelix.railwatch.entity;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.firebase.client.entity.NotificationRequest;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SentNotificationTest {
    @Test
    public void constructWithNotificationRequest_propertiesSetCorrectly() {
        // Given...
        NotificationRequest notificationRequest = new NotificationRequest(NotificationTarget.of("FOO"), "Title", "Body");

        // When...
        SentNotification entity = SentNotification.of(notificationRequest, LocalDateTime.of(2017, 1, 1, 10, 0));

        // Then...
        assertThat(entity.getTo(), is("FOO"));
        assertThat(entity.getTitle(), is("Title"));
        assertThat(entity.getBody(), is("Body"));
    }
}
