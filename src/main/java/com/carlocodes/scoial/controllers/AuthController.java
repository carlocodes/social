package com.carlocodes.scoial.controllers;

import com.carlocodes.scoial.dtos.AuthDto;
import com.carlocodes.scoial.exceptions.SocialException;
import com.carlocodes.scoial.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthDto authDto) throws SocialException {
        authService.register(authDto);
        return ResponseEntity.ok("Registered!");
    }

    @PostMapping("verify")
    public ResponseEntity<String> verify(@RequestBody AuthDto authDto) throws SocialException {
        String token = authService.verify(authDto);
        return ResponseEntity.ok(token);
    }
}
