package com.backend.HowEdible.controller;

import com.backend.HowEdible.dto.VideoDTO;
import com.backend.HowEdible.model.Video;
import com.backend.HowEdible.service.VideoService;

import org.springframework.beans.factory.annotation.Autowired;
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
            return ResponseEntity.ok(new VideoDTO(uploadedVideo)); // ✅ Return VideoDTO instead of Video
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VideoDTO>> getVideosByUser(@PathVariable Long userId) {
        try {
            List<VideoDTO> videos = videoService.getAllVideos();
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
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
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
    public ResponseEntity<List<VideoDTO>> getPaginatedVideos(
        @RequestParam(required = false) String cursor,
        @RequestParam(defaultValue = "10") int limit) {
    	
    	System.out.println("Received cursor: " + cursor);
        System.out.println("Fetching videos with limit: " + limit);
        
        Timestamp parsedCursor = null;
        if (cursor != null && !cursor.isEmpty()) {
            try {
                parsedCursor = Timestamp.valueOf(cursor);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null); // Invalid timestamp format
            }
        }
        return ResponseEntity.ok(videoService.getPaginatedVideos(parsedCursor, limit));
    }


    @GetMapping("/videos")
    public ResponseEntity<List<VideoDTO>> getAllVideos() {
        return ResponseEntity.ok(videoService.getAllVideos());
    }
}
