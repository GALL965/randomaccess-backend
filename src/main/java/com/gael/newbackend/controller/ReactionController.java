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
    String username = data.get("username");
    String reactionType = data.get("reaction");

    User user = userRepository.findByEmail(username).orElseThrow();
    Post post = postRepository.findById(postId).orElseThrow();

    // Verificar si ya existe una reacción
    Reaction existingReaction = reactionRepository.findByUserAndPost(user, post);
    if (existingReaction != null && existingReaction.getType().toString().equals(reactionType)) {
        // Si la reacción ya existe y es la misma, eliminarla
        reactionRepository.delete(existingReaction);
        return ResponseEntity.ok().body("Reacción eliminada.");
    }

    // Si no existe, agregar una nueva
    if (existingReaction != null) {
        reactionRepository.delete(existingReaction); // Si tiene otra reacción, la elimina
    }

    Reaction newReaction = new Reaction();
    newReaction.setPost(post);
    newReaction.setUser(user);
    newReaction.setType(EReaction.valueOf(reactionType));

    reactionRepository.save(newReaction);
    return ResponseEntity.ok().body("Reacción registrada.");
}




}
