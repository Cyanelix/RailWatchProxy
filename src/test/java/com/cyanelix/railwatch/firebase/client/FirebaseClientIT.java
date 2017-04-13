package com.cyanelix.railwatch.firebase.client;

import com.cyanelix.railwatch.firebase.client.entity.NotificationRequest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FirebaseClientIT {
    @Autowired
    private FirebaseClient firebaseClient;

    @Test
    @Ignore("This is really just an exploratory test, and sends a real notification to a real token, which will cease to be valid in due course.")
    public void testSendNotification() {
        // Given...
        NotificationRequest request = new NotificationRequest("evfu1JfGw9c:APA91bGF6KmGoSyi7nkte9UYpMLL3Q2JJMWJVVwjJMzy7UbZP42WLPYAj__M0y3LRAZFlmavaGVSpMLfcQ8wIQJhD1_OdbfEI7zBCZBSw8GFaJ5EB4CPipyTVMFZt92ZcsKGGp2VYr3m",
                "Title", "Body");

        // When...
        boolean success = firebaseClient.sendNotification(request);

        // Then...
        assertThat(success, is(true));
    }
}
