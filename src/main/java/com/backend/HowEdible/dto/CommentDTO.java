package com.backend.HowEdible.dto;


import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long videoId;
    private Long userId;
    private String username;

    public CommentDTO(Long id, String content, LocalDateTime createdAt, Long videoId, Long userId, String username) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.videoId = videoId;
        this.userId = userId;
        this.username = username;
    }

    // getters
    public Long getId() { return id; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getVideoId() { return videoId; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
}
