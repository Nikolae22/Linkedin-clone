package com.backend.features.feed.modal;

import com.backend.features.authentication.model.AuthenticationUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    @JsonIgnore
    private Post post;

    @ManyToOne
    @JoinColumn(name = "author_id",nullable = false)
    private AuthenticationUser author;

    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    public Comment(Post post, AuthenticationUser user, String content) {
        this.post=post;
        this.author=user;
        this.content=content;
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedDate= LocalDateTime.now();
    }
}
