
package com.gael.newbackend.model;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "posts")
public class Post {

@JsonIgnoreProperties({"email", "password", "roles"})
@ManyToOne
@JoinColumn(name = "user_id")
private User user;
@OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
@JsonIgnoreProperties({"posts", "user"})
private java.util.List<Comment> comments = new java.util.ArrayList<>();

public java.util.List<Comment> getComments() {
    return comments;
}

public void setComments(java.util.List<Comment> comments) {
    this.comments = comments;
}


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private String imageUrl;

   private LocalDateTime createdAt;

    // Getters y setters
    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

@JsonManagedReference
    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Reaction> reactions = new ArrayList<>();

public List<Reaction> getReactions() {
    return reactions;
}
public void setReactions(List<Reaction> reactions) {
    this.reactions = reactions;
}


}
