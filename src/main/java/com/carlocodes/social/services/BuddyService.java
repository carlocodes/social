package com.carlocodes.social.services;

import com.carlocodes.social.dtos.BuddyRequestDto;
import com.carlocodes.social.entities.Buddy;
import com.carlocodes.social.entities.User;
import com.carlocodes.social.exceptions.SocialException;
import com.carlocodes.social.repositories.BuddyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BuddyService {
    private final BuddyRepository buddyRepository;
    private final UserService userService;

    public BuddyService(BuddyRepository buddyRepository,
                        UserService userService) {
        this.buddyRepository = buddyRepository;
        this.userService = userService;
    }

    public void sendBuddyRequest(BuddyRequestDto buddyRequestDto) throws SocialException {
        try {
            long senderId = buddyRequestDto.getSenderId();
            long receiverId = buddyRequestDto.getReceiverId();

            User sender = userService.findById(senderId).orElseThrow(() ->
                    new SocialException(String.format("Sender id: %d does not exist!", senderId)));
            User receiver = userService.findById(receiverId).orElseThrow(() ->
                    new SocialException(String.format("Receiver id: %d does not exist!", receiverId)));

            if (buddyRepository.existsBySenderAndReceiverAndAcceptedIsTrue(sender, receiver) ||
                    buddyRepository.existsBySenderAndReceiverAndAcceptedIsTrue(receiver, sender))
                throw new SocialException("Already buddies!");

            if (buddyRepository.existsBySenderAndReceiverAndAcceptedIsNull(sender, receiver) ||
                    buddyRepository.existsBySenderAndReceiverAndAcceptedIsNull(receiver, sender))
                throw new SocialException("Buddy request has already been sent!");

            createBuddyRequest(sender, receiver);
        } catch (SocialException e) {
            throw new SocialException(String.format("Send buddy request from sender id: %d to receiver id: %d failed due to: %s",
                    buddyRequestDto.getSenderId(),
                    buddyRequestDto.getReceiverId(),
                    e.getMessage()), e);
        }
    }

    public void acceptBuddyRequest(BuddyRequestDto buddyRequestDto) throws SocialException {
        try {
            long senderId = buddyRequestDto.getSenderId();
            long receiverId = buddyRequestDto.getReceiverId();

            User sender = userService.findById(senderId).orElseThrow(() ->
                    new SocialException(String.format("Sender id: %d does not exist!", senderId)));
            User receiver = userService.findById(receiverId).orElseThrow(() ->
                    new SocialException(String.format("Receiver id: %d does not exist!", receiverId)));

            if (buddyRepository.existsBySenderAndReceiverAndAcceptedIsTrue(sender, receiver) ||
                    buddyRepository.existsBySenderAndReceiverAndAcceptedIsTrue(receiver, sender))
                throw new SocialException("Already buddies!");

            Buddy buddy = buddyRepository.findBySenderAndReceiverAndAcceptedIsNull(sender, receiver).orElseThrow(() ->
                    new SocialException("Buddy request does not exist!"));
            buddy.setAccepted(true);
            save(buddy);
        } catch (SocialException e) {
            throw new SocialException(String.format("Accept buddy request from sender id: %d to receiver id: %d failed due to %s",
                    buddyRequestDto.getSenderId(),
                    buddyRequestDto.getReceiverId(),
                    e.getMessage()), e);
        }
    }

    private Buddy createBuddyRequest(User sender, User receiver) {
        Buddy buddy = new Buddy();

        buddy.setSender(sender);
        buddy.setReceiver(receiver);
        buddy.setCreatedDateTime(LocalDateTime.now());

        return save(buddy);
    }

    private Buddy save(Buddy buddy) {
        return buddyRepository.save(buddy);
    }
}
