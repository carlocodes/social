package com.carlocodes.social.controllers;

import com.carlocodes.social.dtos.PostDto;
import com.carlocodes.social.exceptions.SocialException;
import com.carlocodes.social.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create-post")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) throws SocialException {
        return ResponseEntity.ok(postService.createPost(postDto));
    }

    @GetMapping("/get-post/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable long id) throws SocialException {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @GetMapping("/get-posts/user/{id}")
    public ResponseEntity<List<PostDto>> getPosts(@PathVariable long id) throws SocialException {
        return ResponseEntity.ok(postService.getPosts(id));
    }

    @PutMapping("/edit-post")
    public ResponseEntity<PostDto> editPost(@RequestBody PostDto postDto) throws SocialException {
        return ResponseEntity.ok(postService.editPost(postDto));
    }

    @GetMapping("/feed/{id}")
    public ResponseEntity<List<PostDto>> feed(@PathVariable long id) throws SocialException {
        return ResponseEntity.ok(postService.feed(id));
    }

    @DeleteMapping("/delete-post")
    public ResponseEntity<String> deletePost(@RequestBody PostDto postDto) throws SocialException {
        postService.deletePost(postDto);
        return ResponseEntity.ok("Post deleted!");
    }
}
