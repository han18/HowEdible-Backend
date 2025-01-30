package com.backend.HowEdible.controller;

import com.backend.HowEdible.dto.VideoDTO;
import com.backend.HowEdible.model.Video;
import com.backend.HowEdible.repository.VideoRepository;
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
import java.util.stream.Collectors;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;



@CrossOrigin(origins = "http://localhost:3000") // to allow front end requests
@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;
    private final VideoRepository videoRepository; // ✅ Declare here

    // adding both dependencies for better readability
    @Autowired
    public VideoController(VideoService videoService, VideoRepository videoRepository) {
        this.videoService = videoService;
        this.videoRepository = videoRepository;
    }
    

//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadVideo(@RequestParam Long userId, @RequestParam MultipartFile file) {
//        try {
//            Video uploadedVideo = videoService.uploadVideo(userId, file);
//            return ResponseEntity.ok(new VideoDTO(uploadedVideo)); // ✅ Return VideoDTO instead of Video
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file");
//        }
//    }
    
    // new auth code for uploading videos
    @PostMapping("/upload")
    public ResponseEntity<?> uploadVideo(
            @RequestParam("userId") Long userId,
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file) {
        
        System.out.println("Received Upload Request: userId=" + userId + ", title=" + title + ", file=" + file.getOriginalFilename());
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is required.");
        }

        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Title is required.");
        }

        if (userId == null) {
            return ResponseEntity.badRequest().body("User ID is required.");
        }

        try {
            Video savedVideo = videoService.uploadVideo(userId, title, file);
            return ResponseEntity.ok(savedVideo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading video: " + e.getMessage());
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
                return ResponseEntity.badRequest().body(null); // Invalid time stamp format
            }
        }
        return ResponseEntity.ok(videoService.getPaginatedVideos(parsedCursor, limit));
    }


    // this is the old mapping before adding the user filed
//    @GetMapping("/videos")
//    public ResponseEntity<List<VideoDTO>> getAllVideos() {
//        return ResponseEntity.ok(videoService.getAllVideos());
//    }
    
    // the 3 blocks of code are part of the user id filed that will be associated with the video 
//    @GetMapping("/videos")
//    public List<VideoDTO> getAllVideos() {
//        List<Video> videos = videoRepository.findAll();
//        return videos.stream().map(VideoDTO::new).collect(Collectors.toList());
//    }
    
    // this was added according to the video and its user
    @GetMapping("/videos")
    public ResponseEntity<List<VideoDTO>> getAllVideos() {
        List<VideoDTO> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }
    
//    this code is added for connect the user to the video
    
}

