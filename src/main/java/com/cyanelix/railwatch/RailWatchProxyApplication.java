package com.cyanelix.railwatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RailWatchProxyApplication {
    private RailWatchProxyApplication() {
        // Just hide the default constructor; this class shouldn't be
        // instantiated.
    }

    public static void main(String[] args) {
        SpringApplication.run(RailWatchProxyApplication.class, args);
    }
}
