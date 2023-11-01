package com.carlocodes.social.services;

import com.carlocodes.social.dtos.PostDto;
import com.carlocodes.social.entities.Buddy;
import com.carlocodes.social.entities.Post;
import com.carlocodes.social.entities.User;
import com.carlocodes.social.exceptions.SocialException;
import com.carlocodes.social.mappers.PostMapper;
import com.carlocodes.social.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final BuddyService buddyService;

    public PostService(PostRepository postRepository,
                       UserService userService,
                       BuddyService buddyService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.buddyService = buddyService;
    }

    public PostDto createPost(PostDto postDto) throws SocialException {
        try {
            long userId = postDto.getUserId();

            User user = userService.findById(userId)
                    .orElseThrow(() -> new SocialException(String.format("User with id: %d does not exist!", userId)));

            return PostMapper.INSTANCE.mapToDto(save(postDto, user));
        } catch (SocialException e) {
            throw new SocialException(String.format("Create post failed for user with id: %s due to %s",
                    postDto.getUserId(), e.getMessage()), e);
        }
    }

    public PostDto getPost(long id) throws SocialException {
        try {
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new SocialException(String.format("Post with id: %d does not exist!", id)));

            return PostMapper.INSTANCE.mapToDto(post);
        } catch (SocialException e) {
            throw new SocialException(String.format("Get post with id: %d failed due to %s", id, e.getMessage()), e);
        }
    }

    public List<PostDto> getPosts(long id) throws SocialException {
        try {
            User user = userService.findById(id)
                    .orElseThrow(() -> new SocialException(String.format("User with id: %d does not exist!", id)));

            return PostMapper.INSTANCE.mapToDtos(postRepository.findByUser(user));
        } catch (SocialException e) {
            throw new SocialException(String.format("Get posts failed for user with id: %d due to %s", id, e.getMessage()), e);
        }
    }

    public PostDto editPost(PostDto postDto) throws SocialException {
        try {
            long id = postDto.getId();
            String message = postDto.getMessage();
            String image = postDto.getImage();
            long userId = postDto.getUserId();

            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new SocialException(String.format("Post with id: %d does not exist!", id)));

            if (!post.getId().equals(userId))
                throw new SocialException("You do not have permission to edit this post!");

            post.setMessage(message != null ? message : post.getMessage());
            post.setImage(image != null ? image : post.getImage());

            return PostMapper.INSTANCE.mapToDto(postRepository.save(post));
        } catch (SocialException e) {
            throw new SocialException(String.format("Edit post with id: %d failed for user with id: %d due to %s",
                    postDto.getId(), postDto.getUserId(), e.getMessage()), e);
        }
    }

    public List<PostDto> feed(long id) throws SocialException {
        try {
            User user = userService.findById(id).orElseThrow(() ->
                    new SocialException(String.format("User id: %d does not exist!", id)));

            List<Buddy> buddies = buddyService.findBySenderAndAcceptedIsTrueOrReceiverAndAcceptedIsTrue(user, user);

            Set<Long> buddyIds = buddies.stream()
                    .map(buddy -> buddyService.getBuddyId(buddy, user))
                    .collect(Collectors.toSet());

            buddyIds.add(user.getId());

            // TODO: Add additional conditions like:
            // Sorting by new/trending posts
            // Limiting # of posts per user
            // etc
            return PostMapper.INSTANCE.mapToDtos(postRepository.findByUserIdInOrderByCreatedDateTimeDesc(buddyIds));
        } catch (SocialException e) {
            throw new SocialException(String.format("Get feed for user with id: %d failed due to %s", id, e.getMessage()), e);
        }
    }

    public void deletePost(PostDto postDto) throws SocialException {
        try {
            long id = postDto.getId();
            long userId = postDto.getUserId();

            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new SocialException(String.format("Post with id: %d does not exist!", id)));

            if (!post.getUser().getId().equals(userId)) {
                throw new SocialException(String.format("User with id: %d is not allowed to delete post with id: %d!", userId, id));
            }

            postRepository.delete(post);
        } catch (SocialException e) {
            throw new SocialException(String.format("Delete post with id: %d for user with id: %d failed due to %s",
                    postDto.getId(), postDto.getUserId(), e.getMessage()), e);
        }
    }

    private Post save(PostDto postDto, User user) {
        Post post = new Post();
        post.setImage(postDto.getImage());
        post.setMessage(postDto.getMessage());
        post.setUser(user);
        return postRepository.save(post);
    }
}
