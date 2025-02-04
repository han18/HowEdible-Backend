package com.backend.HowEdible.controller;

import com.backend.HowEdible.dto.CommentDTO;
import com.backend.HowEdible.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // this is creating a new comment this is the old one that accepts a paramater
//    @PostMapping("/add")
//    public ResponseEntity<CommentDTO> addComment(
//            @RequestParam Long videoId, 
//            @RequestParam Long userId, 
//            @RequestParam String content) {
//        
//        CommentDTO newComment = commentService.createComment(videoId, userId, content);
//        return ResponseEntity.ok(newComment);
//    }
    
    // this is the new response since it only accepts 
    @PostMapping("/add")
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO commentDTO) {
        Long videoId = commentDTO.getVideoId();
        Long userId = commentDTO.getUserId();
        String content = commentDTO.getContent();

        CommentDTO newComment = commentService.createComment(videoId, userId, content);
        return ResponseEntity.ok(newComment);
    }


    // getting all comments for a video
    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByVideo(@PathVariable Long videoId) {
        List<CommentDTO> comments = commentService.getCommentsByVideoId(videoId);
        return ResponseEntity.ok(comments);
    }

    // this is getting a specific comment by ID
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long commentId) {
        CommentDTO comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    // this is updating a comment
    @PutMapping("/update/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long commentId, 
            @RequestParam String newContent) {
        
        CommentDTO updatedComment = commentService.updateComment(commentId, newContent);
        return ResponseEntity.ok(updatedComment);
    }

    // this deletes the comment
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
