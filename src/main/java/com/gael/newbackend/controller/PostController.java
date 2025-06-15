package com.gael.newbackend.controller;

import java.util.Map;

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
public Post createPost(@RequestBody Map<String, String> data) {
    User user = userRepository.findByUsername(data.get("username")).orElseThrow();

    Post post = new Post();
    post.setTitle(data.get("title"));
    post.setDescription(data.get("description"));
    post.setImageUrl(data.get("imageUrl"));
    post.setUser(user);
    post.setCreatedAt(LocalDateTime.now());

    return postRepository.save(post);
}

    @GetMapping("/")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
