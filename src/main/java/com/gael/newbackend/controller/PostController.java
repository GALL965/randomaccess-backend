package com.gael.newbackend.controller;

import com.gael.newbackend.model.Post;
import com.gael.newbackend.model.User;
import com.gael.newbackend.repository.PostRepository;
import com.gael.newbackend.repository.UserRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/")
    public Post createPost(@RequestBody Post post, @RequestParam String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    @GetMapping("/")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
