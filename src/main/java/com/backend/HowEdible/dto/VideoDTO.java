package com.backend.HowEdible.dto;

import java.sql.Timestamp;

import com.backend.HowEdible.model.Video;

public class VideoDTO {
    private Long id;
    private String fileName;
    private String url;
    private String resolution;
    private String aspectRatio;
    private Timestamp uploadDate;
    private String title;
    private String username; // including username as a foreign ID
    private Long userId;

    
    public VideoDTO(Video video) {
        this.id = video.getId();
        this.fileName = video.getFileName();
        this.url = "http://localhost:8080/api/videos/stream/" + video.getId();
        this.resolution = video.getResolution();
        this.aspectRatio = video.getAspectRatio();
        this.uploadDate = video.getUploadDate();
        this.title = video.getTitle();
        this.username = video.getUser().getUsername(); // getting the username
        this.userId = video.getUser() != null ? video.getUser().getId() : null; // Ensures no null error

       

    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public String getUrl() { return url; }
    public String getResolution() { return resolution; }
    public String getAspectRatio() { return aspectRatio; }
    public Timestamp getUploadDate() { return uploadDate; }
    public String getTitle() { return title; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
}
