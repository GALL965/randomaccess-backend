package com.gael.newbackend.repository;

import com.gael.newbackend.model.Reaction;
import com.gael.newbackend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByPost(Post post);
}
