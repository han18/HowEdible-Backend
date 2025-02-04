package com.backend.HowEdible.service.Impl;


// this class implements the full CRUD application for comments 
import com.backend.HowEdible.dto.CommentDTO;
import com.backend.HowEdible.model.Comment;
import com.backend.HowEdible.model.User;
import com.backend.HowEdible.model.Video;
import com.backend.HowEdible.repository.CommentRepository;
import com.backend.HowEdible.repository.UserRepository;
import com.backend.HowEdible.repository.VideoRepository;
import com.backend.HowEdible.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CommentDTO createComment(Long videoId, Long userId, String content) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment(content, video, user);
        Comment savedComment = commentRepository.save(comment);

        return mapToDTO(savedComment);
    }

    @Override
    public CommentDTO getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return mapToDTO(comment);
    }

    @Override
    public List<CommentDTO> getCommentsByVideoId(Long videoId) {
        List<Comment> comments = commentRepository.findByVideoId(videoId);
        return comments.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public CommentDTO updateComment(Long commentId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setContent(newContent);
        Comment updatedComment = commentRepository.save(comment);

        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
    }

    // Utility method to map Comment to CommentDTO
    private CommentDTO mapToDTO(Comment comment) {
        return new CommentDTO(
            comment.getId(),
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getVideo().getId(),
            comment.getUser().getId(),
            comment.getUser().getUsername()
        );
    }
}
