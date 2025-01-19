package com.backend.HowEdible.service;

import com.backend.HowEdible.model.Video;
import com.backend.HowEdible.dto.VideoDTO;
import com.backend.HowEdible.model.User;
import com.backend.HowEdible.repository.VideoRepository;
import com.backend.HowEdible.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserRepository userRepository;

    public Video uploadVideo(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (file.getSize() > 100 * 1024 * 1024) { 
            throw new IllegalArgumentException("File size exceeds the 100MB limit");
        }

        Video video = new Video();
        video.setUser(user);
        video.setFileName(file.getOriginalFilename());
        video.setContent(file.getBytes());
        video.setAspectRatio("16:9");
        video.setResolution("1920x1080");
        video.setUploadDate(new Timestamp(System.currentTimeMillis())); // Set upload timestamp

        // Save video first to get the ID
        video = videoRepository.save(video);

        // ✅ Correct URL format using /stream/{videoId}
        String generatedUrl = "http://localhost:8080/api/videos/stream/" + video.getId();
        video.setUrl(generatedUrl);

        return videoRepository.save(video); // Save updated video with the URL
    }


    public List<Video> getAllVideos(Long userId) {
        return videoRepository.findByUserId(userId);
    }

    public Video findById(Long videoId) {
        return videoRepository.findById(videoId).orElse(null);
    }

    public List<Video> getPaginatedVideos(Timestamp cursor, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "uploadDate"));
        
        if (cursor == null) {
            return videoRepository.findTopByOrderByUploadDateDesc(pageable);
        } else {
            return videoRepository.findByUploadDateBeforeOrderByUploadDateDesc(cursor, pageable);
        }
    }

    public List<Video> getVideosWithPagination(Long lastVideoId, int pageSize) {
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        if (lastVideoId == null || lastVideoId <= 0) {
            return videoRepository.findAll(pageable).getContent();
        } else {
            Page<Video> videoPage = videoRepository.findByIdLessThanOrderByIdDesc(lastVideoId, pageable);
            return videoPage.getContent();
        }
    }
    
//    public List<Video> getAllVideos() {
//        List<Video> videos = videoRepository.findAll();
//
//        // Generate URLs before returning
//        videos.forEach(video -> {
//            String generatedUrl = "http://localhost:8080/api/videos/stream/" + video.getId();
//            video.setUrl(generatedUrl);
//        });
//
//        return videos; // ✅ This ensures only videos are returned, NOT users
//    }
    
    public List<Video> getAllVideos() {
        List<Video> videos = videoRepository.findAll();

        // Ensure URLs are set correctly
        videos.forEach(video -> {
            if (video.getUrl() == null || video.getUrl().isEmpty()) {
                video.setUrl("http://localhost:8080/api/videos/stream/" + video.getId());
            }
        });

        return videos;
    }

    public List<VideoDTO> getAllVideos1() {
        List<Video> videos = videoRepository.findAll();
        return videos.stream().map(VideoDTO::new).collect(Collectors.toList());
    }

}
