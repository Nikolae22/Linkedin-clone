package com.backend.features.feed.controller;

import com.backend.features.authentication.model.AuthenticationUser;
import com.backend.features.feed.dto.CommentDTO;
import com.backend.features.feed.dto.PostDTO;
import com.backend.features.feed.modal.Comment;
import com.backend.features.feed.modal.Post;
import com.backend.features.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping
    public ResponseEntity<List<Post>> getFeedPosts(
            @RequestAttribute("authenticationUser") AuthenticationUser user){
        List<Post> posts=feedService.getFeedPosts(user.getId());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts(){
        List<Post> posts=feedService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(
            @RequestBody PostDTO postDTO,
            @RequestAttribute("authentication")AuthenticationUser user){
        Post post=feedService.createPost(postDTO,user.getId());
        return ResponseEntity.ok(post);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<Post> editPost(@PathVariable Long postId,
                                         @RequestBody PostDTO postDTO,
                                         @RequestAttribute("authenticationUser")AuthenticationUser user){
        Post post=feedService.editPost(postId,user.getId(),postDTO);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @RequestAttribute("authenticationUser") AuthenticationUser user){
        feedService.deletePost(postId,user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/posts/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Long userId){
        List<Post> posts=feedService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/posts/{postId}/like")
    public ResponseEntity<Post> likePost(@PathVariable Long postId,
                                         @RequestAttribute("authentication")AuthenticationUser user){
        Post post=feedService.likePost(postId,user.getId());
        return ResponseEntity.ok(post);
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO,
                                              @RequestAttribute("authenticationUser")AuthenticationUser user){
        Comment comment=feedService.addComment(postId,user.getId(),commentDTO.getContent());
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @RequestAttribute("authenticationUser")AuthenticationUser user){
        feedService.deleteComment(commentId,user.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Comment> editComment(@PathVariable Long commentId, @RequestBody CommentDTO commentDTO,
                                               @RequestAttribute("authenticationUser")AuthenticationUser user){
        Comment comment=feedService.editComment(commentId,user.getId(),commentDTO.getContent());
        return ResponseEntity.ok(comment);
    }
}
