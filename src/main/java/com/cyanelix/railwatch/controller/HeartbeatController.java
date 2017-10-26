package com.cyanelix.railwatch.controller;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.service.HeartbeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("heartbeat")
public class HeartbeatController {
    private final HeartbeatService heartbeatService;

    @Autowired
    public HeartbeatController(HeartbeatService heartbeatService) {
        this.heartbeatService = heartbeatService;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "{deviceId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void put(@PathVariable("deviceId") String deviceId) {
        heartbeatService.recordHeartbeat(NotificationTarget.of(deviceId));
    }
}
