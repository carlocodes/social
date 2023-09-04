package com.carlocodes.scoial.services;

import com.carlocodes.scoial.dtos.PostDto;
import com.carlocodes.scoial.entities.Post;
import com.carlocodes.scoial.entities.User;
import com.carlocodes.scoial.exceptions.SocialException;
import com.carlocodes.scoial.repositories.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    public PostService(PostRepository postRepository,
                       UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public PostDto create(PostDto postDto) throws SocialException {
        try {
            Long userId = postDto.getUserId();

            User user = userService.findById(userId)
                    .orElseThrow(() -> new SocialException(String.format("User with id: %s does not exist!", userId)));

            return mapToDto(save(postDto, user));
        } catch (SocialException e) {
            throw new SocialException(String.format("Create post failed for user with id: %s due to %s", postDto.getUserId(), e.getMessage()), e);
        }
    }

    private Post save(PostDto postDto, User user) {
        Post post = new Post();
        post.setImage(postDto.getImage());
        post.setMessage(postDto.getMessage());
        post.setUser(user);
        return postRepository.save(post);
    }

    private PostDto mapToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setImage(post.getImage());
        postDto.setMessage(post.getMessage());
        postDto.setUserId(post.getUser().getId());
        return postDto;
    }
}
