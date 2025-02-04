package com.backend.HowEdible.service;

import com.backend.HowEdible.dto.CommentDTO;
import com.backend.HowEdible.model.Comment;
import java.util.List;

public interface CommentService {
    CommentDTO createComment(Long videoId, Long userId, String content);  
    CommentDTO getCommentById(Long commentId);
    List<CommentDTO> getCommentsByVideoId(Long videoId);
    CommentDTO updateComment(Long commentId, String newContent);
    void deleteComment(Long commentId);
}