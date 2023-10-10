package com.carlocodes.social.services;

import com.carlocodes.social.entities.FollowRequest;
import com.carlocodes.social.repositories.FollowRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowRequestService {
    private final FollowRequestRepository followRequestRepository;

    public FollowRequestService(FollowRequestRepository followRequestRepository) {
        this.followRequestRepository = followRequestRepository;
    }

    protected List<FollowRequest> findByReceiverIdAndAcceptedIsTrue(Long receiverId) {
        return followRequestRepository.findByReceiverIdAndAcceptedIsTrue(receiverId);
    }
}
