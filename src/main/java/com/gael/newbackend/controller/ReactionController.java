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

    @PostMapping
    public ResponseEntity<?> reactToPost(@PathVariable Long postId, @RequestBody Map<String, String> body) {
        String username = body.get("username");
        String reactionType = body.get("reaction");

        Optional<Post> optionalPost = postRepository.findById(postId);
        Optional<User> optionalUser = userRepository.findByEmail(username); // correo usado como username

        if (optionalPost.isEmpty() || optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Post o usuario no encontrado");
        }

        Reaction reaction = new Reaction();
        reaction.setPost(optionalPost.get());
        reaction.setUser(optionalUser.get());
        reaction.setType(EReaction.valueOf(reactionType));

        reactionRepository.save(reaction);
        return ResponseEntity.ok().body("Reacción registrada");
    }

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
