package com.gael.newbackend.repository;

import java.util.List;
import java.util.Map; // ← AÑADE ESTO
import com.gael.newbackend.model.EReaction; // ← AÑADE ESTO
import com.gael.newbackend.model.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.gael.newbackend.model.Reaction;
import com.gael.newbackend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByPost(Post post);

@Query("SELECT r.type, COUNT(r) FROM Reaction r WHERE r.post.id = :postId GROUP BY r.type")
List<Object[]> countReactionsByPostId(@Param("postId") Long postId);

void deleteByUserAndPost(User user, Post post);


}
