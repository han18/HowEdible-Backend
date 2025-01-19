package com.backend.HowEdible.controller;

import com.backend.HowEdible.dto.VideoDTO;
import com.backend.HowEdible.model.Video;
import com.backend.HowEdible.service.VideoService;

import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.sql.Timestamp;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadVideo(@RequestParam Long userId, @RequestParam MultipartFile file) {
        try {
            Video uploadedVideo = videoService.uploadVideo(userId, file);
            return ResponseEntity.ok(uploadedVideo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Video>> getVideosByUser(@PathVariable Long userId) {
        try {
            List<Video> videos = videoService.getAllVideos(userId);
            return ResponseEntity.ok(videos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/stream/{videoId}")
    public ResponseEntity<StreamingResponseBody> streamVideo(@PathVariable Long videoId) {
        Video video = videoService.findById(videoId);
        if (video == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        InputStream videoStream = new ByteArrayInputStream(video.getContent());

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("video/mp4"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline") // ✅ Force video playback
                .body(outputStream -> {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = videoStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        outputStream.flush();
                    }
                });
    }




    @GetMapping("/feed")
    public List<Video> getPaginatedVideos(
        @RequestParam(required = false) Timestamp cursor,
        @RequestParam(defaultValue = "10") int limit) {
        return videoService.getPaginatedVideos(cursor, limit);
    }
    
//    @GetMapping
//    public ResponseEntity<List<Video>> getAllVideos() {
//        List<Video> videos = videoService.getAllVideos();
//
//        if (videos.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.ok(videos);
//    }
    
    @GetMapping("/videos")
    public List<Video> getAllVideos() {
        List<Video> videos = videoService.getAllVideos();
        
        // Generate proper URLs before returning videos
        videos.forEach(video -> {
            String generatedUrl = "http://localhost:8080/api/videos/stream/" + video.getId();
            video.setUrl(generatedUrl);
        });

        return videos;
    }
    @GetMapping
    public ResponseEntity<List<Video>> getAllVideos1() {
        return ResponseEntity.ok(videoService.getAllVideos());
    }

}
