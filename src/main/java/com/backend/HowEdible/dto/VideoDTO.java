package com.backend.HowEdible.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.backend.HowEdible.model.Video;

public class VideoDTO {
    private Long id;
    private String fileName;
    private String url;  // ✅ Add URL field
    private String resolution;
    private String aspectRatio;
    private Timestamp uploadDate; // remember I changed it from loacalDateTime to Time stamp

    public VideoDTO(Video video) {
        this.id = video.getId();
        this.fileName = video.getFileName();
        this.url = "http://localhost:8080/api/videos/stream/" + video.getId();
        this.resolution = video.getResolution();
        this.aspectRatio = video.getAspectRatio();
        this.uploadDate = video.getUploadDate();
    }
}

