package com.backend.features.feed.service;

import com.backend.features.authentication.model.AuthenticationUser;
import com.backend.features.authentication.repository.AuthenticationUserRepository;
import com.backend.features.feed.dto.PostDTO;
import com.backend.features.feed.modal.Comment;
import com.backend.features.feed.modal.Post;
import com.backend.features.feed.repository.CommentRepository;
import com.backend.features.feed.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final PostRepository postRepository;
    private final AuthenticationUserRepository userRepository;
    private final CommentRepository commentRepository;

    public Post createPost(PostDTO postDTO, Long authorId) {

        AuthenticationUser user=userRepository.findById(authorId)
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        Post post=new Post(postDTO.getContent(),user);
        post.setPicture(postDTO.getPicture());
        return postRepository.save(post);

    }

    public Post editPost(Long postId, Long authorId, PostDTO postDTO) {
        Post post=postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("Post not found"));
        AuthenticationUser user=userRepository.findById(authorId)
                .orElseThrow(()->new IllegalArgumentException("User not found"));

        if (!post.getAuthor().equals(user)){
            throw new IllegalArgumentException("User is not the author of the post");
        }

        post.setContent(postDTO.getContent());
        post.setPicture(postDTO.getPicture());
        return postRepository.save(post);
    }

    public List<Post> getFeedPosts(Long authorId) {
        AuthenticationUser user=userRepository.findById(authorId)
                .orElseThrow(()->new IllegalArgumentException("User not found"));
     return postRepository.findByAuthorIdNotOrderByCreationDateDesc(authorId);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreationDateDesc();
    }

    public void deletePost(Long postId, Long authorId) {
        Post post=postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("Post not found"));
        AuthenticationUser user=userRepository.findById(authorId)
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        if (!post.getAuthor().equals(user)){
            throw new IllegalArgumentException("User is not the author of the post");
        }
        postRepository.delete(post);

    }

    public List<Post> getPostsByUserId(Long authorId) {
        AuthenticationUser user=userRepository.findById(authorId)
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        return postRepository.findByAuthorId(authorId);
    }

    public Post likePost(Long postId, Long authorId) {
        Post post=postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("Post not found"));
        AuthenticationUser user=userRepository.findById(authorId)
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        if (post.getLikes().contains(user)){
            post.getLikes().remove(user);
        }else {
            post.getLikes().add(user);
        }
        return postRepository.save(post);
    }

    public Comment addComment(Long postId, Long userId, String content) {
        Post post=postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("Post  not found"));
        AuthenticationUser user=userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        Comment comment=new Comment(post,user,content);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(()->new IllegalArgumentException("Comment not found"));
        AuthenticationUser user=userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        if (!comment.getAuthor().equals(user)){
            throw new IllegalArgumentException("User is not the author of the comment");
        }
        commentRepository.delete(comment);

    }

    public Comment editComment(Long commentId, Long userId, String newContent) {
        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(()->new IllegalArgumentException("Comment not found"));
        AuthenticationUser user=userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        if (!comment.getAuthor().equals(user)){
            throw new IllegalArgumentException("User is not the author of the comment");
        }
        comment.setContent(newContent);
        return commentRepository.save(comment);

    }
}
