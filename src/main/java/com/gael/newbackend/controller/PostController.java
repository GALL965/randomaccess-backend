package com.gael.newbackend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.gael.newbackend.model.EReaction;
import com.gael.newbackend.model.Post;
import com.gael.newbackend.model.Reaction;
import com.gael.newbackend.model.User;
import com.gael.newbackend.repository.PostRepository;
import com.gael.newbackend.repository.ReactionRepository;
import com.gael.newbackend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReactionRepository reactionRepository;

    @PostMapping("/")
    public Post createPost(@RequestBody Map<String, String> data) {
        User user = userRepository.findByEmail(data.get("username")).orElseThrow();

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

    @PostMapping("/{postId}/reactions")
    public void addOrUpdateReaction(@PathVariable Long postId, @RequestBody Map<String, String> data) {
        String username = data.get("username");
        String reactionType = data.get("reaction");

        User user = userRepository.findByEmail(username).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        reactionRepository.deleteByUserAndPost(user, post);

        Reaction reaction = new Reaction();
        reaction.setPost(post);
        reaction.setUser(user);
        reaction.setType(EReaction.valueOf(reactionType));
        reactionRepository.save(reaction);
    }

    @DeleteMapping("/{postId}/reactions/{username}")
    public void removeReaction(@PathVariable Long postId, @PathVariable String username) {
        User user = userRepository.findByEmail(username).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();
        reactionRepository.deleteByUserAndPost(user, post);
    }

@GetMapping("/{postId}/reactions/count")
public Map<String, Long> getReactionCounts(@PathVariable Long postId) {
    List<Object[]> result = reactionRepository.countReactionsByPostId(postId);
    Map<String, Long> counts = new java.util.HashMap<>();
    for (Object[] row : result) {
        counts.put(row[0].toString(), (Long) row[1]);
    }
    return counts;
}


}
