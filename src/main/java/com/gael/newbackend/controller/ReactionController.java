package com.gael.newbackend.controller;


import com.gael.newbackend.model.*;
import com.gael.newbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/reactions")
@CrossOrigin(origins = "*")
public class ReactionController {

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;


@PostMapping("/")
public ResponseEntity<?> reactToPost(@RequestBody Map<String, String> data) {
    try {
        System.out.println(">>> Datos recibidos:");
        System.out.println("userId: " + data.get("userId"));
        System.out.println("reaction: " + data.get("reaction"));
        System.out.println("postId: " + data.get("postId"));

        Long userId = Long.parseLong(data.get("userId"));
        String reactionType = data.get("reaction");
        Long postId = Long.parseLong(data.get("postId"));

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post no encontrado"));

        Reaction existingReaction = reactionRepository.findByUserAndPost(user, post);
        if (existingReaction != null && existingReaction.getType().toString().equals(reactionType)) {
            reactionRepository.delete(existingReaction);
            System.out.println(">>> Reacción existente eliminada");
            return ResponseEntity.ok().body("Reacción eliminada.");
        }

        if (existingReaction != null) {
            reactionRepository.delete(existingReaction);
            System.out.println(">>> Reacción anterior eliminada");
        }

        Reaction newReaction = new Reaction();
        newReaction.setPost(post);
        newReaction.setUser(user);

        try {
            newReaction.setType(EReaction.valueOf(reactionType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Tipo de reacción inválido: " + reactionType);
        }

        reactionRepository.save(newReaction);
        System.out.println(">>> Reacción guardada con éxito");
        return ResponseEntity.ok().body("Reacción registrada.");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
    }
}


@GetMapping("/user")
public ResponseEntity<Map<String, String>> getUserReaction(
        @RequestParam Long userId,
        @RequestParam Long postId) {

    User user = userRepository.findById(userId).orElse(null);
    Post post = postRepository.findById(postId).orElse(null);

    if (user == null || post == null) {
        return ResponseEntity.ok(Map.of("reaction", null));
    }

    Reaction reaction = reactionRepository.findByUserAndPost(user, post);
    if (reaction != null) {
        return ResponseEntity.ok(Map.of("reaction", reaction.getType().toString()));
    } else {
        return ResponseEntity.ok(Map.of("reaction", null));
    }
}


}
