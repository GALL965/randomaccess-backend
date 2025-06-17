package com.gael.newbackend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.gael.newbackend.model.Reaction;
import com.gael.newbackend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByPost(Post post);

@Query("SELECT r.type AS reaction, COUNT(r) AS count FROM Reaction r WHERE r.post.id = :postId GROUP BY r.type")
Map<EReaction, Long> countReactionsByPostId(@Param("postId") Long postId);


}
