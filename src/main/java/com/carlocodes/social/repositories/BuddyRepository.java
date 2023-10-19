package com.carlocodes.social.repositories;

import com.carlocodes.social.entities.Buddy;
import com.carlocodes.social.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuddyRepository extends JpaRepository<Buddy, Long> {
    boolean existsBySenderAndReceiverAndAcceptedIsNull(User sender, User receiver);
    boolean existsBySenderAndReceiverAndAcceptedIsTrue(User sender, User receiver);
}
