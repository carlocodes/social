package com.carlocodes.social.controllers;

import com.carlocodes.social.dtos.PostDto;
import com.carlocodes.social.exceptions.SocialException;
import com.carlocodes.social.services.PostService;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/create")
    public ResponseEntity<PostDto> create(@RequestBody PostDto postDto) throws SocialException {
        return ResponseEntity.ok(postService.create(postDto));
    }

    @GetMapping("/getPost/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long id) throws SocialException {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @GetMapping("/getPosts/{userId}")
    public ResponseEntity<List<PostDto>> getPosts(@PathVariable Long userId) throws SocialException {
        return ResponseEntity.ok(postService.getPosts(userId));
    }

    @PutMapping("/edit")
    public ResponseEntity<PostDto> edit(@RequestBody PostDto postDto) throws SocialException {
        return ResponseEntity.ok(postService.edit(postDto));
    }
}
