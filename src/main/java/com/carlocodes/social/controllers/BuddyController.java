package com.carlocodes.social.controllers;

import com.carlocodes.social.dtos.BuddyRequestDto;
import com.carlocodes.social.exceptions.SocialException;
import com.carlocodes.social.services.BuddyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buddy")
public class BuddyController {
    private final BuddyService buddyService;

    public BuddyController(BuddyService buddyService) {
        this.buddyService = buddyService;
    }

    @PostMapping("/sendBuddyRequest")
    public ResponseEntity<String> sendBuddyRequest(@RequestBody BuddyRequestDto buddyRequestDto) throws SocialException {
        buddyService.sendBuddyRequest(buddyRequestDto);
        return ResponseEntity.ok("Buddy request sent!");
    }

    @PutMapping("/acceptBuddyRequest")
    public ResponseEntity<String> acceptBuddyRequest(@RequestBody BuddyRequestDto buddyRequestDto) throws SocialException {
        buddyService.acceptBuddyRequest(buddyRequestDto);
        return ResponseEntity.ok("Buddy request accepted!");
    }

    @PutMapping("/declineBuddyRequest")
    public ResponseEntity<String> declineBuddyRequest(@RequestBody BuddyRequestDto buddyRequestDto) throws SocialException {
        buddyService.declineBuddyRequest(buddyRequestDto);
        return ResponseEntity.ok("Buddy request declined!");
    }

    @DeleteMapping("/remove-buddy")
    public ResponseEntity<String> removeBuddy(@RequestBody BuddyRequestDto buddyRequestDto) throws SocialException {
        buddyService.removeBuddy(buddyRequestDto);
        return ResponseEntity.ok("Buddy removed!");
    }
}
