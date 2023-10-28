package com.carlocodes.social.repositories;

import com.carlocodes.social.entities.Post;
import com.carlocodes.social.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
    List<Post> findByUserIdInOrderByCreatedDateTimeDesc(Set<Long> ids);
}
