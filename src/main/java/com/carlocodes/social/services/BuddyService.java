package com.carlocodes.social.services;

import com.carlocodes.social.dtos.BuddyDto;
import com.carlocodes.social.dtos.BuddyRequestDto;
import com.carlocodes.social.entities.Buddy;
import com.carlocodes.social.entities.User;
import com.carlocodes.social.exceptions.SocialException;
import com.carlocodes.social.mappers.BuddyMapper;
import com.carlocodes.social.repositories.BuddyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

            saveBuddyRequest(sender, receiver);
        } catch (SocialException e) {
            throw new SocialException(String.format("Send buddy request from sender id: %d to receiver id: %d failed due to: %s",
                    buddyRequestDto.getSenderId(), buddyRequestDto.getReceiverId(), e.getMessage()), e);
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
            buddyRepository.save(buddy);
        } catch (SocialException e) {
            throw new SocialException(String.format("Accept buddy request from sender id: %d to receiver id: %d failed due to %s",
                    buddyRequestDto.getSenderId(), buddyRequestDto.getReceiverId(), e.getMessage()), e);
        }
    }

    public void declineBuddyRequest(BuddyRequestDto buddyRequestDto) throws SocialException {
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
            buddy.setAccepted(false);
            buddyRepository.save(buddy);
        } catch (SocialException e) {
            throw new SocialException(String.format("Decline buddy request from sender id: %d to receiver id: %d failed due to %s",
                    buddyRequestDto.getSenderId(), buddyRequestDto.getReceiverId(), e.getMessage()), e);
        }
    }

    public void removeBuddy(BuddyRequestDto buddyRequestDto) throws SocialException {
        try {
            long senderId = buddyRequestDto.getSenderId();
            long receiverId = buddyRequestDto.getReceiverId();

            User sender = userService.findById(senderId).orElseThrow(() ->
                    new SocialException(String.format("Sender id: %d does not exist!", senderId)));
            User receiver = userService.findById(receiverId).orElseThrow(() ->
                    new SocialException(String.format("Receiver id: %d does not exist!", receiverId)));

            Optional<Buddy> senderToReceiverBuddy = buddyRepository.findBySenderAndReceiverAndAcceptedIsTrue(sender, receiver);
            Optional<Buddy> receiverToSenderBuddy = buddyRepository.findBySenderAndReceiverAndAcceptedIsTrue(receiver, sender);

            if (senderToReceiverBuddy.isPresent()) {
                buddyRepository.delete(senderToReceiverBuddy.get());
            } else if (receiverToSenderBuddy.isPresent()) {
                buddyRepository.delete(receiverToSenderBuddy.get());
            } else {
                throw new SocialException(String.format("Sender id: %d and receiver id: %d are not buddies!", senderId, receiverId));
            }
        } catch (SocialException e) {
            throw new SocialException(String.format("Remove buddy from sender id: %d to receiver id: %d failed due to %s",
                    buddyRequestDto.getSenderId(), buddyRequestDto.getReceiverId(), e.getMessage()), e);
        }
    }

    public List<BuddyDto> getPendingBuddyRequests(long id) throws SocialException {
        try {
            User user = userService.findById(id).orElseThrow(() ->
                    new SocialException(String.format("User id: %d does not exist!", id)));

            return BuddyMapper.INSTANCE.mapToDtos(buddyRepository.findByReceiverAndAcceptedIsNull(user));
        } catch (SocialException e) {
            throw new SocialException(String.format("Get pending buddy requests for user id: %d failed due to %s", id, e.getMessage()), e);
        }
    }

    private void saveBuddyRequest(User sender, User receiver) {
        Buddy buddy = new Buddy();

        buddy.setSender(sender);
        buddy.setReceiver(receiver);
        buddy.setCreatedDateTime(LocalDateTime.now());

        buddyRepository.save(buddy);
    }

    public Long getBuddyId(Buddy buddy, User user) {
        return buddy.getSender().equals(user) ? buddy.getReceiver().getId() : buddy.getSender().getId();
    }

    public List<Buddy> findBySenderAndAcceptedIsTrueOrReceiverAndAcceptedIsTrue(User sender, User receiver) {
        return buddyRepository.findBySenderAndAcceptedIsTrueOrReceiverAndAcceptedIsTrue(sender, receiver);
    }
}
