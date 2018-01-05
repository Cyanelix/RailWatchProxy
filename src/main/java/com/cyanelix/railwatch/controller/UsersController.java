package com.cyanelix.railwatch.controller;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.User;
import com.cyanelix.railwatch.dto.UserDTO;
import com.cyanelix.railwatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("users")
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> create(@RequestBody @Valid UserDTO userDTO, UriComponentsBuilder uriComponentsBuilder) {
        User user = userService.createUser(NotificationTarget.of(userDTO.getNotificationTarget()));

        UriComponents uriComponents = uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.getUserId().get());

        return ResponseEntity.created(uriComponents.toUri()).build();
    }
}
