package com.cyanelix.railwatch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Europe/London"));
    }
}
