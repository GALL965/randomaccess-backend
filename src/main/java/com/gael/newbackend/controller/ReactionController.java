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
    Long userId = Long.parseLong(data.get("userId"));
    String reactionType = data.get("reaction");
    Long postId = Long.parseLong(data.get("postId"));  // Ahora obtenemos postId del cuerpo de la solicitud

   User user = userRepository.findById(userId).orElseThrow();

    Post post = postRepository.findById(postId).orElseThrow();  // Ahora postId está definido correctamente

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
   try {
    newReaction.setType(EReaction.valueOf(reactionType));
   } catch (IllegalArgumentException e) {
    return ResponseEntity.badRequest().body("Tipo de reacción inválido: " + reactionType);
   }

     
    reactionRepository.save(newReaction);
    return ResponseEntity.ok().body("Reacción registrada.");
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
