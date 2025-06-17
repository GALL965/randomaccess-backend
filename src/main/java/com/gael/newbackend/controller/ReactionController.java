package com.gael.newbackend.controller;

import com.gael.newbackend.model.*;
import com.gael.newbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/posts/{postId}/reactions")
@CrossOrigin(origins = "*")
public class ReactionController {

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping
    public ResponseEntity<List<Reaction>> getReactionsForPost(@PathVariable Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Reaction> reactions = reactionRepository.findByPost(optionalPost.get());
        return ResponseEntity.ok(reactions);
    }




}
