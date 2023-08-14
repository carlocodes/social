package com.carlocodes.scoial.controllers;

import com.carlocodes.scoial.dtos.UserDto;
import com.carlocodes.scoial.exceptions.SocialException;
import com.carlocodes.scoial.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) throws SocialException {
        userService.register(userDto);
        return ResponseEntity.ok("Registered!");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody UserDto userDto) throws SocialException {
        userService.verify(userDto);
        return ResponseEntity.ok("Verified!");
    }
}