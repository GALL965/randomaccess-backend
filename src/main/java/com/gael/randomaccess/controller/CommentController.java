package com.gael.randomaccess.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gael.randomaccess.model.Comment;
import com.gael.randomaccess.repository.CommentRepository;
import com.gael.randomaccess.repository.PostRepository;
import com.gael.randomaccess.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @PostConstruct
public void init() {
    System.out.println(">> CommentController CARGADO");
}


    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserRepository userRepo;

@PostMapping
public Comment addComment(@RequestBody CommentRequest request) {
    System.out.println("Intentando guardar comentario: " + request.getContent());

    var user = userRepo.findById(request.getUserId()).orElse(null);
    var post = postRepo.findById(request.getPostId()).orElse(null);

    if (user == null || post == null) {
        System.out.println("User o Post no encontrado");
        return null;
    }

    Comment comment = new Comment();
    comment.setContent(request.getContent());
    comment.setUser(user);
    comment.setPost(post);

    System.out.println("Guardando en base de datos: " + comment.getContent());

    return commentRepo.save(comment);
}

@GetMapping("/post/{postId}")
public List<Comment> getCommentsByPost(@PathVariable Long postId) {
    return commentRepo.findAll()
        .stream()
        .filter(c -> c.getPost() != null && c.getPost().getId().equals(postId))
        .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())) // más recientes primero
        .toList();
}

@GetMapping("/post/{postId}/paged")
public Page<Comment> getCommentsPaged(
        @PathVariable Long postId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
) {
    return commentRepo.findByPostIdOrderByCreatedAtDesc(postId, PageRequest.of(page, size));
}



@DeleteMapping("/{id}")
public void deleteComment(@PathVariable Long id) {
    System.out.println("Intentando eliminar comentario con ID: " + id);
    commentRepo.deleteById(id);
    System.out.println("Comentario eliminado: " + id);
}


@PutMapping("/{id}")
public Comment updateComment(@PathVariable Long id, @RequestBody CommentRequest request) {
    System.out.println("Editando comentario: " + id);
    var comment = commentRepo.findById(id).orElse(null);
    if (comment == null) {
        System.out.println("Comentario no encontrado");
        return null;
    }

    comment.setContent(request.getContent());
    return commentRepo.save(comment);
}



}
