package com.carlocodes.social.repositories;

import com.carlocodes.social.entities.FollowRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRequestRepository extends JpaRepository<FollowRequest, Long> {
    List<FollowRequest> findByReceiverIdAndAcceptedIsTrue(Long receiverId);
}
