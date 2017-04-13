package com.cyanelix.railwatch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Configuration
public class TestApplicationConfiguration {
    @Bean
    public Clock clock() {
        return Clock.fixed(Instant.parse("2017-01-01T10:30:00Z"), ZoneId.systemDefault());
    }
}
